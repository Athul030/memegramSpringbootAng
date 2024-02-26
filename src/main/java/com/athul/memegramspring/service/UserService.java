package com.athul.memegramspring.service;

import com.athul.memegramspring.dto.FollowDTO;
import com.athul.memegramspring.dto.UserDTO;
import com.athul.memegramspring.entity.Follow;
import com.athul.memegramspring.entity.User;
import com.athul.memegramspring.utils.UserBlockRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserDTO registerNewUser(UserDTO userDTO);

    UserDTO createUser(UserDTO userDto);
    UserDTO updateUser(UserDTO userDTO,Integer userId);
//    UserDTO patchUser(UserDTO userDTO,Integer userId);

    UserDTO getUserById(Integer userId);

    boolean checkUser(String username);

    UserDTO getUserByUsername(String username);

    List<UserDTO> getAllUsers();

    Page<UserDTO> getAllUsersForPageable(Pageable pageable);

    void deleteUser(Integer userId);

    UserDTO saveUserDTOFromOAuth(String username);

    void changeProfilePic(String username, String fileName);

    UserDTO blockAUser(UserBlockRequest userBlockRequest);

    UserDTO unBlockAUser(UserBlockRequest userBlockRequest);

    int findUserIdFromUsername(String username);

    FollowDTO followToDTO(Follow follow);

}
