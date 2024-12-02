package org.blueline.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

import org.blueline.api.model.EventStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class EventDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    @NotBlank
    private String description;

    @NotBlank
    private EventStatus status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long organizerId;

    @NotBlank
    private String location;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Set<Long> participantIds;

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }

    public void setParticipantIds(Set<Long> participantIds) {
        this.participantIds = participantIds;
    }

    public EventStatus getStatus() {
        return this.status;
    }

}
