package com.athul.memegramspring.repository;

import com.athul.memegramspring.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken,Integer> {
    Optional<RefreshToken> findByToken(String token);

        @Query("SELECT u FROM RefreshToken u WHERE u.user.email= :username")
        RefreshToken findByUsername(String username);
}
