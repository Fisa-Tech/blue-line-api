package org.blueline.api.model.dto;

import org.blueline.api.model.enums.Sex;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "User's first name", required = true)
    private String firstname;

    @Schema(description = "User's last name", required = true)
    private String lastname;

    @Schema(accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    private @Email String email;

    private Sex sex;
}
