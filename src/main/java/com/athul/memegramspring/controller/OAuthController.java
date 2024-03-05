package com.athul.memegramspring.controller;

import com.athul.memegramspring.dto.UserDTO;
import com.athul.memegramspring.exceptions.ApiResponseCustomBlockedUser;
import com.athul.memegramspring.security.JwtAuthRequest;
import com.athul.memegramspring.security.JwtAuthResponse;
import com.athul.memegramspring.security.JwtHelper;
import com.athul.memegramspring.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;


@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final JwtHelper jwtHelper;


    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;
    private String authorizationCode="";

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

//    private String tokenEndpoint = "https://oauth2.googleapis.com/token";
    private String tokenEndpoint = "https://www.googleapis.com/oauth2/v4/token";


    @GetMapping("/login/oauth2/code/google")
    public ResponseEntity<JwtAuthResponse> handleCallBack(HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException, InterruptedException {
        authorizationCode = request.getParameter("code");

        StringBuffer requestBodyBuffer = new StringBuffer();
        requestBodyBuffer.append("code=").append(authorizationCode).append("&client_id=").append(clientId)
                .append("&client_secret=").append(clientSecret).append( "&redirect_uri=").append(redirectUri)
                .append( "&grant_type=authorization_code")
                .append("&access_type=offline")
                .append("&prompt=consent")
                .append("&approval_prompt=force");
        String requestBody = requestBodyBuffer.toString();


        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody,headers);
        //post request
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(tokenEndpoint,requestEntity,String.class);
        if(responseEntity.getStatusCode() == HttpStatus.OK){
            String accessTokenResponse = responseEntity.getBody();
            System.out.println("Access token is"+ accessTokenResponse);
            String username = getEmailAddressFromTokenResponse(accessTokenResponse);
            if(userService.checkOAuthUserBlocked(username)){
                throw new ApiResponseCustomBlockedUser("You are blocked from using Memegram",HttpStatus.LOCKED, "423");
            }
            //split
            UserDTO userDTO = null;
            if(userService.checkUser(username)){
                userDTO= userService.getUserByUsername(username);
            }else{
                String profilePictureUrl = this.getProfilePictureURLFromIdToken(accessTokenResponse);
                userDTO = userService.saveUserDTOFromOAuth(username,profilePictureUrl);
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            String token = jwtHelper.generateToken(userDetails);
            JwtAuthResponse response = new JwtAuthResponse();
            response.setUsername(username);
            response.setAccessToken(token);
            response.setUser(userDTO);

            ObjectMapper mapper = new ObjectMapper();

            String callbackUrl = "http://localhost:4200/callback?response=" + encodeURIComponent(mapper.writeValueAsString(response));
            httpServletResponse.sendRedirect(callbackUrl);

            return new ResponseEntity<>(response,HttpStatus.OK);
        }else{System.out.println("Failed to obtain access token. Status:"+responseEntity.getStatusCode());
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }



    private String getEmailAddressFromTokenResponse(String accessTokenResponse) throws IOException, InterruptedException {

        String tokenInfoEndpoint = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + getTokenIdFromTokenResponse(accessTokenResponse);
        HttpClient httpClient = HttpClient.newHttpClient();
        java.net.http.HttpRequest httpGet = java.net.http.HttpRequest.newBuilder().uri(URI.create(tokenInfoEndpoint)).build();
        HttpResponse<String> response = httpClient.send(httpGet, HttpResponse.BodyHandlers.ofString());
        // Parse the JSON response
        String responseJson = response.body();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseJson);
        System.out.println(jsonNode);
        String email = jsonNode.get("email").asText();
        return email;
    }

    private String getProfilePictureURLFromIdToken(String accessTokenResponse) throws IOException, InterruptedException {

        String tokenInfoEndpoint = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + getTokenIdFromTokenResponse(accessTokenResponse);

        HttpClient httpClient = HttpClient.newHttpClient();
        java.net.http.HttpRequest httpGet = java.net.http.HttpRequest.newBuilder().uri(URI.create(tokenInfoEndpoint)).build();


        HttpResponse<String> response = httpClient.send(httpGet, HttpResponse.BodyHandlers.ofString());

        // Parse the JSON response
        String responseJson = response.body();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseJson);
        // Extract email from the response
        String profilePicture = jsonNode.get("picture").asText();

        return profilePicture;
    }

    private String getAccessTokenFromTokenResponse(String accessTokenResponse) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(accessTokenResponse);
        String accessToken = jsonNode.get("access_token").asText();

        return accessToken;
    }

    private String getTokenIdFromTokenResponse(String accessTokenResponse) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(accessTokenResponse);
        String idToken = jsonNode.get("id_token").asText();

        return idToken;
    }

    private String encodeURIComponent(String parameter){
        try{
            return URLEncoder.encode(parameter, StandardCharsets.UTF_8.toString());
        }catch (UnsupportedEncodingException e){
            throw new RuntimeException("Error encoding params",e);
        }
    }

}
