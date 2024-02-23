package com.athul.memegramspring.service.impl;

import com.athul.memegramspring.dto.UserDTO;
import com.athul.memegramspring.entity.Follow;
import com.athul.memegramspring.entity.User;
import com.athul.memegramspring.exceptions.ResourceNotFoundException;
import com.athul.memegramspring.repository.FollowRepo;
import com.athul.memegramspring.repository.UserRepo;
import com.athul.memegramspring.service.FollowService;
import com.athul.memegramspring.utils.FollowerFollowingDetails;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepo followRepo;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;


    @Override
    public Follow follow(int followerId, int followingId) {
        Follow connection = new Follow();
        connection.setFollowingId(userRepo.getReferenceById(followingId));
        connection.setFollowerId(userRepo.getReferenceById(followerId));
        connection.setFollowedDate(LocalDateTime.now());
        followRepo.save(connection);
        return followRepo.save(connection);
    }

    @Override
    public void unfollow(int followerId, int followingId) {
        String errorCode = "FollowServiceImpl:unfollow()";
        User followerUser=userRepo.findById(followerId).orElseThrow(()->new ResourceNotFoundException("User","Id",followerId,errorCode));
        User followingUser=userRepo.findById(followingId).orElseThrow(()->new ResourceNotFoundException("User","Id",followingId,errorCode));

        Follow existingFollow = followRepo.findFollowConnection(followerUser,followingUser).orElseThrow(()->
                new ResourceNotFoundException("Follow","FollowingId",followingId,errorCode));
        followRepo.delete(existingFollow);
    }

    @Override
    public FollowerFollowingDetails getDetailsOfFollowersAndFollowing(int userId) {
        String errorCode = "FollowServiceImpl:getDetailsOfFollowersAndFollowing()";
        User user=userRepo.findById(userId).orElseThrow(
                ()->new ResourceNotFoundException("User","Id",userId,errorCode));
        List<User> followerList = followRepo.followerList(user);
        ModelMapper modelMapper = new ModelMapper();
        List<UserDTO> followerListDto = followerList.stream().map(user1 -> modelMapper.map(user1,UserDTO.class))
                .collect(Collectors.toList());

        List<User> followingList = followRepo.followingList(user);
        ModelMapper modelMapper1 = new ModelMapper();
        List<UserDTO> followingListDto = followingList.stream().map(user1 -> modelMapper1.map(user1,UserDTO.class))
                .collect(Collectors.toList());

        int followerCount = followRepo.followerCount(user);
        int followingCount = followRepo.followingCount(user);

        FollowerFollowingDetails currentFollowerFollowingDetails = new FollowerFollowingDetails();
        currentFollowerFollowingDetails.setFollowerList(followerListDto);
        currentFollowerFollowingDetails.setFollowingList(followingListDto);
        currentFollowerFollowingDetails.setFollowerNumber(followerCount);
        currentFollowerFollowingDetails.setFollowingNumber(followingCount);
        return currentFollowerFollowingDetails;
    }
}
