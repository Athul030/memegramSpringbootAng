package com.athul.memegramspring.service;

import com.athul.memegramspring.dto.UserDTO;
import com.athul.memegramspring.entity.User;

import java.util.List;

public interface UserService {

    UserDTO registerNewUser(UserDTO userDTO);

    UserDTO createUser(UserDTO userDto);
    UserDTO updateUser(UserDTO userDTO,Integer userId);
//    UserDTO patchUser(UserDTO userDTO,Integer userId);

    UserDTO getUserById(Integer userId);

    List<UserDTO> getAllUsers();

    void deleteUser(Integer userId);
}
