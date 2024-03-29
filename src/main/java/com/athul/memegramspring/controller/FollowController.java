package com.athul.memegramspring.controller;

import com.athul.memegramspring.dto.FollowDTO;
import com.athul.memegramspring.entity.Follow;
import com.athul.memegramspring.entity.User;
import com.athul.memegramspring.repository.UserRepo;
import com.athul.memegramspring.service.FollowService;
import com.athul.memegramspring.service.UserService;
import com.athul.memegramspring.service.impl.FollowServiceImpl;
import com.athul.memegramspring.utils.FollowRequestBody;
import com.athul.memegramspring.utils.FollowerFollowingDetails;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final UserService userService;
    private final UserRepo userRepo;

    @PostMapping("/follow")
    ResponseEntity<Boolean> follow(@RequestBody FollowRequestBody followRequestBody){
        Follow newFollow = followService.follow(followRequestBody.getFollowerId(), followRequestBody.getFollowingId());
        if(newFollow!=null) return new ResponseEntity<>(true, HttpStatus.CREATED);
        else return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/unfollow")
    ResponseEntity<Boolean> unfollow(@RequestBody FollowRequestBody followRequestBody){
        followService.unfollow(followRequestBody.getFollowerId(), followRequestBody.getFollowingId());
        return new ResponseEntity<>(true,HttpStatus.OK);
    }

        @GetMapping("/followerAndFollowingDetails")
    ResponseEntity<FollowerFollowingDetails> followersAndFollowingDetails(Authentication authentication){

        FollowerFollowingDetails followerFollowingDetails = followService.getDetailsOfFollowersAndFollowing(userService.findUserIdFromUsername(authentication.getName()));
        return new ResponseEntity<>(followerFollowingDetails,HttpStatus.OK);
    }

    //for other profile visit user
    @GetMapping("/followerAndFollowingDetails/{userId}")
    ResponseEntity<FollowerFollowingDetails> followersAndFollowingDetailsForOtherUser(@PathVariable Integer userId){

        FollowerFollowingDetails followerFollowingDetails = followService.getDetailsOfFollowersAndFollowing(userId);
        return new ResponseEntity<>(followerFollowingDetails,HttpStatus.OK);
    }
}
