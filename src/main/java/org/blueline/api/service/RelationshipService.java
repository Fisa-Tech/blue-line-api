package org.blueline.api.service;

import lombok.RequiredArgsConstructor;
import org.blueline.api.exception.BadRequestException;
import org.blueline.api.exception.ForbiddenException;
import org.blueline.api.exception.NotFoundException;
import org.blueline.api.model.Relationship;
import org.blueline.api.model.User;
import org.blueline.api.model.dto.RelationshipDto;
import org.blueline.api.model.dto.UserDto;
import org.blueline.api.model.enums.RequestStatus;
import org.blueline.api.repository.RelationshipRepository;
import org.blueline.api.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RelationshipService {
    private final AuthService authService;
    private final RelationshipRepository relationshipRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public RelationshipDto create(Authentication authentication, String userReceiverFriendId) {
        User user = authService.authenticate(authentication);

        User receiver = userRepository.findByFriendId(userReceiverFriendId)
                .orElseThrow(() -> new NotFoundException("User receiver not found with friend id: " + userReceiverFriendId));
        Relationship relationship = new Relationship();
        relationship.setUserAsker(user);
        relationship.setUserReceiver(receiver);
        relationship.setRequestStatus(RequestStatus.PENDING);

        // check if the request is valid
        if(relationship.getUserReceiver() == null) {
            throw new BadRequestException("User receiver is required");
        } else if (relationship.getUserReceiver().getId().equals(user.getId())) {
            throw new BadRequestException("You cannot create a relationship with yourself");
        } else if (relationshipRepository.existsByUserAskerAndUserReceiverAndRequestStatus(user, relationship.getUserReceiver(), RequestStatus.PENDING)) {
            throw new BadRequestException("Relationship request already exists");
        } else if (!userRepository.existsById(relationship.getUserReceiver().getId())) {
            throw new
                    NotFoundException("User receiver not found with id: " + relationship.getUserReceiver().getId());
        }

        return modelMapper.map(relationshipRepository.save(relationship), RelationshipDto.class);
    }

    public RelationshipDto updateStatus(Authentication authentication, Long relationshipId, RequestStatus newStatus) {
        User user = authService.authenticate(authentication);

        Relationship relationship = relationshipRepository.findById(relationshipId)
                .orElseThrow(() -> new NotFoundException("Relationship not found with id: " + relationshipId));

        // check if the edition is possible
        if(!relationship.getRequestStatus().equals(RequestStatus.PENDING)) {
            throw new BadRequestException("Relationship status has already been updated");
        } else {
            if(newStatus.equals(RequestStatus.ACCEPTED) || newStatus.equals(RequestStatus.REFUSED)) {
                if (!relationship.getUserReceiver().getId().equals(user.getId())) {
                    throw new ForbiddenException("You are not allowed to update this relationship");
                }
                if(newStatus.equals(RequestStatus.ACCEPTED)) {
                    // accept relationship in both ways
                    if(relationshipRepository.existsByUserAskerAndUserReceiverAndRequestStatus(relationship.getUserReceiver(), relationship.getUserAsker(), RequestStatus.PENDING)) {
                        Relationship relationship2 = relationshipRepository.findByUserAskerAndUserReceiverAndRequestStatus(relationship.getUserReceiver(), relationship.getUserAsker(), RequestStatus.PENDING);
                        relationship2.setRequestStatus(RequestStatus.ACCEPTED);
                        relationshipRepository.save(relationship2);
                    }
                }
            } else if(newStatus.equals(RequestStatus.CANCELED)) {
                if (!relationship.getUserAsker().getId().equals(user.getId())) {
                    throw new ForbiddenException("You are not allowed to update this relationship");
                }
            }
        }

        relationship.setRequestStatus(newStatus);

        return modelMapper.map(relationshipRepository.save(relationship), RelationshipDto.class);
    }

    public List<RelationshipDto> getFriendRequests(Authentication authentication) {
        User user = authService.authenticate(authentication);
        List<Relationship> relationshipsRequests = relationshipRepository.findAllByUserReceiverAndRequestStatus(user, RequestStatus.PENDING);
        return relationshipsRequests.stream().map(relationship -> modelMapper.map(relationship, RelationshipDto.class)).toList();
    }

    public List<UserDto> getFriends(Authentication authentication) {
        User user = authService.authenticate(authentication);

        List<Relationship> relationships = relationshipRepository.findAllByUserAndRequestStatus(user, RequestStatus.ACCEPTED);

        return relationships.stream().map(relationship -> {
            if(relationship.getUserAsker().getId().equals(user.getId())) {
                return modelMapper.map(relationship.getUserReceiver(), UserDto.class);
            } else {
                return modelMapper.map(relationship.getUserAsker(), UserDto.class);
            }
        }).distinct().toList();
    }

    public void deleteRelationship(Long friendId, Authentication authentication) {
        User user = authService.authenticate(authentication);
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + friendId));

        List<Relationship> relationships = relationshipRepository
                .findAllByUserAskerAndUserReceiverAndRequestStatus(user, friend, RequestStatus.ACCEPTED);

        relationships.addAll(relationshipRepository
                .findAllByUserAskerAndUserReceiverAndRequestStatus(friend, user, RequestStatus.ACCEPTED));

        relationships.stream().distinct().forEach(relationshipRepository::delete);
    }
}
