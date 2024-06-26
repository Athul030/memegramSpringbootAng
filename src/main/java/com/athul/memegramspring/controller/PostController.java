package com.athul.memegramspring.controller;

import com.athul.memegramspring.dto.PostDTO;
import com.athul.memegramspring.entity.Post;
import com.athul.memegramspring.exceptions.ApiResponseCustom;
import com.athul.memegramspring.service.FileService;
import com.athul.memegramspring.service.PostService;
import com.athul.memegramspring.service.S3FileUploadService;
import com.athul.memegramspring.service.UserService;
import com.athul.memegramspring.utils.FollowerFollowingDetails;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
@CrossOrigin("*")
public class  PostController {

    private final PostService postService;

    private final FileService fileService;

    private final UserService userService;

    private final S3FileUploadService s3FileUploadService;

    @Value("${project.image}")
    private String path;

    @Value("${project.imageTarget")
    private String altPath;

    @Value("${baseUrl}")
    private String url;
    @Value("${baseUrlForNonPageablePosts}")
    private String urlForPosts;

    //create a post
    @PostMapping("/user/{userId}/category/{categoryId}/posts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404",description = "Category/User not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<PostDTO> createPost(@RequestPart PostDTO postDTO,
                                                @PathVariable Integer userId,
                                                @PathVariable Integer categoryId,
                                                @RequestPart("file")MultipartFile file) throws IOException {
//        //image using local storage
//        String fileName = fileService.uploadImage(path, file);
//        postDTO.setImageName(fileName);

        //image using cloud storage
        String fileName = s3FileUploadService.uploadFileToS3(file).get("fileUrl");
        postDTO.setImageUrl(fileName);
        PostDTO createdPost = postService.createPost(postDTO,userId,categoryId);
        return new ResponseEntity<PostDTO>(createdPost, HttpStatus.CREATED);
    }

    //create a post from token without userId
    @PostMapping(value = "/createPost")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<PostDTO> createPost(@RequestPart PostDTO postDTO,
                                              @RequestParam String categoryId,
                                              @RequestPart("file") MultipartFile file,
                                              @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        Integer parsedCategoryId = Integer.parseInt(categoryId);
        Integer userId = userService.getUserByUsername(userDetails.getUsername()).getId();
//        //image using local storage
//        String fileName = fileService.uploadImage(path, file);
//        postDTO.setImageName(fileName);

        //image using cloud storage
        String fileName = s3FileUploadService.uploadFileToS3(file).get("fileUrl");
        postDTO.setImageUrl(fileName);

        PostDTO createdPost = postService.createPost(postDTO,userId,parsedCategoryId);
        return new ResponseEntity<PostDTO>(createdPost, HttpStatus.CREATED);
    }


    //get posts by user from TOken
    @GetMapping("/user/posts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404",description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<List<PostDTO>> getPostsByUser(Authentication authentication){
        Integer userId = userService.findUserIdFromUsername(authentication.getName());
        List<PostDTO> postsByUser = postService.getPostsByUser(userId);
        return new ResponseEntity<List<PostDTO>>(postsByUser,HttpStatus.OK);

    }

    //get posts of other user
    @GetMapping("/user/posts/{userId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404",description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<List<PostDTO>> getPostsByOtherUser(@PathVariable Integer userId){
        List<PostDTO> postsByUser = postService.getPostsByUser(userId);
        return new ResponseEntity<List<PostDTO>>(postsByUser,HttpStatus.OK);

    }


    //get by category
    @GetMapping("/category/{categoryId}/posts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404",description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<List<PostDTO>> getPostsByCategory(@PathVariable Integer categoryId){

        List<PostDTO> postsByCategory = postService.getPostsByCategory(categoryId);
        return new ResponseEntity<List<PostDTO>>(postsByCategory,HttpStatus.OK);

    }

    //get all posts pageable
    @GetMapping("/posts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<Page<PostDTO>> getAllPostWIthPageable( Pageable pageable){

        Page<PostDTO> page = postService.getAllPost(pageable);
//        String baseUrl = url;
//        page.getContent().forEach(postDTO -> {
//            String imageUrl = baseUrl+"images/"+postDTO.getImageName();
//            postDTO.setImageUrl(imageUrl);
//        });
        return new ResponseEntity<>(page,HttpStatus.OK);
    }

    //get all posts without pageable
    @GetMapping("/getAllPosts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<List<PostDTO>> getAllPost(){

        List<PostDTO> page1 = postService.getAllPost();
//        String baseUrl = urlForPosts;
//        page1.stream().forEach(postDTO -> {
//            String imageUrl = baseUrl+"images/"+postDTO.getImageName();
//
//            postDTO.setImageUrl(imageUrl);
//        });
        return new ResponseEntity<>(page1,HttpStatus.OK);
    }

    //get post details by id
    @GetMapping("/posts/{postId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404",description = "Post not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<PostDTO> getPostById(@PathVariable Integer postId){
        PostDTO postDTO = postService.getPostById(postId);
        return new ResponseEntity<PostDTO>(postDTO,HttpStatus.OK);
    }

    //delete post
    @DeleteMapping("/postsDelete/{postId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404",description = "Post not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ApiResponseCustom deletePost(@PathVariable  int postId, Authentication authentication){

        postService.deletePost(postId,authentication.getName());
        return new ApiResponseCustom("Post is successfully deleted",HttpStatus.OK);

    }



    @PutMapping("/posts/{postId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404",description = "Post not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
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

//    //post multiple image upload
//    @PostMapping("/post/image/upload/{postId}")
//    public ResponseEntity<PostDTO> uploadPostImageMultiple(@RequestParam("image")MultipartFile[] file,
//                                                   @PathVariable Integer postId) throws IOException {
//        String[] fileName = fileService.uploadMultipleImage(path, file);
//        PostDTO postDto = postService.getPostById(postId);
//        postDto.setImageName(fileName);
//        PostDTO postDTO = postService.updatePost(postDto, postId);
//        return new ResponseEntity<PostDTO>(postDTO,HttpStatus.OK);
//
//    }

    @GetMapping(value="/post/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response) throws IOException {
        InputStream resource = fileService.getResource(path,imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

    @GetMapping("/postCountOfUser")
    ResponseEntity<Integer> postCountOfUser(Authentication authentication){
        String username = authentication.getName();
        int count = postService.numberOfPostByAUser(username);
        return new ResponseEntity<>(count,HttpStatus.OK);
    }

    @GetMapping("/postCountOfUser/{userId}")
    ResponseEntity<Integer> postCountOfUser(@PathVariable Integer userId){
        int count = postService.numberOfPostByOtherUser(userId);
        return new ResponseEntity<>(count,HttpStatus.OK);
    }


}
