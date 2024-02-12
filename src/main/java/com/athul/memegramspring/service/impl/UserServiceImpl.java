package com.athul.memegramspring.service.impl;

import com.athul.memegramspring.dto.RoleDto;
import com.athul.memegramspring.dto.UserDTO;
import com.athul.memegramspring.entity.Role;
import com.athul.memegramspring.entity.User;
import com.athul.memegramspring.enums.Provider;
import com.athul.memegramspring.exceptions.ResourceNotFoundException;
import com.athul.memegramspring.repository.RoleRepo;
import com.athul.memegramspring.repository.UserRepo;
import com.athul.memegramspring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;



    @Override
    public UserDTO registerNewUser(UserDTO userDto) {
        User user =new User();

        user.setFullName(userDto.getFullName());
        user.setUserHandle(userDto.getUserHandle());
        user.setEmail(userDto.getEmail());
        user.setBio(userDto.getBio());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setProvider(Provider.LOCAL);
        //2 is ROLE_USER
        Role role = roleRepo.findById(2).get();
        user.getRoles().add(role);
        User savedUser = userRepo.save(user);
        return userToDTO(savedUser);
    }

    @Override
    public UserDTO createUser(UserDTO userDto) {
        User user =new User();

        user.setFullName(userDto.getFullName());
        user.setUserHandle(userDto.getUserHandle());
        user.setEmail(userDto.getEmail());
        user.setBio(userDto.getBio());
        user.setPassword(userDto.getPassword());
        user.setProvider(Provider.LOCAL);
        User savedUser = userRepo.save(user);
        return userToDTO(savedUser);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, Integer userId) {
        String errorCode = "UserServiceImpl:updateUser()";
        User user=userRepo.findById(userId).orElseThrow(
                ()->new ResourceNotFoundException("User","Id",userId,errorCode));
        user.setFullName(userDTO.getFullName());
        user.setUserHandle(userDTO.getUserHandle());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setBio(userDTO.getBio());
        user.setProvider(userDTO.getProvider());
        User updatedUser = userRepo.save(user);
        return userToDTO(updatedUser);
    }

//    @Override
//    public UserDTO patchUser(UserDTO userDTO, Integer userId) {
//
//        User existingUser=userRepo.findById(userId).orElseThrow(
//                ()->new ResourceNotFoundException("User","Id",userId));
//        if(userDTO.getFullName()!=null) existingUser.setFullName(userDTO.getFullName());
//        if(userDTO.getUserHandle()!=null) existingUser.setUserHandle(userDTO.getUserHandle());
//        if(userDTO.getEmail()!=null) existingUser.setEmail(userDTO.getEmail());
//        if(userDTO.getPassword()!=null) existingUser.setPassword(userDTO.getPassword());
//        if(userDTO.getBio()!=null) existingUser.setBio(userDTO.getBio());
//        User updatedUser = userRepo.save(existingUser);
//        return userToDTO(updatedUser);
//    }

    @Override
    public UserDTO getUserById(Integer userId) {
        String errorCode = "UserServiceImpl:getUserById()";
        User foundedUser=userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","Id",userId,errorCode));
        return userToDTO(foundedUser);
    }

    @Override
    public boolean checkUser(String username) {
        Optional<User> userOptional = userRepo.findByEmail(username);
        return userOptional.isPresent();
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        String errorCode = "UserServiceImpl:getUserByUsername()";

        User foundedUser=userRepo.findByEmail(username).orElseThrow(()->new ResourceNotFoundException("User","Id",username,errorCode));

        return userToDTO(foundedUser);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepo.findAll();
        List<UserDTO> userDTOs = users.stream().map(user -> userToDTO(user))
                .collect(Collectors.toList());
        return userDTOs;
    }

    @Override
    public void deleteUser(Integer userId) {
        String errorCode = "UserServiceImpl:deleteUser()";
        User user = userRepo.findById(userId).orElseThrow(
                ()->new ResourceNotFoundException("User","Id",userId,errorCode));
        userRepo.delete(user);

    }

    @Override
    public UserDTO saveUserDTOFromOAuth(String username) {
        User user =new User();
        user.setEmail(username);

        int atIndex = username.indexOf('@');
        String userHandle = username.substring(0, atIndex);
        user.setUserHandle(userHandle);
        user.setFullName(userHandle);
        user.setProvider(Provider.GOOGLE);

        //2 is ROLE_USER
        Role role = roleRepo.findById(2).get();
        user.getRoles().add(role);

        User savedOAuthUser = userRepo.save(user);
        return userToDTO(savedOAuthUser);
    }

    public User dtoToUser(UserDTO userDTO){
        User user =new User();
        user.setId(userDTO.getId());
        user.setFullName(userDTO.getFullName());
        user.setUserHandle(userDTO.getUserHandle());
        user.setEmail(userDTO.getEmail());
        user.setBio(userDTO.getBio());
        user.setPassword(userDTO.getPassword());
        return user;
    }

    private UserDTO userToDTO(User user){
        UserDTO userDTO=new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFullName(user.getFullName());
        userDTO.setUserHandle(user.getUserHandle());
        userDTO.setEmail(user.getEmail());
        userDTO.setBio(user.getBio());
        userDTO.setPassword(user.getPassword());
        Set<RoleDto> roleDtos = user.getRoles().stream().map(role -> modelMapper.map(role, RoleDto.class)).collect(Collectors.toSet());
        userDTO.setRoles(roleDtos);
        userDTO.setProvider(user.getProvider());
        return userDTO;

    }
}
