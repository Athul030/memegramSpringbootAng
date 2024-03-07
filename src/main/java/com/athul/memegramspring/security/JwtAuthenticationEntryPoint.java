package com.athul.memegramspring.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        System.out.println("From jwtAuthenticationentrypoint"+response.getStatus());
        if(response.getStatus()==403)   {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,"Access Forbidden");
        }else if(response.getStatus()==415){
            response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,"Access Forbidden");
        }else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
        }
    }
}
