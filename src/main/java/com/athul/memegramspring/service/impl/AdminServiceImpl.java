package com.athul.memegramspring.service.impl;
import com.athul.memegramspring.dto.UserDTO;
import com.athul.memegramspring.entity.User;
import com.athul.memegramspring.exceptions.ResourceNotFoundException;
import com.athul.memegramspring.repository.UserRepo;
import com.athul.memegramspring.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepo userRepo;


    @Override
    public UserDTO blockUser(int userId) {
        String errorCode = "AdminServiceImpl:blockUser()";
        User user = userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","UserId",userId,errorCode));
        user.setBlocked(true);
        User user1 = userRepo.save(user);
        ModelMapper modelMapper = new ModelMapper();
        UserDTO userDTO = modelMapper.map(user1, UserDTO.class);
        return userDTO;
    }

    @Override
    public UserDTO unBlockUser(int userId) {
        String errorCode = "AdminServiceImpl:unBlockUser()";
        User user = userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","UserId",userId,errorCode));
        user.setBlocked(false);
        User user1 = userRepo.save(user);
        ModelMapper modelMapper = new ModelMapper();
        UserDTO userDTO = modelMapper.map(user1, UserDTO.class);
        return userDTO;
    }
}
