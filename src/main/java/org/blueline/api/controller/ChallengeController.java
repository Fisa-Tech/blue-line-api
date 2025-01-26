package org.blueline.api.controller;

import java.util.List;

import org.blueline.api.model.challenge.ChallengeStatus;
import org.blueline.api.model.dto.ChallengeDto;
import org.blueline.api.model.dto.EventDto;
import org.blueline.api.model.dto.ExceptionDto;
import org.blueline.api.model.dto.UserDto;
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
@RequestMapping("/api/challenges")
@Tag(name = "Challenges", description = "Endpoints for managing challenges")
public class ChallengeController {

        private final ChallengeService challengeService;

        public ChallengeController(ChallengeService challengeService) {
                this.challengeService = challengeService;
        }

        @PostMapping
        @Operation(
        summary = "Create a new challenge without an event",
        description = "Creates a challenge not linked to any event.",
        responses = {
                @ApiResponse(responseCode = "201", description = "Challenge created"),
                @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
                @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
                @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
        },
        security = @SecurityRequirement(name = "bearerAuth")
        )
        public ResponseEntity<ChallengeDto> createChallenge(@Valid @RequestBody ChallengeDto challengeDto, 
                                                        Authentication authentication) {
        // On ne passe aucun eventId dans le DTO
        challengeDto.setEventId(null);
        ChallengeDto createdChallenge = challengeService.createChallengeWithoutEvent(challengeDto, authentication);
        return new ResponseEntity<>(createdChallenge, HttpStatus.CREATED);
        }


        @PutMapping("/{id}")
        @Operation(
                summary = "Update an existing challenge",
                description = "Authenticated users can update an existing challenge by its ID.",
                responses = {
                        @ApiResponse(responseCode = "200", description = "Challenge updated"),
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
        public ResponseEntity<ChallengeDto> updateChallenge(@PathVariable("id") Long id,
                                                                @Valid @RequestBody ChallengeDto challengeDto,
                                                                Authentication authentication) {
                ChallengeDto updatedChallenge = challengeService.updateChallenge(id, challengeDto, authentication);
                return new ResponseEntity<>(updatedChallenge, HttpStatus.OK);
        }

        @DeleteMapping("/{id}")
        @Operation(
                summary = "Delete a challenge",
                description = "Authenticated users can delete a challenge by its ID.",
                responses = {
                        @ApiResponse(responseCode = "204", description = "Challenge deleted"),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", 
                                        content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
                        @ApiResponse(responseCode = "404", description = "Challenge not found", 
                                        content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
                },
                security = @SecurityRequirement(name = "bearerAuth")
        )
        public ResponseEntity<Void> deleteChallenge(@PathVariable("id") Long id, Authentication authentication) {
                challengeService.deleteChallenge(id, authentication);
                return ResponseEntity.noContent().build();
        }


        @GetMapping("/pending")
        @Operation(
                summary = "Get challenges with status 'A_RELEVER'",
                description = "Returns all challenges currently in the 'A_RELEVER' status.",
                responses = {
                        @ApiResponse(responseCode = "200", description = "OK")
                },
                security = @SecurityRequirement(name = "bearerAuth")
        )
        public ResponseEntity<List<ChallengeDto>> getAReleverChallenges() {
                List<ChallengeDto> challenges = challengeService.getChallengesByStatus(ChallengeStatus.A_RELEVER);
                return ResponseEntity.ok(challenges);
        }

        @GetMapping("/ongoing")
        @Operation(
                summary = "Get challenges with status 'EN_COURS'",
                description = "Returns all challenges currently in the 'EN_COURS' status.",
                responses = {
                        @ApiResponse(responseCode = "200", description = "OK")
                },
                security = @SecurityRequirement(name = "bearerAuth")
        )
        public ResponseEntity<List<ChallengeDto>> getEnCoursChallenges() {
                List<ChallengeDto> challenges = challengeService.getChallengesByStatus(ChallengeStatus.EN_COURS);
                return ResponseEntity.ok(challenges);
        }

        @GetMapping("/completed")
        @Operation(
                summary = "Get challenges with status 'TERMINE'",
                description = "Returns all challenges currently in the 'TERMINE' status.",
                responses = {
                        @ApiResponse(responseCode = "200", description = "OK")
                },
                security = @SecurityRequirement(name = "bearerAuth")
        )
        public ResponseEntity<List<ChallengeDto>> getTermineChallenges() {
                List<ChallengeDto> challenges = challengeService.getChallengesByStatus(ChallengeStatus.TERMINE);
                return ResponseEntity.ok(challenges);
        }

        @GetMapping("/{challengeId}/participants")
        @Operation(
                summary = "Get participants of a challenge",
                description = "Returns all users participating in the specified challenge.",
                responses = {
                        @ApiResponse(responseCode = "200", description = "OK"),
                        @ApiResponse(responseCode = "404", description = "Challenge not found", 
                                        content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
                },
                security = @SecurityRequirement(name = "bearerAuth")
        )
        public ResponseEntity<List<UserDto>> getChallengeParticipants(@PathVariable("challengeId") Long challengeId) {
                List<UserDto> participants = challengeService.getChallengeParticipants(challengeId);
                return ResponseEntity.ok(participants);
        }

        @GetMapping("/event/{eventId}")
        @Operation(
                summary = "Get all challenges for a given event",
                description = "Returns all challenges associated with the specified event.",
                responses = {
                        @ApiResponse(responseCode = "200", description = "OK"),
                        @ApiResponse(responseCode = "404", description = "Event not found", 
                                        content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
                },
                security = @SecurityRequirement(name = "bearerAuth")
        )
        public ResponseEntity<List<ChallengeDto>> getChallengesByEvent(@PathVariable("eventId") Long eventId) {
                List<ChallengeDto> challenges = challengeService.getChallengesByEvent(eventId);
                return ResponseEntity.ok(challenges);
        }

        @GetMapping
        @Operation(
                summary = "Get all challenges",
                description = "Returns all challenges regardless of their status.",
                responses = {
                        @ApiResponse(responseCode = "200", description = "OK")
                },
                security = @SecurityRequirement(name = "bearerAuth")
        )
        public ResponseEntity<List<ChallengeDto>> getAllChallenges() {
                List<ChallengeDto> challenges = challengeService.getAllChallenges();
                return ResponseEntity.ok(challenges);
        }


        @GetMapping("{id}")
        @Operation(
                summary = "Get a challenge by ID",
                description = "Retrieves the details of a specific event by its ID.",
                responses = {
                        @ApiResponse(responseCode = "200", description = "Event found"),
                        @ApiResponse(responseCode = "404", description = "Event not found", content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
                },
                security = @SecurityRequirement(name = "bearerAuth")
        )
        public ResponseEntity<ChallengeDto> getChallengeById(@PathVariable Long id) {
                return new ResponseEntity<>(challengeService.getChallengeById(id), HttpStatus.OK);
        }
}

