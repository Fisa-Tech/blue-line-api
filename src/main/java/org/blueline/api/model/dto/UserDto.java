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
    private String username;
    @Schema(accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;
    private @Email String email;
    private String address;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean isAdmin;
    private Sex sex;
}
