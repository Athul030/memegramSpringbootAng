package com.athul.memegramspring.dto;

import com.athul.memegramspring.entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FollowerDTO {

    private int id;
    private String email;
    @JsonIgnore
    private UserDTO user;
    private Role role;
    private boolean isBlocked;
}
