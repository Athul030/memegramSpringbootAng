package com.athul.memegramspring.controller;

import com.athul.memegramspring.dto.UserDTO;
import com.athul.memegramspring.entity.RefreshToken;
import com.athul.memegramspring.exceptions.ApiException;
import com.athul.memegramspring.repository.RefreshTokenRepo;
import com.athul.memegramspring.security.JwtAuthRequest;
import com.athul.memegramspring.security.JwtAuthResponse;
import com.athul.memegramspring.security.JwtHelper;
import com.athul.memegramspring.service.RefreshTokenService;
import com.athul.memegramspring.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepo refreshTokenRepo;

    @PostMapping("/login")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "401"), //unauth
            @ApiResponse(responseCode = "404"), //user not found
            @ApiResponse(responseCode = "500") //unexpected error during auth or tok gen
    })
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request)  {

        Authentication authentication = authenticate(request.getUsername(), request.getPassword());
        if(authentication.isAuthenticated()){

            // Check if a refresh token already exists for the user
            RefreshToken existingRefreshToken = refreshTokenRepo.findByUsername(request.getUsername());
            RefreshToken refreshToken = null;
            if (existingRefreshToken == null) {
                 refreshToken = refreshTokenService.createRefreshToken(request.getUsername());
            }else{
                refreshToken =existingRefreshToken;
            }


            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String token = jwtHelper.generateToken(userDetails);
            JwtAuthResponse response= new JwtAuthResponse();
            response.setAccessToken(token);
            response.setToken(refreshToken.getToken());
            response.setUsername(userDetails.getUsername());
            response.setUser(userService.getUserByUsername(userDetails.getUsername()));
            return new ResponseEntity<>(response,HttpStatus.OK);
        }else{
            throw new UsernameNotFoundException("Invalid user request");
        }
    }

    @PostMapping("/register")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500")
    })
    public ResponseEntity<UserDTO> registerUser(@Valid  @RequestBody UserDTO userDTO){
        System.out.println(userDTO);
        UserDTO registeredUserDTO = userService.registerNewUser(userDTO);
        return new ResponseEntity<>(registeredUserDTO,HttpStatus.OK);
    }

    @PostMapping("/refreshToken")
    public JwtAuthResponse refreshToken(@RequestBody RefreshToken refreshToken){
        return refreshTokenService.findByToken(refreshToken.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user->{
                    String accessToken= jwtHelper.generateToken(userDetailsService.loadUserByUsername(user.getEmail()));
                    return JwtAuthResponse.builder().accessToken(accessToken)
                            .token(refreshToken.getToken()).build();
                }).orElseThrow(()->new RuntimeException("Refresh Token is not in DB"));
    }

    private Authentication authenticate(String username,String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);

        try{
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return authentication;
        }catch (BadCredentialsException e){
            throw new ApiException("Invalid username or password");
        }
    }

//    private void authenticate(String username,String password) throws Exception {
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
//
//        try{
//            authenticationManager.authenticate(authenticationToken);
//        }catch (BadCredentialsException e){
//            throw new ApiException("Invalid username or password");
//        }
//    }
}
