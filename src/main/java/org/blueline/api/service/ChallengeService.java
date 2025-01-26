package org.blueline.api.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.catalina.mapper.Mapper;
import org.blueline.api.model.Event;
import org.blueline.api.model.User;
import org.blueline.api.model.challenge.Challenge;
import org.blueline.api.model.challenge.ChallengeCompletion;
import org.blueline.api.model.challenge.ChallengeStatus;
import org.blueline.api.model.dto.ChallengeDto;
import org.blueline.api.model.dto.EventDto;
import org.blueline.api.model.dto.UserDto;
import org.blueline.api.repository.ChallengeCompletionRepository;
import org.blueline.api.repository.ChallengeRepository;
import org.blueline.api.repository.EventRepository;
import org.blueline.api.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final EventRepository eventRepository;

    private final UserRepository userRepository;
    private final ChallengeCompletionRepository challengeCompletionRepository; 
    private final ModelMapper modelMapper;

    private UserDto mapUserEntityToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        return dto;
    }
    public ChallengeService(ChallengeRepository challengeRepository,
    EventRepository eventRepository,
    UserRepository userRepository,
    ChallengeCompletionRepository challengeCompletionRepository,
    ModelMapper modelMapper) {
        this.challengeRepository = challengeRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.challengeCompletionRepository = challengeCompletionRepository;
        this.modelMapper = modelMapper;
    }

    public ChallengeDto createChallengeWithoutEvent(ChallengeDto challengeDto, Authentication authentication) {
        // Création sans event
        Challenge challenge = new Challenge();
        mapDtoToEntity(challengeDto, challenge);
        // Aucune association à un event
        challenge = challengeRepository.save(challenge);
        return mapEntityToDto(challenge);
    }

    public ChallengeDto createChallengeForEvent(Long eventId, ChallengeDto challengeDto, Authentication authentication) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        Challenge challenge = new Challenge();
        mapDtoToEntity(challengeDto, challenge);
        challenge.setEvent(event);
        challenge = challengeRepository.save(challenge);
        return mapEntityToDto(challenge);
    }


    public ChallengeDto updateChallenge(Long id, ChallengeDto challengeDto, Authentication authentication) {
        // Logique métier (vérification droits, etc.)
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow();

        mapDtoToEntity(challengeDto, challenge);
        if (challengeDto.getEventId() != null) {
            Event event = eventRepository.findById(challengeDto.getEventId())
                    .orElseThrow();
            challenge.setEvent(event);
        } else {
            challenge.setEvent(null);
        }

        challenge = challengeRepository.save(challenge);
        return mapEntityToDto(challenge);
    }

    public void deleteChallenge(Long id, Authentication authentication) {
        // Logique métier (vérification droits, etc.)
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow();
        challengeRepository.delete(challenge);
    }

    private void mapDtoToEntity(ChallengeDto dto, Challenge entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setType(dto.getType());
        entity.setDistanceGoal(dto.getDistanceGoal());
        entity.setTimeGoal(dto.getTimeGoal());
        entity.setStreakPeriod(dto.getStreakPeriod());
        entity.setStreakNbOfParticipations(dto.getStreakNbOfParticipations());
        entity.setStatus(dto.getStatus());
    }

    private ChallengeDto mapEntityToDto(Challenge entity) {
        ChallengeDto dto = new ChallengeDto();
        dto.setId(entity.getId());
        dto.setEventId(entity.getEvent() != null ? entity.getEvent().getId() : null);
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setType(entity.getType());
        dto.setDistanceGoal(entity.getDistanceGoal());
        dto.setTimeGoal(entity.getTimeGoal());
        dto.setStreakPeriod(entity.getStreakPeriod());
        dto.setStreakNbOfParticipations(entity.getStreakNbOfParticipations());
        dto.setStatus(entity.getStatus());
        return dto;
    }


    public List<ChallengeDto> getChallengesByStatus(ChallengeStatus status) {
        List<Challenge> challenges = challengeRepository.findByStatus(status);
        return challenges.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    public List<ChallengeDto> getChallengesByEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        List<Challenge> challenges = challengeRepository.findByEvent(event);
        return challenges.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    public List<ChallengeDto> getAllChallenges() {
        List<Challenge> challenges = challengeRepository.findAll();
        return challenges.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getChallengeParticipants(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new EntityNotFoundException("Challenge not found"));

        List<ChallengeCompletion> completions = challengeCompletionRepository.findByChallenge(challenge);
        Set<Long> userIds = completions.stream()
                .map(cc -> cc.getUser().getId())
                .collect(Collectors.toSet());
        List<User> participants = userRepository.findAllById(userIds);

        return participants.stream()
                .map(this::mapUserEntityToDto)
                .collect(Collectors.toList());
    }


    public ChallengeDto getChallengeById(Long id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow();
        return modelMapper.map(challenge, ChallengeDto.class);
    }
}
