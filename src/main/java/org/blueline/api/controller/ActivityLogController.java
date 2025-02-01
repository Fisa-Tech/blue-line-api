package org.blueline.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.blueline.api.model.dto.ActiveUsersDto;
import org.blueline.api.model.dto.ExceptionDto;
import org.blueline.api.model.enums.Gender;
import org.blueline.api.model.enums.Period;
import org.blueline.api.model.enums.Status;
import org.blueline.api.model.enums.UserAction;
import org.blueline.api.service.ActivityLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
@Validated
@Tag(name = "Activity", description = "Manage activity statistics of users")
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    @GetMapping("/activeUsers")
    @Operation(
            summary = "Get number of active users by period",
            description = "Get number of active users by period",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Actives users found"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
                    @ApiResponse(responseCode = "404", description = "Actives users not found", content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ActiveUsersDto> getActiveUsers(Authentication authentication, @RequestParam String startDate,
                                                         @RequestParam String endDate,
                                                         @RequestParam Period period,
                                                         @RequestParam(required = false) Status userStatus,
                                                         @RequestParam(required = false) Gender gender,
                                                         @RequestParam(required = false) UserAction userAction) {
        return activityLogService.getActiveUsers(
                authentication,
                startDate,
                endDate,
                period,
                userStatus,
                gender,
                userAction
        );
    }

}
