package org.blueline.api.repository;

import org.blueline.api.model.Event;
import org.blueline.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>{
    boolean existsByName(String name);
    List<Event> findByOrganizer(User organizer);   
}