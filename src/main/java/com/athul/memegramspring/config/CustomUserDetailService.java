package com.athul.memegramspring.config;

import com.athul.memegramspring.entity.User;
import com.athul.memegramspring.exceptions.ApiResponseCustom;
import com.athul.memegramspring.exceptions.ApiResponseCustomBlockedUser;
import com.athul.memegramspring.exceptions.ResourceNotFoundException;
import com.athul.memegramspring.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
        try{
            User user = userRepo.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User", "email", username,errorCode));
            if(user.isBlocked()){
                System.out.println(new ApiResponseCustomBlockedUser("You are blocked from accessing Memegram", HttpStatus.UNAUTHORIZED, "401").toString());

                throw  new ApiResponseCustomBlockedUser("You are blocked from accessing Memegram", HttpStatus.UNAUTHORIZED, "401");
            }
            return user;
        }catch (ApiResponseCustomBlockedUser e) {
            throw new UsernameNotFoundException("Error loading user ", e);
        }


    }
}
