package com.athul.memegramspring.service;

import com.athul.memegramspring.dto.PostDTO;
import com.athul.memegramspring.entity.Post;

import java.util.List;

public interface PostService {

    //create
    PostDTO createPost(PostDTO postDTO, Integer userId,Integer categoryId);
    //update
    PostDTO updatePost(PostDTO postDTO,Integer postId);

    //delete
    void deletePost(Integer postId);

    //get all posts
    List<PostDTO> getAllPost();

    //get a single post
    PostDTO getPostById(Integer postId);

    //get all posts by category
    List<PostDTO> getPostsByCategory(Integer categoryId);

    //get all posts by user
    List<PostDTO> getPostsByUser(Integer userId);

}
