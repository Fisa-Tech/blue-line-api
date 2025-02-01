package org.blueline.api.repository;

import org.blueline.api.model.ActivityLog;
import org.blueline.api.model.enums.Gender;
import org.blueline.api.model.enums.Status;
import org.blueline.api.model.enums.UserAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    @Query("SELECT a FROM ActivityLog a " +
            "WHERE a.timestamp BETWEEN :start AND :end " +
            "AND (:userStatus IS NULL OR a.user.status = :userStatus) " +
            "AND (:gender IS NULL OR a.user.gender = :gender)" +
            "AND (:userAction IS NULL OR a.action = :userAction)")
    List<ActivityLog> findActiveUsers(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("userStatus") Status userStatus,
            @Param("gender") Gender gender,
            @Param("userAction") UserAction userAction
    );
}
