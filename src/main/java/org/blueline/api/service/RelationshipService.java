package org.blueline.api.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.blueline.api.model.Relationship;
import org.blueline.api.model.User;
import org.blueline.api.model.dto.RelationshipDto;
import org.blueline.api.model.enums.RequestStatus;
import org.blueline.api.repository.RelationshipRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RelationshipService {
    private final AuthService authService;
    private final RelationshipRepository relationshipRepository;
    private final ModelMapper modelMapper;

    public RelationshipDto updateStatus(Authentication authentication, Long relationshipId, RequestStatus newStatus) {
        User user = authService.authenticate(authentication);

        Relationship relationship = relationshipRepository.findById(relationshipId)
                .orElseThrow(() -> new EntityNotFoundException("Relationship not found with id: " + relationshipId));

        return modelMapper.map(relationshipRepository.save(relationship), RelationshipDto.class);
    }
}
