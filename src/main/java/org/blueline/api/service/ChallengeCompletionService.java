package org.blueline.api.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.catalina.mapper.Mapper;
import org.blueline.api.model.Event;
import org.blueline.api.model.User;
import org.blueline.api.model.challenge.Challenge;
import org.blueline.api.model.challenge.ChallengeCompletion;
import org.blueline.api.model.challenge.ChallengeStatus;
import org.blueline.api.model.dto.ChallengeCompletionDTO;
import org.blueline.api.model.dto.ChallengeDto;
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
public class ChallengeCompletionService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeCompletionRepository completionRepository;
    private final UserRepository userRepository;
    private final AuthService authService; // pour récupérer l'utilisateur connecté, si besoin

    // Constructeur
    public ChallengeCompletionService(ChallengeRepository challengeRepository,
                                      ChallengeCompletionRepository completionRepository,
                                      UserRepository userRepository,
                                      AuthService authService) {
        this.challengeRepository = challengeRepository;
        this.completionRepository = completionRepository;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    public ChallengeCompletionDTO addCompletion(Long challengeId, 
    ChallengeCompletionDTO completionDto, 
                                                Authentication authentication) {

        // Récupérer l'utilisateur connecté
        User user = authService.authenticate(authentication);

        // Trouver le challenge
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new EntityNotFoundException("Challenge not found"));

        // Créer l'entité ChallengeCompletion
        ChallengeCompletion completion = new ChallengeCompletion();
        completion.setChallenge(challenge);
        completion.setUser(user); // On associe l'utilisateur authentifié
        completion.setDistanceAchieved(completionDto.getDistanceAchieved());
        completion.setTimeAchieved(completionDto.getTimeAchieved());
        completion.setCompletionDate(
            completionDto.getCompletionDate() != null 
                ? completionDto.getCompletionDate()
                : LocalDateTime.now()
        );

        // Sauvegarde en base
        ChallengeCompletion saved = completionRepository.save(completion);

        // Retourne un DTO
        return mapToDto(saved);
    }

    public List<ChallengeCompletionDTO> getCompletionsByChallenge(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new EntityNotFoundException("Challenge not found"));

        List<ChallengeCompletion> completions = completionRepository.findByChallenge(challenge);

        return completions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ChallengeCompletionDTO updateCompletion(Long completionId, ChallengeCompletionDTO dto, Authentication auth) {
        // Récupérer l'utilisateur connecté
        User user = authService.authenticate(auth);

        // Récupérer la completion
        ChallengeCompletion completion = completionRepository.findById(completionId)
                .orElseThrow(() -> new EntityNotFoundException("Completion not found"));


        // Mettre à jour (selon ce que tu autorises à modifier)
        if (dto.getDistanceAchieved() != null) {
            completion.setDistanceAchieved(dto.getDistanceAchieved());
        }
        if (dto.getTimeAchieved() != null) {
            completion.setTimeAchieved(dto.getTimeAchieved());
        }
        if (dto.getCompletionDate() != null) {
            completion.setCompletionDate(dto.getCompletionDate());
        }
        // etc.

        ChallengeCompletion updated = completionRepository.save(completion);
        return mapToDto(updated);
    }

    public void deleteCompletion(Long completionId, Authentication auth) {
        User user = authService.authenticate(auth);

        ChallengeCompletion completion = completionRepository.findById(completionId)
                .orElseThrow(() -> new EntityNotFoundException("Completion not found"));


        completionRepository.delete(completion);
    }

    // ---------------------------
    // Méthode de mapping interne
    // ---------------------------
    private ChallengeCompletionDTO mapToDto(ChallengeCompletion entity) {
        ChallengeCompletionDTO dto = new ChallengeCompletionDTO();
        // dto.setUserId(entity.getUser().getId());
        dto.setDistanceAchieved(entity.getDistanceAchieved());
        dto.setTimeAchieved(entity.getTimeAchieved());
        dto.setCompletionDate(entity.getCompletionDate());
        return dto;
    }
}
