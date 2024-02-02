package com.athul.memegramspring.security;

import com.athul.memegramspring.dto.UserDTO;
import com.athul.memegramspring.entity.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JwtAuthResponse {

    private String jwtToken;
    private String username;

    private UserDTO user;
}
