package org.blueline.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.blueline.api.model.dto.ExceptionDto;
import org.blueline.api.model.dto.RelationshipDto;
import org.blueline.api.model.dto.RequestStatusDto;
import org.blueline.api.model.dto.UserDto;
import org.blueline.api.model.enums.RequestStatus;
import org.blueline.api.service.RelationshipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relationships")
@RequiredArgsConstructor
@Validated
@Tag(name = "Relationship", description = "Manage relationships between users")
public class RelationshipController {

    private final RelationshipService relationshipService;

    @PostMapping("/{userReceiverFriendId}")
    @Operation(
            summary = "Create a relationship request",
            description = "A user can create a relationship request.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Relationship created"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
                    @ApiResponse(responseCode = "404", description = "User receiver not found", content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<RelationshipDto> create(@PathVariable String userReceiverFriendId, Authentication authentication) {
        return new ResponseEntity<>(relationshipService.create(authentication, userReceiverFriendId), HttpStatus.OK);
    }

    @GetMapping("/pending")
    @Operation(
            summary = "Get relationship requests",
            description = "A user can get its relationship requests.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Relationship requests found"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
                    @ApiResponse(responseCode = "404", description = "Relationships not found", content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<RelationshipDto>> getFriendRequests(Authentication authentication) {
        return new ResponseEntity<>(relationshipService.getFriendRequests(authentication), HttpStatus.OK);
    }

    @GetMapping("/friends")
    @Operation(
            summary = "Get my friends",
            description = "A user can get its friends.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Friends found"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<UserDto>> getFriends(Authentication authentication) {
        return new ResponseEntity<>(relationshipService.getFriends(authentication), HttpStatus.OK);
    }

    @PatchMapping("/{relationshipId}/status")
    @Operation(
            summary = "Update the status of a relationship",
            description = "A user can accept or refuse a relationship request.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Relationship updated"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
                    @ApiResponse(responseCode = "404", description = "Relationship not found", content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<RelationshipDto> update(@PathVariable Long relationshipId, @Valid @RequestBody RequestStatusDto requestStatus, Authentication authentication) {
        return new ResponseEntity<>(relationshipService.updateStatus(authentication, relationshipId, requestStatus.getRequestStatus()), HttpStatus.OK);
    }

    @DeleteMapping("/{friendId}")
    @Operation(
            summary = "Delete an relationship",
            description = "Deletes an relationship.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "RelationShip deleted"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Relationship not found")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> deleteRelationship(@PathVariable Long friendId, Authentication authentication) {
        relationshipService.deleteRelationship(friendId, authentication);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
