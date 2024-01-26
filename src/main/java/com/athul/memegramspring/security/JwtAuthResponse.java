package com.athul.memegramspring.security;

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
}
