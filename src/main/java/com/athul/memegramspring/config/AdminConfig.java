//package com.athul.memegramspring.config;
//
//import com.athul.memegramspring.repository.AdminRepository;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.BeanIds;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
//import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//@Configuration
//@EnableWebSecurity
//public class AdminConfig {
//
//    private final AdminRepository adminRepository;
//
//    public AdminConfig(AdminRepository adminRepository) {
//        this.adminRepository = adminRepository;
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService(){
//        return new AdminServiceConfig(adminRepository);
//    }
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder(){
//        return  new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider(){
//        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
//        provider.setUserDetailsService(userDetailsService());
//        provider.setPasswordEncoder(passwordEncoder());
//        return provider;
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(authorize->authorize
//                        .requestMatchers("/admin/register","/admin/login","/admin/login?logout","/admin/check","/error/**").permitAll()
//                        .requestMatchers("/admin/logout","/admin/logout?continue").hasAuthority("ADMIN")
//                        .anyRequest().authenticated()
//
//        ).formLogin(form->form
//                        .loginPage("/admin/login")
//                        .loginProcessingUrl("/admin/login")
//                        .permitAll())
//
//                .sessionManagement(sessionManagement -> sessionManagement
//                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
//                .exceptionHandling().authenticationEntryPoint(new Http403ForbiddenEntryPoint())
//                .and().anonymous().disable()
//                .securityContext().securityContextRepository(new HttpSessionSecurityContextRepository());
////                .logout(logout-> logout
////                        .invalidateHttpSession(true)
////                        .clearAuthentication(true)
////                        .logoutRequestMatcher(new AntPathRequestMatcher("/admin/logout"))
////                        .logoutSuccessUrl("/admin/login?logout")
////                        .permitAll())
//
//        ;
//
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//
//}
