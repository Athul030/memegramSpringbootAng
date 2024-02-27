package com.athul.memegramspring.controller;



import com.athul.memegramspring.dto.UserDTO;
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
    public ResponseEntity<UserDTO> blockUserByAdmin(@PathVariable Integer userId){
        UserDTO userDTO = adminService.blockUser(userId);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PatchMapping("/unBlockUser/{userId}")
    public ResponseEntity<UserDTO> unBlockUserByAdmin(@PathVariable Integer userId){
        UserDTO userDTO = adminService.unBlockUser(userId);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}
