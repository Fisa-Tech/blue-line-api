package org.blueline.api.controller;

import org.blueline.api.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;

import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

import org.blueline.api.model.EventStatus;
import org.blueline.api.model.dto.EventDto;
import org.blueline.api.model.dto.ExceptionDto;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Validated
@Tag(name = "Event", description = "Manages the creation, retrieval, update, and deletion of events.")
public class EventController {

    private final EventService eventService;

    @PostMapping
    @Operation(
            summary = "Create a new event",
            description = "Authenticated users can create a new event.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Event created"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
                    @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<EventDto> createEvent(@Valid @RequestBody EventDto eventDto, Authentication authentication) {
        return new ResponseEntity<>(eventService.register(eventDto,authentication), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "Get an event by ID",
            description = "Retrieves the details of a specific event by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Event found"),
                    @ApiResponse(responseCode = "404", description = "Event not found", content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<EventDto> getEventById(@PathVariable Long id) {
        return new ResponseEntity<>(eventService.getEventById(id), HttpStatus.OK);
    }

    @GetMapping("me")
    @Operation(
            summary = "Get my event",
            description = "Retrieves the details of me.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Event found"),
                    @ApiResponse(responseCode = "404", description = "Event not found", content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<EventDto>> getMyEvent(Authentication authentication) {
        return new ResponseEntity<>(eventService.getMyEvent(authentication), HttpStatus.OK);
    }

    @GetMapping
    @Operation(
            summary = "List all events",
            description = "Retrieves a list of all events.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of events")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<EventDto>> getAllEvents() {
        return new ResponseEntity<>(eventService.getAllEvents(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete an event",
            description = "Deletes an event. Only the organizer or an admin can delete an event.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Event deleted"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Event not found")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id, Authentication authentication) {
        eventService.deleteEvent(id, authentication);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Update the status of an event",
            description = "Updates the status of an event. Only the organizer or an admin can update an event.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Event updated"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Event not found")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<EventDto> updateEventStatus(
            @PathVariable Long id,
            @RequestParam EventStatus status,
            Authentication authentication) {
        return new ResponseEntity<>(eventService.updateEventStatus(id, status, authentication), HttpStatus.OK);
    }

    @PostMapping("/{id}/join")
    @Operation(
            summary = "Join an event",
            description = "Allows an authenticated user to join an event.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User joined the event"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Event not found"),
                    @ApiResponse(responseCode = "409", description = "User already joined the event")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<EventDto> joinEvent(@PathVariable Long id, Authentication authentication) {
        return new ResponseEntity<>(eventService.joinEvent(id, authentication), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/leave")
    @Operation(
        summary = "Leave an event",
        description = "Allows an authenticated user to leave an event.",
        responses = {
            @ApiResponse(responseCode = "200", description = "User leave the event"),
            @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "404", description = "Event not found", content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
        },
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<EventDto> leaveEvent(@PathVariable Long id, Authentication authentication) {
        return new ResponseEntity<>(eventService.leaveEvent(id, authentication), HttpStatus.OK);
    }


}

