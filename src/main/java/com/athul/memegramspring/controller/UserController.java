package com.athul.memegramspring.controller;

import com.athul.memegramspring.dto.UserDTO;
import com.athul.memegramspring.exceptions.ApiResponse;
import com.athul.memegramspring.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> createUser( @Valid  @RequestBody UserDTO userDTO){
        UserDTO createUserDTO=userService.createUser(userDTO);
        return new ResponseEntity<>(createUserDTO, HttpStatus.CREATED);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO,
                                              @PathVariable Integer userId){
        UserDTO updatedUser = userService.updateUser(userDTO,userId);
        return ResponseEntity.ok(updatedUser);
    }

//    @PatchMapping("/update/{userId}")
//    public ResponseEntity<UserDTO> patchUser(@Valid @RequestBody UserDTO userDTO,
//                                              @PathVariable Integer userId){
//        UserDTO updatedUser = userService.patchUser(userDTO,userId);
//        return ResponseEntity.ok(updatedUser);
//    }

    //ADMIN
    //DELETE USER

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer userId){
        userService.deleteUser(userId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("User deleted successfully",true), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        List<UserDTO> listOfUsers =userService.getAllUsers();
        return new ResponseEntity<>(listOfUsers,HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getSingleUser(@PathVariable Integer userId){
        UserDTO userDTO =userService.getUserById(userId);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }
}
