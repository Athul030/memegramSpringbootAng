//package com.athul.memegramspring.controller;
//
//import com.athul.memegramspring.dto.UserDTO;
//import com.athul.memegramspring.exceptions.ApiResponseCustom;
//import com.athul.memegramspring.service.UserService;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import jakarta.validation.Valid;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/admin")
//@CrossOrigin("*")
//public class AdminController {
//
//    private AdminService adminService;
//
//    public UserController(AdminService adminService) {
//
//        this.adminService = adminService;
//    }
//
//    @PostMapping("/register")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Created"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error")
//    })
//    public ResponseEntity<UserDTO> createUser( @Valid  @RequestBody UserDTO userDTO){
//        UserDTO createUserDTO=userService.createUser(userDTO);
//        return new ResponseEntity<>(createUserDTO, HttpStatus.CREATED);
//    }
//
//    @PutMapping("/update/{userId}")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Created"),
//            @ApiResponse(responseCode = "401", description = "Unauthorized"),
//            @ApiResponse(responseCode = "404",description = "User not found"),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error")
//    })
//    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO,
//                                              @PathVariable Integer userId){
//        UserDTO updatedUser = userService.updateUser(userDTO,userId);
//        return ResponseEntity.ok(updatedUser);
//    }
//
//
//
//    //ADMIN
//    //DELETE USER
//
//    @DeleteMapping("/delete/{userId}")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Ok"),
//            @ApiResponse(responseCode = "401", description = "Unauthorized"),
//            @ApiResponse(responseCode = "404",description = "User not found"),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error")
//    })
//    public ResponseEntity<ApiResponseCustom> deleteUser(@PathVariable Integer userId){
//        userService.deleteUser(userId);
//        return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom("User deleted successfully",true), HttpStatus.OK);
//    }
//
//    @GetMapping("/getAll")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Ok"),
//            @ApiResponse(responseCode = "401", description = "Unauthorized"),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error")
//    })
//    public ResponseEntity<List<UserDTO>> getAllUsers(){
//        List<UserDTO> listOfUsers =userService.getAllUsers();
//        return new ResponseEntity<>(listOfUsers,HttpStatus.OK);
//    }
//    //    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping("/{userId}")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Ok"),
//            @ApiResponse(responseCode = "401", description = "Unauthorized"),
//            @ApiResponse(responseCode = "404",description = "User not found"),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error")
//    })
//    public ResponseEntity<UserDTO> getSingleUser(@PathVariable Integer userId){
//        UserDTO userDTO =userService.getUserById(userId);
//        return new ResponseEntity<>(userDTO,HttpStatus.OK);
//    }
//
//    //test method
//    @GetMapping("/test")
//    public ResponseEntity<?> tester(){
//        System.out.println("connection success");
//        return new ResponseEntity<>("hi",HttpStatus.OK);
//    }
//}
