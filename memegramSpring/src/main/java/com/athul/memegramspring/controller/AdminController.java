package com.athul.memegramspring.controller;

import com.athul.memegramspring.dto.AdminDTO;
import com.athul.memegramspring.dto.LoginDTO;
import com.athul.memegramspring.entity.Admin;
import com.athul.memegramspring.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/admin")
public class AdminController {

    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

//    @GetMapping("/login?logout")
//    public String performLogout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
//        // .. perform logout
//        this.logoutHandler.logout(request,response,authentication);
//        return "LOG OUT";
//    }

    @PostMapping("/logout")
    public String Logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication2 = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("1"+authentication);
        System.out.println("2"+authentication2);
        // .. perform logout
        this.logoutHandler.logout(request,response,authentication2);
        System.out.println("1"+authentication);
        System.out.println("2"+authentication2);
        return "LOG OUT2";
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody AdminDTO adminDTO, BindingResult result){

        if(result.hasErrors()){
            //send back the entered info
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
        return (adminService.register(adminDTO));


    }

//    @PostMapping("/login")
//    public ResponseEntity<String> login(@Valid @RequestBody LoginDTO loginDTO){
//        return (adminService.login(loginDTO));
//    }

    @GetMapping("/check")
    public String check( Principal principal){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        if (authentication != null && authentication.isAuthenticated() ) {
            // User is authenticated
            return "User is  logged in";
        } else {
            // User is not authenticated
            return "User is not logged in";
        }


    }

//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(){
//        return (adminService.logout());
//    }


}
