package com.athul.memegramspring.service.impl;

import com.athul.memegramspring.entity.RefreshToken;
import com.athul.memegramspring.repository.RefreshTokenRepo;
import com.athul.memegramspring.repository.UserRepo;
import com.athul.memegramspring.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepo refreshTokenRepo;
    private final UserRepo userRepo;
    @Override
    public RefreshToken createRefreshToken(String username) {
        if(refreshTokenRepo.findByUsername(username)!=null){
            refreshTokenRepo.delete(refreshTokenRepo.findByUsername(username));
        }
        RefreshToken refreshToken = RefreshToken.builder().user(userRepo.findByEmails(username))
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(100* 60 *1000)).build();

        return refreshTokenRepo.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepo.findByToken(token);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepo.delete(token);
            throw new RuntimeException(token.getToken()+ "Refresh Token is expired, please re-login");
        }
        return token;
    }
}
