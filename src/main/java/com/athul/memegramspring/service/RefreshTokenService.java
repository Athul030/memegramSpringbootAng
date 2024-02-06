package com.athul.memegramspring.service;

import com.athul.memegramspring.entity.RefreshToken;

import java.sql.Ref;
import java.util.Optional;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(String username);



    Optional<RefreshToken> findByToken(String token);

    RefreshToken verifyExpiration(RefreshToken token);
}
