package com.athul.memegramspring.service.impl;

import com.athul.memegramspring.dto.*;
import com.athul.memegramspring.entity.Follow;
import com.athul.memegramspring.entity.Role;
import com.athul.memegramspring.entity.User;
import com.athul.memegramspring.enums.Provider;
import com.athul.memegramspring.exceptions.ResourceNotFoundException;
import com.athul.memegramspring.repository.FollowRepo;
import com.athul.memegramspring.repository.RoleRepo;
import com.athul.memegramspring.repository.UserRepo;
import com.athul.memegramspring.service.UserService;
import com.athul.memegramspring.utils.UserBlockRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;
    private final FollowRepo followRepo;


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
        user.setBlocked(false);
        user.setProfilePicUrl("https://memegramawsbucket1.s3.us-east-2.amazonaws.com//myfolder/images/defaultProfilePicture.jpeg");
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
        user.setBlocked(false);

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
    public Page<UserDTO> getAllUsersForPageable(Pageable pageable) {
        Page<User> page = userRepo.findAllPageable(pageable);
        List<UserDTO> userDTOs = page.stream().map(user -> userToDTO(user))
                .collect(Collectors.toList());
        return new PageImpl<>(userDTOs,pageable,page.getTotalElements());
    }


    @Override
    public void deleteUser(Integer userId) {
        String errorCode = "UserServiceImpl:deleteUser()";
        User user = userRepo.findById(userId).orElseThrow(
                ()->new ResourceNotFoundException("User","Id",userId,errorCode));
        userRepo.delete(user);

    }

    @Override
    public UserDTO saveUserDTOFromOAuth(String username, String profilePicUrl) {
        User user =new User();
        user.setEmail(username);

        int atIndex = username.indexOf('@');
        String userHandle = username.substring(0, atIndex);
        user.setUserHandle(userHandle);
        user.setFullName(userHandle);
        user.setProvider(Provider.GOOGLE);
        user.setProfilePicUrl(profilePicUrl);
//        user.setProfilePicUrl("https://memegramawsbucket1.s3.us-east-2.amazonaws.com//myfolder/images/defaultProfilePicture.jpeg");
        //2 is ROLE_USER
        Role role = roleRepo.findById(2).get();
        user.getRoles().add(role);
        user.setBlocked(false);

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

        List<FollowerDTO> followerIds = followRepo.followerList(user).stream().map(this::mapUserToFollowerDTO).collect(Collectors.toList());
        userDTO.setFollowers(followerIds);

        List<FollowingDTO> followingIds = followRepo.followingList(user).stream().map(this::mapUserToFollowingDTO).collect(Collectors.toList());
        userDTO.setFollowing(followingIds);




        Set<UserDTO> blockedIds = Optional.ofNullable(user.getBlockedUsers())
                .orElse(Collections.emptySet())
                .stream()
                .map(blockedUser -> modelMapper.map(blockedUser, UserDTO.class))
                .collect(Collectors.toSet());
        userDTO.setBlockedUsers(blockedIds);
        userDTO.setBlocked(user.isBlocked());
        userDTO.setProvider(user.getProvider());
        userDTO.setProfilePicUrl(user.getProfilePicUrl());
        return userDTO;

    }

    private FollowerDTO mapUserToFollowerDTO(User user) {
        FollowerDTO followerDTO = modelMapper.map(user, FollowerDTO.class);
        followerDTO.setEmail(user.getEmail());
        return followerDTO;
    }

    private FollowingDTO mapUserToFollowingDTO(User user) {
        FollowingDTO followingDTO = modelMapper.map(user, FollowingDTO.class);
        followingDTO.setEmail(user.getEmail());
        return followingDTO;
    }

    public void changeProfilePic (String username, String fileName){
        String errorCode = "UserServiceImpl:changeProfilePic()";
        User foundedUser=userRepo.findByEmail(username).orElseThrow(()->new ResourceNotFoundException("User","Id",username,errorCode));
        foundedUser.setProfilePicUrl(fileName);
        userRepo.save(foundedUser);
    }

    @Override
    public UserDTO blockAUser(UserBlockRequest userBlockRequest) {
        String errorCode = "UserServiceImpl:blockAUser()";
        User blockingUser = userRepo.findById(userBlockRequest.getBlockingUserId()).orElseThrow(()-> new ResourceNotFoundException("User", "Id", userBlockRequest.getBlockingUserId(),errorCode));
        User blockedUser = userRepo.findById(userBlockRequest.getBlockedUserId()).orElseThrow(()-> new ResourceNotFoundException("User","Id", userBlockRequest.getBlockedUserId(),errorCode));
        blockingUser.getBlockedUsers().add(blockedUser);
        User user = userRepo.save(blockingUser);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;

    }

    @Override
    public UserDTO unBlockAUser(UserBlockRequest userBlockRequest) {
        String errorCode = "UserServiceImpl:unBlockAUser()";
        User blockingUser = userRepo.findById(userBlockRequest.getBlockingUserId()).orElseThrow(()-> new ResourceNotFoundException("User", "Id", userBlockRequest.getBlockingUserId(),errorCode));
        User unBlockedUser = userRepo.findById(userBlockRequest.getBlockedUserId()).orElseThrow(()-> new ResourceNotFoundException("User","Id", userBlockRequest.getBlockedUserId(),errorCode));
        blockingUser.getBlockedUsers().remove(unBlockedUser);
        User user = userRepo.save(blockingUser);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }

    @Override
    public int findUserIdFromUsername(String username) {
        String errorCode = "FollowServiceImpl:findUserIdFromUsername()";

        User user = userRepo.findByEmail(username).orElseThrow(()->new ResourceNotFoundException("User","username",username,errorCode));
        int userId = user.getId();
        return userId;
    }

    @Override
    public FollowDTO followToDTO(Follow follow) {
        FollowDTO followDTO = new FollowDTO();
        followDTO.setId(follow.getId());
        ModelMapper modelMapper1 = new ModelMapper();
        //mapping user entities to my dto
        UserDTO followerDTO = modelMapper1.map(userRepo.findById(follow.getFollower().getId()), UserDTO.class);
        UserDTO followingDTO = modelMapper1.map(userRepo.findById(follow.getFollowing().getId()), UserDTO.class);

        followDTO.setFollower(followerDTO);
        followDTO.setFollowing(followingDTO);
        followDTO.setFollowedDate(follow.getFollowedDate());

        return followDTO;

    }


}
