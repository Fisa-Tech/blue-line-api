package org.blueline.api.service;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.blueline.api.exception.ConflictException;
import org.blueline.api.model.Event;
import org.blueline.api.model.EventStatus;
import org.blueline.api.model.User;
import org.blueline.api.model.dto.EventDto;
import org.blueline.api.repository.EventRepository;
import org.blueline.api.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AuthService authService;

    public EventDto register(EventDto eventDto, Authentication authentication) {
        User owner = authService.authenticate(authentication);

        if (eventRepository.existsByName(eventDto.getName())) {
            throw new ConflictException("Event name already exists");
        }

        // Mapper le DTO vers l'entité Event
        Event event = modelMapper.map(eventDto, Event.class);

        // Définir l'organisateur comme l'utilisateur authentifié
        event.setOrganizer(owner);

        // Initialiser le statut si nécessaire
        if (event.getStatus() == null) {
            event.setStatus(EventStatus.PLANNED);
        }

        // Ajouter l'organisateur comme participant
        event.getParticipants().add(owner);

        Event savedEvent = eventRepository.save(event);
        return modelMapper.map(savedEvent, EventDto.class);
    }

    public EventDto getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow();
        return modelMapper.map(event, EventDto.class);
    }

    public List<EventDto> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(event -> modelMapper.map(event, EventDto.class))
                .collect(Collectors.toList());
    }

    public void deleteEvent(Long id, Authentication authentication) {
        User owner = authService.authenticate(authentication);
    
        Event event = eventRepository.findById(id)
            .orElseThrow();
    
        if (!event.getOrganizer().equals(owner) && !owner.isAdmin()) {
            throw new AccessDeniedException("You do not have permission to delete this event");
        }
    
        eventRepository.delete(event);
    }
    
    public EventDto updateEventStatus(Long id, EventStatus status, Authentication authentication) {
        User owner = authService.authenticate(authentication);

        Event event = eventRepository.findById(id)
            .orElseThrow();

        if (!event.getOrganizer().equals(owner) && !owner.isAdmin()) {
            throw new AccessDeniedException("You do not have permission to update this event");
        }
            

        event.setStatus(status);

        return modelMapper.map(eventRepository.save(event), EventDto.class);
    }

    public EventDto joinEvent(Long eventId, Authentication authentication) {
        User user = authService.authenticate(authentication);
    
        // Récupérer l'utilisateur depuis la base de données
        User managedUser = userRepository.findById(user.getId())
            .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
    
        Event event = eventRepository.findById(eventId)
                .orElseThrow();
    
        if (event.getParticipants().contains(managedUser)) {
            throw new ConflictException("L'utilisateur a déjà rejoint l'événement");
        }
        event.getParticipants().add(managedUser);
    
        Event savedEvent = eventRepository.save(event);
        return modelMapper.map(savedEvent, EventDto.class);
    }
    


    public List<EventDto> getMyEvent(Authentication authentication) {
        User owner = authService.authenticate(authentication);
        List<Event> events = eventRepository.findByOrganizer(owner);
        return events.stream()
                .map(event -> modelMapper.map(event, EventDto.class))
                .collect(Collectors.toList());
    }

    public EventDto leaveEvent(Long eventId, Authentication authentication) {
        User user = authService.authenticate(authentication);

        User managedUser = userRepository.findById(user.getId())
            .orElseThrow(() -> new EntityNotFoundException("User not find"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not find"));

        if (!event.getParticipants().contains(managedUser)) {
            throw new ConflictException("User is not present in this event");
        }

        if (event.getOrganizer().equals(managedUser)) {
            throw new ConflictException("Owner cannot leave his event");
        }

        event.getParticipants().remove(managedUser);

        Event savedEvent = eventRepository.save(event);
        return modelMapper.map(savedEvent, EventDto.class);
    }
}
