package com.athul.memegramspring.controller;



import com.athul.memegramspring.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;


    @PatchMapping("/blockUser/{userId}")
    public ResponseEntity<?> blockUserByAdmin(@PathVariable Integer userId){
        adminService.blockUser(userId);
        return new ResponseEntity<>("User successfully blocked", HttpStatus.OK);
    }

    @PatchMapping("/unBlockUser/{userId}")
    public ResponseEntity<?> unBlockUserByAdmin(@PathVariable Integer userId){
        adminService.unBlockUser(userId);
        return new ResponseEntity<>("User successfully unblocked", HttpStatus.OK);
    }
}
