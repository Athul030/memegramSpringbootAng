package com.athul.memegramspring.service;


import com.athul.memegramspring.dto.UserDTO;
import org.springframework.http.ResponseEntity;

public interface AdminService {

    UserDTO blockUser(int userId);

    UserDTO unBlockUser(int userId);

}
