package com.athul.memegramspring.dto;

import com.athul.memegramspring.enums.Provider;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private int id;

    @NotBlank
    private String fullName;
    @NotBlank
    @Size(min=4, message = "Username must be minimum of 4 chars")
    private String userHandle;
    @Email(message = "Email address is not valid")
    private String email;
    @NotBlank
    @Size(min=3, message = "Password must be minimum of 3 chars")
    private String password;
    @NotBlank
    @Size(min=4, message = "Username must be minimum of 5 chars")
    private String bio;

    private Provider provider;

    private Set<RoleDto> roles = new HashSet<>();

    private String profilePicUrl;

    private boolean isBlocked;
}
