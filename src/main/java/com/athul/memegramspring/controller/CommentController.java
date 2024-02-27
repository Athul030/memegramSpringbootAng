package com.athul.memegramspring.controller;

import com.athul.memegramspring.dto.CommentDTO;
import com.athul.memegramspring.entity.Follow;
import com.athul.memegramspring.exceptions.ApiResponseCustom;
import com.athul.memegramspring.service.CommentService;
import com.athul.memegramspring.utils.AddCommentRequest;
import com.athul.memegramspring.utils.FollowRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    @PostMapping("/addComment")
    ResponseEntity<CommentDTO> addComment(@RequestBody AddCommentRequest addCommentRequest){
        CommentDTO commentDTO = commentService.addComment(addCommentRequest.getPostId(), addCommentRequest.getUserId(), addCommentRequest.getCommentText());
        return new ResponseEntity<>(commentDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteComment")
    ApiResponseCustom follow(@RequestBody CommentDTO commentDTO, Authentication authentication){

        commentService.deleteComment(commentDTO.getCommentId(),authentication.getName());
        return new ApiResponseCustom("Comment is successfully deleted",HttpStatus.OK);
    }

    @GetMapping("/getAllComments/{postId}")
    ResponseEntity<List<CommentDTO>> getAllTheComments(@PathVariable("postId") int postId){
        List<CommentDTO> commentDTOS = commentService.getAllComments(postId);
        return new ResponseEntity<>(commentDTOS,HttpStatus.OK);
    }
}
