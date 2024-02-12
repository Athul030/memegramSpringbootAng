package com.athul.memegramspring.config;

import com.athul.memegramspring.entity.User;
import com.athul.memegramspring.exceptions.ResourceNotFoundException;
import com.athul.memegramspring.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailService implements UserDetailsService {

    private UserRepo userRepo;

    public CustomUserDetailService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String errorCode = "CustomUserDetailsService:loadUserByUsername()";

        User user = userRepo.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User", "email", username,errorCode));

        return user;
    }
}
