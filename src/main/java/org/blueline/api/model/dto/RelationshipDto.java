package org.blueline.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.blueline.api.model.enums.RequestStatus;

@Data
public class RelationshipDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private UserDto userAsker;
    private UserDto userReceiver;
    private RequestStatus requestStatus;
}
