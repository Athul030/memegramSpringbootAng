package com.athul.memegramspring.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
public class OAuthController {

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
    public void handleCallBack(HttpServletRequest request) throws IOException {
        authorizationCode = request.getParameter("code");

        String requestBody = "code=" + authorizationCode +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&redirect_uri=" + redirectUri +
                "&grant_type="+"authorization_code"+
                "&access_type="+"offline"+
                "&prompt="+"consent"+
                "&approval_prompt="+"force";

        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody,headers);
        //post request
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(tokenEndpoint,requestEntity,String.class);
        if(responseEntity.getStatusCode() == HttpStatus.OK){
            String accessTokenResponse = responseEntity.getBody();
            System.out.println("Access toke is"+ accessTokenResponse);
        }else{System.out.println("Failed to obtain access token. Status:"+responseEntity.getStatusCode());}
    }
}
