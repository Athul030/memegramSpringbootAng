package com.athul.memegramspring.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.athul.memegramspring.security.JWTAuthenticationFilter;
import com.athul.memegramspring.security.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@PropertySource("classpath:env.properties")
public class SecurityConfig  {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String id;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String secretAuth;

    @Value("${aws.access-key}")
    private String awsAccessKey;
    @Value("${aws.access-secret-key}")
    private String awsAccessSecretKey;
    @Value("${aws.region}")
    private String awsRegion;





    private  final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private  final JWTAuthenticationFilter jwtAuthenticationFilter;
    private  final UserDetailsService userDetailsService;
    private final CustomCors customCors;
    private final CustomOAuth2UserService oAuth2UserService;
//    private final CustomAccessDeniedHandlerImpl customAccessDeniedHandler;



    @Bean
    public static BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(corsCustomizer->corsCustomizer.configurationSource(customCors))
                .authorizeHttpRequests( authorize->authorize
                        .requestMatchers(HttpMethod.GET,"/images/*" ).permitAll()
                        .requestMatchers("/callback","/callback/**","/api/v1/auth/refreshToken","/api/v1/auth/**").permitAll()
                        .requestMatchers("/src/main/resources/static/images/**").permitAll()
                        .requestMatchers("/static/images/**").permitAll()
                        .requestMatchers("/files/**").permitAll()
                        .requestMatchers("/oauth/**","/login/oauth2/code/google").permitAll()
                        .requestMatchers("/swagger-ui/**","/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/user/test").permitAll()
                        .anyRequest().authenticated())
                .logout((logout)->logout.logoutUrl("/api/v1/auth/logout")
                        .logoutSuccessUrl("/login"))
                .exceptionHandling(ex->ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                        .oauth2Login((login)->login.redirectionEndpoint((endpoint)->
                                endpoint.baseUri("/login/oauth2/authorization"))
                                .userInfoEndpoint(userInfo-> userInfo.userService(oAuth2UserService)))
        ;

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(){
        ClientRegistration googleRegistration = CommonOAuth2Provider.GOOGLE
                .getBuilder("google")
                .clientId(id)
                .clientSecret(secretAuth)
                .build();

        return new InMemoryClientRegistrationRepository(googleRegistration);

    }

    @Bean
    public OAuth2AuthorizedClientManager auth2AuthorizedClientManager(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository){

        OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder().authorizationCode().refreshToken().build();

        DefaultOAuth2AuthorizedClientManager auth2AuthorizedClientManager = new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository,oAuth2AuthorizedClientRepository);
        auth2AuthorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        return auth2AuthorizedClientManager;
    }


    public AWSStaticCredentialsProvider getAwsCredentialsProvider(){
        BasicAWSCredentials awsCred = new BasicAWSCredentials(awsAccessKey,awsAccessSecretKey);
        return new AWSStaticCredentialsProvider(awsCred);
    }

    @Bean
    public AmazonS3 getAmazonS3Client(){
        return AmazonS3ClientBuilder.standard().withRegion(awsRegion)
                .withCredentials(getAwsCredentialsProvider()).build();
    }





}
