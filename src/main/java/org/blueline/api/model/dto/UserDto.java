package org.blueline.api.model.dto;

import org.blueline.api.model.enums.Gender;
import org.blueline.api.model.enums.Status;



import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "User's first name")
    private String firstname;

    @Schema(description = "User's last name")
    private String lastname;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "User's friend Id")
    private String friendId;

    @Schema(description = "User's email", requiredMode = Schema.RequiredMode.REQUIRED)
    private @Email String email;

    @Schema(description = "User's password", requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    private Gender gender;

    private String avatar;

    private Status status;
}