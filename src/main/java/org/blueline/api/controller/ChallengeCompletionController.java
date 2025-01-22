package org.blueline.api.controller;

import java.util.List;

import org.blueline.api.model.challenge.ChallengeStatus;
import org.blueline.api.model.dto.ChallengeCompletionDTO;
import org.blueline.api.model.dto.ChallengeDto;
import org.blueline.api.model.dto.ExceptionDto;
import org.blueline.api.model.dto.UserDto;
import org.blueline.api.service.ChallengeCompletionService;
import org.blueline.api.service.ChallengeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/api/challenge-completions")
@Tag(name = "Challenge Completions", description = "Endpoints for managing challenge completions")
public class ChallengeCompletionController {

    private final ChallengeCompletionService completionService;

    public ChallengeCompletionController(ChallengeCompletionService completionService) {
        this.completionService = completionService;
    }

    @PostMapping("/challenge/{challengeId}")
    @Operation(
        summary = "Add a new completion for a given challenge",
        description = "Creates a new ChallengeCompletion for the authenticated user (or a specific user) for a given challenge.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Completion created"),
            @ApiResponse(responseCode = "400", description = "Bad request", 
                content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", 
                content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "404", description = "Challenge not found", 
                content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "409", description = "Conflict", 
                content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
        },
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ChallengeCompletionDTO> addCompletion(
            @PathVariable("challengeId") Long challengeId,
            @Valid @RequestBody ChallengeCompletionDTO completionDto,
            Authentication authentication) {
        
        // Appel du service pour créer la completion
        ChallengeCompletionDTO created = completionService.addCompletion(challengeId, completionDto, authentication);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/challenge/{challengeId}")
    @Operation(
        summary = "Get all completions for a challenge",
        description = "Returns a list of all ChallengeCompletions associated with the specified challenge.",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Challenge not found", 
                content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
        },
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<ChallengeCompletionDTO>> getCompletionsByChallenge(@PathVariable("challengeId") Long challengeId) {
        List<ChallengeCompletionDTO> completions = completionService.getCompletionsByChallenge(challengeId);
        return ResponseEntity.ok(completions);
    }

    @PutMapping("/{completionId}")
    @Operation(
        summary = "Update an existing completion",
        description = "Update a ChallengeCompletion by its ID (for authenticated user or admin).",
        responses = {
            @ApiResponse(responseCode = "200", description = "Completion updated"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Completion not found")
        },
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ChallengeCompletionDTO> updateCompletion(
            @PathVariable("completionId") Long completionId,
            @Valid @RequestBody ChallengeCompletionDTO completionDto,
            Authentication authentication) {

                ChallengeCompletionDTO updated = completionService.updateCompletion(completionId, completionDto, authentication);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{completionId}")
    @Operation(
        summary = "Delete a completion",
        description = "Deletes a ChallengeCompletion by its ID (for authorized user or admin).",
        responses = {
            @ApiResponse(responseCode = "204", description = "Completion deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Completion not found")
        },
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> deleteCompletion(@PathVariable("completionId") Long completionId,
                                                 Authentication authentication) {
        completionService.deleteCompletion(completionId, authentication);
        return ResponseEntity.noContent().build();
    }
}
