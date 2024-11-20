package org.blueline.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    private @Email String email;

    private String firstName;

    private String lastName;

    @Schema(accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    private boolean isAdmin;
}
