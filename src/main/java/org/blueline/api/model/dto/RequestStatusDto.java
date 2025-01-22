package org.blueline.api.model.dto;

import lombok.Data;
import org.blueline.api.model.enums.RequestStatus;

@Data
public class RequestStatusDto {
    private RequestStatus requestStatus;
}
