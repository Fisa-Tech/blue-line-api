package org.blueline.api.service;

import lombok.RequiredArgsConstructor;
import org.blueline.api.exception.UnauthorizedException;
import org.blueline.api.model.ActivityLog;
import org.blueline.api.model.User;
import org.blueline.api.model.dto.ActiveUsersDto;
import org.blueline.api.model.enums.Gender;
import org.blueline.api.model.enums.Period;
import org.blueline.api.model.enums.Status;
import org.blueline.api.model.enums.UserAction;
import org.blueline.api.repository.ActivityLogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;


import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;
    private final AuthService authService;

    public void saveActivity(Authentication authentication, String action, String endpoint) {
        ActivityLog activityLog = new ActivityLog();

        User user = authService.authenticate(authentication);
        activityLog.setUser(user);
        activityLog.setAction(getUserAction(action, endpoint));
        activityLog.setTimestamp(new Timestamp(System.currentTimeMillis()).toLocalDateTime());

        activityLogRepository.save(activityLog);
    }

    private UserAction getUserAction(String action, String endpoint) {
        String joinEventRegex = "^api/events/\\d+/join$";
        Pattern joinEventPattern = Pattern.compile(joinEventRegex);

        String joinChallengeRegex = "^api/challenge-completions/challenge/\\d+$";
        Pattern joinChallengePattern = Pattern.compile(joinChallengeRegex);

        if(Objects.equals(action, "POST") && joinEventPattern.matcher(endpoint).matches()) {
            return UserAction.JOIN_EVENT;
        } else if(Objects.equals(action, "POST") && joinChallengePattern.matcher(endpoint).matches()) {
            return UserAction.JOIN_CHALLENGE;
        } else if (Objects.equals(action, "POST") && Objects.equals(endpoint, "api/challenges")) {
            return UserAction.CREATE_CHALLENGE;
        } else if (Objects.equals(action, "POST") && Objects.equals(endpoint, "api/events")) {
            return UserAction.CREATE_EVENT;
        } else {
            return UserAction.OTHER;
        }

    }

    public ResponseEntity<ActiveUsersDto> getActiveUsers(Authentication authentication,
                                                         String startDate,
                                                         String endDate,
                                                         Period period,
                                                         Status userStatus,
                                                         Gender gender,
                                                         UserAction userAction) {
        User user = authService.authenticate(authentication);
        if(!user.isAdmin()) {
            throw new UnauthorizedException("You do not have permission to get users statistics");
        }

        LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
        LocalDateTime end = LocalDate.parse(endDate).atTime(23, 59, 59);

        List<ActivityLog> activityLogs = activityLogRepository.findActiveUsers(
                start,
                end,
                userStatus,
                gender,
                userAction
        );

        int totalActiveUsersCount = (int) activityLogs.stream()
                .map(ActivityLog::getUser)
                .distinct()
                .count();

        Map<Timestamp, Set<Long>> activeUsersByPeriod = new TreeMap<>();

        for (ActivityLog log : activityLogs) {
            LocalDateTime timestamp = log.getTimestamp();
            Long userId = log.getUser().getId();


            LocalDateTime periodStart = getPeriodStart(timestamp, period);

            activeUsersByPeriod
                    .computeIfAbsent(Timestamp.valueOf(periodStart.toLocalDate().atStartOfDay()), k -> new HashSet<>())
                    .add(userId);
        }

        Map<Timestamp, Integer> activeUsersCountByPeriod = activeUsersByPeriod.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().size()
                ));

        ActiveUsersDto activeUsersDto = new ActiveUsersDto();
        activeUsersDto.setTotalActiveUsers(totalActiveUsersCount);
        activeUsersDto.setActiveUsersPerPeriod(activeUsersCountByPeriod);


        return ResponseEntity.ok(activeUsersDto);
    }

    private LocalDateTime getPeriodStart(LocalDateTime timestamp, Period period) {
        return switch (period) {
            case DAY -> timestamp.toLocalDate().atStartOfDay();
            case WEEK -> timestamp.with(DayOfWeek.MONDAY).toLocalDate().atStartOfDay();
            case MONTH -> timestamp.withDayOfMonth(1).toLocalDate().atStartOfDay();
            case YEAR -> timestamp.withDayOfYear(1).toLocalDate().atStartOfDay();
        };
    }
}
