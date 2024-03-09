package com.athul.memegramspring.service.impl;

import com.athul.memegramspring.dto.LikeDTO;
import com.athul.memegramspring.dto.PostDTO;
import com.athul.memegramspring.dto.UserDTO;
import com.athul.memegramspring.entity.Like;
import com.athul.memegramspring.entity.Post;
import com.athul.memegramspring.entity.User;
import com.athul.memegramspring.exceptions.ResourceNotFoundException;
import com.athul.memegramspring.repository.LikeRepo;
import com.athul.memegramspring.repository.PostRepo;
import com.athul.memegramspring.repository.UserRepo;
import com.athul.memegramspring.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final UserRepo userRepo;
    private final PostRepo postRepo;
    private final LikeRepo likeRepo;
    private final ModelMapper modelMapper;

    @Override
    public LikeDTO likeContent(int userIdOfPersonLiking, int postId) {

        String errorCode = "LikeServiceImpl:likeContent()";
        User likedUser=userRepo.findById(userIdOfPersonLiking).orElseThrow(()->new ResourceNotFoundException("User","Id",userIdOfPersonLiking,errorCode));
        Post likedPost = postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","Post id",postId,errorCode));
        Like currentLike = new Like();
        currentLike.setLikedDate(LocalDateTime.now());
        currentLike.setPost(likedPost);
        currentLike.setUser(likedUser);
        ModelMapper modelMapper1 = new ModelMapper();
        LikeDTO likeDTO = modelMapper1.map(likeRepo.save(currentLike),LikeDTO.class);
        likeDTO.setUser(modelMapper1.map(likedUser, UserDTO.class));
        likeDTO.setPost(modelMapper1.map(likedPost, PostDTO.class));
        return likeDTO;
    }

    @Override
    public LikeDTO unLikeContent(int userIdOfPersonUnliking, int postId) {

        String errorCode = "LikeServiceImpl:unLikeContent()";
        User unLikedUser=userRepo.findById(userIdOfPersonUnliking).orElseThrow(()->new ResourceNotFoundException("User","Id",userIdOfPersonUnliking,errorCode));
        Post unLikedPost =postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","Post id",postId,errorCode));
        Like currentLike = likeRepo.findByUserAndPost(unLikedUser,unLikedPost).orElseThrow(()-> new ResourceNotFoundException("Like","Unli" +
                "ked User / Unliked Post",unLikedPost.getPostId(),errorCode));
        likeRepo.delete(currentLike);
        ModelMapper modelMapper1 = new ModelMapper();
        return modelMapper1.map(currentLike, LikeDTO.class);
    }

    public int noOfLikes(int postId){
        String errorCode = "LikeServiceImpl:noOfLikes()";
        Post likedPost = postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","Post id",postId,errorCode));
        List<Like> likeList = likedPost.getLikes();
        return (likeList != null) ? likeList.size() : 0;
    }

    @Override
    public LikeDTO getDetailsOfLike(int userIdOfPerson, int postId) {
        String errorCode = "LikeServiceImpl:getDetailsOfLike()";

        User currentUser=userRepo.findById(userIdOfPerson).orElseThrow(()->new ResourceNotFoundException("User","Id",userIdOfPerson,errorCode));
        Post post =postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","Post id",postId,errorCode));
        Like currentLike = likeRepo.findByUserAndPost(currentUser,post).orElseThrow(()-> new ResourceNotFoundException("Like","Unli" +
                "ked User / Unliked Post",post.getPostId(),errorCode));
        ModelMapper modelMapper2 = new ModelMapper();
        LikeDTO likeDTO = modelMapper2.map(currentLike,LikeDTO.class);
        likeDTO.setUser(modelMapper2.map(currentUser, UserDTO.class));
        likeDTO.setPost(modelMapper2.map(post, PostDTO.class));
        return likeDTO;
    }
}
