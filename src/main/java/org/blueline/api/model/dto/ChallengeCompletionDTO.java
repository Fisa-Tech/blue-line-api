package org.blueline.api.model.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeCompletionDTO {
    // @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
   //il faudrait en fait créer un deuxieme DTO pour etre plus propre, mais nous manquons de temps pour le faire
    private Long userId; 
    private Double distanceAchieved;
    private Long timeAchieved;
    private LocalDateTime completionDate; 
}
