package com.athul.memegramspring.controller;


import com.athul.memegramspring.dto.LikeDTO;
import com.athul.memegramspring.dto.PostDTO;
import com.athul.memegramspring.entity.Follow;
import com.athul.memegramspring.entity.Like;
import com.athul.memegramspring.repository.PostRepo;
import com.athul.memegramspring.service.LikeService;
import com.athul.memegramspring.utils.FollowRequestBody;
import com.athul.memegramspring.utils.LikeRequestBody;
import com.athul.memegramspring.utils.LikeResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    private final ModelMapper modelMapper;
    private final PostRepo postRepo;

    @PostMapping("/like")
    ResponseEntity<LikeResponse> likeAPost(@RequestBody LikeRequestBody likeRequestBody){

        LikeDTO newLike = likeService.likeContent(likeRequestBody.getUserIdOfPersonLiking(), likeRequestBody.getPostId());
        int likeCount = likeService.noOfLikes(likeRequestBody.getPostId());
        LikeResponse likeResponse = new LikeResponse(likeCount,newLike);
        return new ResponseEntity<>(likeResponse,HttpStatus.OK);
    }


    @PostMapping("/unlike")
    ResponseEntity<LikeResponse> unlikeAPost(@RequestBody LikeRequestBody likeRequestBody){
        LikeDTO deletedLike = likeService.unLikeContent(likeRequestBody.getUserIdOfPersonLiking(), likeRequestBody.getPostId());
        int likeCount = likeService.noOfLikes(likeRequestBody.getPostId());

        LikeResponse likeResponse = new LikeResponse(likeCount,deletedLike);
        return new ResponseEntity<>(likeResponse,HttpStatus.OK);
    }
}
