package com.athul.memegramspring.service;

import com.athul.memegramspring.dto.PostDTO;
import com.athul.memegramspring.entity.Post;
import com.athul.memegramspring.enums.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface PostService {

    //create
    PostDTO createPost(PostDTO postDTO, Integer userId,Integer categoryId);
    //update
    PostDTO updatePost(PostDTO postDTO,Integer postId);

    //delete
    void deletePost(Integer postId, String loggedInUserName);
    List<PostDTO> getAllPost();
    //get all posts
    Page<PostDTO> getAllPost(Pageable pageable);

    //get a single post
    PostDTO getPostById(Integer postId);

    //get all posts by category
    List<PostDTO> getPostsByCategory(Integer categoryId);

    //get all posts by user
    List<PostDTO> getPostsByUser(Integer userId);

    int numberOfPostByAUser(String username);

    int numberOfPostByOtherUser(Integer userId);

    Map<Date,Integer> getPostsDataAdminDashboard();
}
