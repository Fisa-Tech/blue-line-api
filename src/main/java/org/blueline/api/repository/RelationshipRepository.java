package org.blueline.api.repository;

import org.blueline.api.model.Relationship;
import org.blueline.api.model.User;
import org.blueline.api.model.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
    boolean existsByUserAskerAndUserReceiverAndRequestStatus(User userAsker, User userReceiver, RequestStatus requestStatus);

    Relationship findByUserAskerAndUserReceiverAndRequestStatus(User userReceiver, User userAsker, RequestStatus requestStatus);

    List<Relationship> findAllByUserAskerAndUserReceiverAndRequestStatus(User userReceiver, User userAsker, RequestStatus requestStatus);

    List<Relationship> findAllByUserReceiverAndRequestStatus(User user, RequestStatus requestStatus);

    @Query("SELECT r FROM Relationship r WHERE (r.userReceiver = :user OR r.userAsker = :user) AND r.requestStatus = :status")
    List<Relationship> findAllByUserAndRequestStatus(@Param("user") User user, @Param("status") RequestStatus requestStatus);

    void deleteAllByUserReceiverAndUserAskerAndRequestStatus(User userReceiver, User userAsker, RequestStatus requestStatus);
}
