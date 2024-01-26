package com.athul.memegramspring.service.impl;

import com.athul.memegramspring.dto.AdminDTO;
import com.athul.memegramspring.dto.LoginDTO;
import com.athul.memegramspring.entity.Admin;
import com.athul.memegramspring.repository.AdminRepository;
import com.athul.memegramspring.repository.RoleRepository;
import com.athul.memegramspring.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AdminServiceImpl implements AdminService  {

    private final AdminRepository adminRepository;

    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public AdminServiceImpl(AdminRepository adminRepository, AuthenticationManager authenticationManager, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<String> register(AdminDTO adminDTO) {
        if(adminDTO.getEmail()==null || adminDTO.getPassword()==null || adminDTO.getRepeatPassword()==null){
            return new ResponseEntity<>("Empty data provided",HttpStatus.BAD_REQUEST);
        }

        Admin admin = adminRepository.findByUsername(adminDTO.getEmail());
        if (admin != null) {

            return new ResponseEntity<>("Username already taken",HttpStatus.FORBIDDEN);
        }
        if (adminDTO.getPassword().equals(adminDTO.getRepeatPassword())) {

            adminRepository.save(AdminDTOToAdmin(adminDTO));
            return new ResponseEntity<>("Account created", HttpStatus.CREATED);
            } else {
            return new ResponseEntity<>("Password do not match",HttpStatus.UNAUTHORIZED);
        }

    }

    @Override
    public ResponseEntity<String> login(LoginDTO loginDTO) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
            System.out.println(authentication.isAuthenticated());
            System.out.println("logged in"+authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return new ResponseEntity<>("User signed in successfully!", HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }

    }

    @Override
    public ResponseEntity<String> logout() {
        return new ResponseEntity<>("User logged out successfully",HttpStatus.OK);
    }

    private Admin AdminDTOToAdmin(AdminDTO adminDTO){
        Admin admin = new Admin();
        admin.setUsername(adminDTO.getEmail());
        admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
        admin.setRoles(Arrays.asList(roleRepository.findByName("ADMIN")));
        return admin;
    }
}
