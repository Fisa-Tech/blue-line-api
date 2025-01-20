package org.blueline.api.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeCompletionDTO {

    // private Long userId; 
    private Double distanceAchieved;
    private Long timeAchieved;
    private LocalDateTime completionDate; 
}
