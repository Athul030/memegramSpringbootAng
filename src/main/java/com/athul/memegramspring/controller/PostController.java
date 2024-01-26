package com.athul.memegramspring.controller;

import com.athul.memegramspring.dto.PostDTO;
import com.athul.memegramspring.entity.Post;
import com.athul.memegramspring.exceptions.ApiResponse;
import com.athul.memegramspring.service.FileService;
import com.athul.memegramspring.service.PostService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private final FileService fileService;

    @Value("${project.image}")
    private String path;

    //create a post
    @PostMapping("/user/{userId}/category/{categoryId}/posts")
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO,
                                                @PathVariable Integer userId,
                                              @PathVariable Integer categoryId){

        PostDTO createdPost = postService.createPost(postDTO,userId,categoryId);
        return new ResponseEntity<PostDTO>(createdPost, HttpStatus.CREATED);
    }

    //get by user
    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<List<PostDTO>> getPostsByUser(@PathVariable Integer userId){

        List<PostDTO> postsByUser = postService.getPostsByUser(userId);
        return new ResponseEntity<List<PostDTO>>(postsByUser,HttpStatus.OK);

    }

    //get by category
    @GetMapping("/category/{categoryId}/posts")
    public ResponseEntity<List<PostDTO>> getPostsByCategory(@PathVariable Integer categoryId){

        List<PostDTO> postsByCategory = postService.getPostsByCategory(categoryId);
        return new ResponseEntity<List<PostDTO>>(postsByCategory,HttpStatus.OK);

    }

    //get all posts
    @GetMapping("/posts")
    public ResponseEntity<List<PostDTO>> getAllPost(){
        List<PostDTO> allPost = postService.getAllPost();
        return new ResponseEntity<List<PostDTO>>(allPost,HttpStatus.OK);
    }

    //get post details by id
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Integer postId){
        PostDTO postDTO = postService.getPostById(postId);
        return new ResponseEntity<PostDTO>(postDTO,HttpStatus.OK);
    }

    //delete post
    @DeleteMapping("/posts/{postId}")
    public ApiResponse deletePost(@PathVariable  Integer postId){

        postService.deletePost(postId);
        return new ApiResponse("Post is successfully deleted",true);

    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostDTO> updatePost (@RequestBody PostDTO postDTO,
                                               @PathVariable Integer postId){
        PostDTO updatedPost = postService.updatePost(postDTO, postId);
        return new ResponseEntity<>(updatedPost,HttpStatus.OK);
    }


    //post image upload
    @PostMapping("/post/image/upload/{postId}")
    public ResponseEntity<PostDTO> uploadPostImage(@RequestParam("image")MultipartFile image,
                                                         @PathVariable Integer postId) throws IOException {
        String fileName = fileService.uploadImage(path, image);
        PostDTO postDto = postService.getPostById(postId);
        postDto.setImageName(fileName);
        PostDTO postDTO = postService.updatePost(postDto, postId);
        return new ResponseEntity<PostDTO>(postDTO,HttpStatus.OK);

    }

    @GetMapping(value="/post/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response) throws IOException {
        InputStream resource = fileService.getResource(path,imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }
}
