package com.athul.memegramspring.controller;



import com.athul.memegramspring.dto.UserDTO;
import com.athul.memegramspring.enums.Provider;
import com.athul.memegramspring.service.AdminService;
import com.athul.memegramspring.service.PostService;
import com.athul.memegramspring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;
    private final PostService postService;

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

    @GetMapping("/providerChartData")
    public ResponseEntity<Map<Provider,Integer>> dataOfProviderCount(){
        Map<Provider,Integer> map = userService.getAllUsersForDashboard();
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    @GetMapping("/postsChartData")
    public ResponseEntity<Map<Provider,Integer>> dataOfPostsForGraph(){
        Map<Provider,Integer> map = userService.getAllUsersForDashboard();
        return new ResponseEntity<>(map,HttpStatus.OK);
    }
}
