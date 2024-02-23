package com.athul.memegramspring.service.impl;

import com.athul.memegramspring.dto.CommentDTO;
import com.athul.memegramspring.dto.PostDTO;
import com.athul.memegramspring.dto.UserDTO;
import com.athul.memegramspring.entity.Comment;
import com.athul.memegramspring.entity.Post;
import com.athul.memegramspring.entity.User;
import com.athul.memegramspring.exceptions.ResourceNotFoundException;
import com.athul.memegramspring.repository.CommentRepo;
import com.athul.memegramspring.repository.PostRepo;
import com.athul.memegramspring.repository.UserRepo;
import com.athul.memegramspring.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepo commentRepo;
    private final UserRepo userRepo;
    private final PostRepo postRepo;


    @Override
    public CommentDTO addComment(int postId, int userId, String commentText) {


        String errorCode = "CommentServiceImpl:addComment()";

        Comment newComment = new Comment();
        newComment.setCommentedDate(LocalDateTime.now());
        newComment.setCommentText(commentText);

        User commentedUser=userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","Id",userId,errorCode));
        Post commentedPost = postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","Post id",postId,errorCode));
        newComment.setPost(commentedPost);
        newComment.setUser(commentedUser);
        Comment addedComment = commentRepo.save(newComment);

        ModelMapper modelMapper = new ModelMapper();
        CommentDTO addedCommentDto = modelMapper.map(addedComment, CommentDTO.class);
        addedCommentDto.setUser(modelMapper.map(commentedUser, UserDTO.class));
        addedCommentDto.setPost(modelMapper.map(commentedPost, PostDTO.class));
        return addedCommentDto;
    }

    @Override
    public void deleteComment(int commentId) {
        String errorCode = "CommentServiceImpl:deleteComment()";
        Comment comment = commentRepo.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment","Id",commentId,errorCode));
        commentRepo.delete(comment);

    }
}
