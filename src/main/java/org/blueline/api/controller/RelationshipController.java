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
import org.blueline.api.model.dto.UserDto;
import org.blueline.api.service.RelationshipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relationships")
@RequiredArgsConstructor
@Validated
@Tag(name = "Relationship", description = "Manage relationships between users")
public class RelationshipController {

    private final RelationshipService relationshipService;

    @PatchMapping("/{userId}/status")
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
    public ResponseEntity<RelationshipDto> update(@Valid @RequestBody RelationshipDto relationshipDto, Authentication authentication) {
        return new ResponseEntity<>(relationshipService.updateStatus(authentication, relationshipDto), HttpStatus.OK);
    }
}
