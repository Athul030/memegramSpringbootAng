package com.athul.memegramspring.security;

import com.athul.memegramspring.config.CustomUserDetailService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

//    private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

    private final JwtHelper jwtHelper;
    private final CustomUserDetailService customUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //Authorization
        String requestHeader = request.getHeader("Authorization");
//        logger.info("Header:{}",requestHeader);
        String username = null;
        String token = null;

        if(requestHeader!=null && requestHeader.startsWith("Bearer")){
            token = requestHeader.substring(7);
            try{
                username = jwtHelper.getUsernameFromToken(token);
            }catch (IllegalArgumentException e){
//                logger.info("Illegal argument while fetching the username !!");
                System.out.println("Illegal argument while fetching the username !!");
                e.printStackTrace();
            }catch(ExpiredJwtException e){
//                logger.info("Given jwt token is expired !!");
                System.out.println("Given jwt token is expired !!");

                e.printStackTrace();
            }catch(MalformedJwtException e){
//                logger.info("Some changes has done in token !! Invalid token");
                System.out.println("Some changes has done in token !! Invalid token");

                e.printStackTrace();
            }
        }else{
//            logger.info("JWT Token does not begin with Bearer!!");
            System.out.println("JWT Token does not begin with Bearer!!");
        }

        if(username!=null && SecurityContextHolder.getContext().getAuthentication() ==null){

            //fetch user details from username
            UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
            Boolean validateToken = jwtHelper.validateToken(token, userDetails);
            if(validateToken){

                //set the authentication
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }else{
//                logger.info("Invalid JWT token");
                System.out.println("Invalid JWT token");
            }
        }else{
            System.out.println("Username is null or context is not null ");
        }


        filterChain.doFilter(request,response);
    }
}
