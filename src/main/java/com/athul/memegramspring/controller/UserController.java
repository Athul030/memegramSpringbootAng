package com.athul.memegramspring.controller;

import com.athul.memegramspring.dto.UserDTO;
import com.athul.memegramspring.exceptions.ApiResponseCustom;
import com.athul.memegramspring.service.UserService;
import com.athul.memegramspring.utils.UserBlockRequest;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<UserDTO> createUser( @Valid  @RequestBody UserDTO userDTO){
        UserDTO createUserDTO=userService.createUser(userDTO);
        return new ResponseEntity<>(createUserDTO, HttpStatus.CREATED);
    }

    @PutMapping("/update/{userId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404",description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404",description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<ApiResponseCustom> deleteUser(@PathVariable Integer userId){
        userService.deleteUser(userId);
        return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom("User deleted successfully",HttpStatus.OK), HttpStatus.OK);
    }

    //get all users without pageable
    @GetMapping("/getAllUsers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        List<UserDTO> listOfUsers =userService.getAllUsers();
        return new ResponseEntity<>(listOfUsers,HttpStatus.OK);
    }

    //get all users with pageable
    @GetMapping("/getAll")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<Page<UserDTO>> getAllUsersWithPageable(Pageable pageable){
        System.out.println("Page size here: "+pageable.getPageSize());
        System.out.println("Page No. : "+pageable.getPageNumber());

        Page<UserDTO> listOfUsersPage =userService.getAllUsersForPageable(pageable);
        return new ResponseEntity<>(listOfUsersPage,HttpStatus.OK);
    }

    //front end receives current user from token
    @GetMapping("/currentUser")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<UserDTO> getSingleUser(@AuthenticationPrincipal UserDetails userDetails){
        Integer userId = userService.getUserByUsername(userDetails.getUsername()).getId();
        UserDTO userDTO =userService.getUserById(userId);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }




//    @PreAuthorize("hasRole('ADMIN')")
    //front end receives other user from path variable for profile visits

    @GetMapping("/{userId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404",description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<UserDTO> getSingleUser(@PathVariable Integer userId){
        UserDTO userDTO =userService.getUserById(userId);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    //test method
    @GetMapping("/test")
    public ResponseEntity<?> tester(){
        System.out.println("connection success");
        return new ResponseEntity<>("hi",HttpStatus.OK);
    }


    //block another user
    @PostMapping("/userBlock")
    public ResponseEntity<UserDTO> blockAUser(@RequestBody UserBlockRequest userBlockRequest){

        UserDTO userDTO = userService.blockAUser(userBlockRequest);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);

    }

    //un block another user
    @PostMapping("/userUnBlock")
    public ResponseEntity<UserDTO> unBlockAUser(@RequestBody UserBlockRequest userBlockRequest){

        UserDTO userDTO = userService.unBlockAUser(userBlockRequest);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);

    }
    
    
    //report user
    @PostMapping("/reportUser/{userId}")
    public ResponseEntity<Boolean> reportUser(@PathVariable int userId, Authentication authentication,@RequestBody String reason){
        int mainUserId = userService.findUserIdFromUsername(authentication.getName());

        Boolean result  = userService.reportUser(mainUserId,userId,reason);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
