package com.athul.memegramspring.controller;

import com.athul.memegramspring.dto.UserDTO;
import com.athul.memegramspring.entity.RefreshToken;
import com.athul.memegramspring.entity.User;
import com.athul.memegramspring.exceptions.ApiException;
import com.athul.memegramspring.exceptions.ApiResponseCustomBlockedUser;
import com.athul.memegramspring.repository.RefreshTokenRepo;
import com.athul.memegramspring.security.JwtAuthRequest;
import com.athul.memegramspring.security.JwtAuthResponse;
import com.athul.memegramspring.security.JwtHelper;
import com.athul.memegramspring.service.RefreshTokenService;
import com.athul.memegramspring.service.UserService;
import com.athul.memegramspring.utils.RefreshTokenRequest;
import io.jsonwebtoken.Jwt;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) throws ApiResponseCustomBlockedUser {

        Authentication authentication = authenticate(request.getUsername(), request.getPassword());
        if(authentication.isAuthenticated()){

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(request.getUsername());



            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String accessToken = jwtHelper.generateToken(userDetails);
            JwtAuthResponse response= new JwtAuthResponse();
            response.setAccessToken(accessToken);
            response.setRefreshToken(refreshToken.getToken());
            response.setUsername(userDetails.getUsername());
            userService.setUserPresence(request.getUsername());
            response.setUser(userService.getUserByUsername(userDetails.getUsername()));
            System.out.println("resInAuthController"+response);
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
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "401"),
            @ApiResponse(responseCode = "500")
    })
    public ResponseEntity<JwtAuthResponse> refreshTokenMethod(@RequestBody RefreshTokenRequest refreshToken){
        String extractRefreshToken = refreshToken.getRefreshToken();

        JwtAuthResponse jwtAuthResponse1 =
                refreshTokenService.findByToken(extractRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user->{
                    String accessToken= jwtHelper.generateToken(userDetailsService.loadUserByUsername(user.getEmail()));
                    JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
                    System.out.println("New access token is "+accessToken);
                    jwtAuthResponse.setAccessToken(accessToken);
                    jwtAuthResponse.setUsername(user.getUsername());
                    jwtAuthResponse.setRefreshToken(extractRefreshToken);
                    jwtAuthResponse.setUser(userService.getUserByUsername(user.getUsername()));
                    System.out.println("jwtAuthResponse is" + jwtAuthResponse);
                    return jwtAuthResponse;
                }).orElseThrow(()->new RuntimeException("Refresh Token is not in DB"));
        return new ResponseEntity<>(jwtAuthResponse1,HttpStatus.OK);
    }

    private Authentication authenticate(String username,String password) throws ApiResponseCustomBlockedUser {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);

        try{
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return authentication;

        }catch (BadCredentialsException e){
            if(userService.getUserByUsername(username).isBlocked()==true)   throw new ApiResponseCustomBlockedUser("You are blocked from using Memegram",HttpStatus.LOCKED, "423");
            else throw new ApiException("Invalid username or password");
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

    @PostMapping("/logout")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500")
    })
    public void logout(){
        SecurityContextHolder.getContext();
        SecurityContextHolder.getContext().toString();
        SecurityContextHolder.clearContext();
    }

    @PostMapping("/removeUserPresence/{userId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500")
    })
    public void removeUserPresence(@PathVariable int userId){
        userService.removeUserPresence(userId);
    }




}
