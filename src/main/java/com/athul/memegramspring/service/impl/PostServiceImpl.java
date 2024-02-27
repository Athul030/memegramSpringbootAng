package com.athul.memegramspring.service.impl;

import com.athul.memegramspring.dto.*;
import com.athul.memegramspring.entity.*;
import com.athul.memegramspring.enums.PostType;
import com.athul.memegramspring.exceptions.ResourceNotFoundException;
import com.athul.memegramspring.repository.CategoryRepo;
import com.athul.memegramspring.repository.LikeRepo;
import com.athul.memegramspring.repository.PostRepo;
import com.athul.memegramspring.repository.UserRepo;
import com.athul.memegramspring.service.PostService;
import com.athul.memegramspring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepo postRepo;
    private final UserRepo userRepo;
    private final UserService userService;
    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;
    private final LikeRepo likeRepo;



    @Override
    public PostDTO createPost(PostDTO postDTO, Integer userId,Integer categoryId) {
        String errorCodeFindUser = "PostServiceImpl:createPost()::findUser";
        String errorCodeFindCategory = "PostServiceImpl:createPost()::findUser";

        User user= userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","User id",userId,errorCodeFindUser));
        Category category= categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","Category id",categoryId, errorCodeFindCategory));

        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        if(postDTO.getImageName()==null) {
            post.setImageName("default.png");
        }else{
            post.setImageName(postDTO.getImageName());
        }
        post.setAddedDate(new Date());
        post.setUser(user);
        post.setCategory(category);
        Post savedPost = postRepo.save(post);

        PostDTO returnedPostDto = new PostDTO();
        returnedPostDto.setPostId(savedPost.getPostId());
        returnedPostDto.setTitle(savedPost.getTitle());
        returnedPostDto.setContent(savedPost.getContent());
        returnedPostDto.setImageName(savedPost.getImageName());
        returnedPostDto.setAddedDate(savedPost.getAddedDate());
        returnedPostDto.setUser(userToDTO(savedPost.getUser()));

        returnedPostDto.setCategory(catToDTO(savedPost.getCategory()));
        return returnedPostDto;
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO,Integer postId) {
        String errorCode = "PostServiceImpl:updatePost()";
        Post post = postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","Post id",postId,errorCode));
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        if(postDTO.getImageName()==null) {
            post.setImageName("updateDefault.png");
        }else{
            post.setImageName(postDTO.getImageName());
        }
        post.setAddedDate(new Date());
        Post savedPost = postRepo.save(post);

        PostDTO returnedPostDto = new PostDTO();
        returnedPostDto.setPostId(savedPost.getPostId());
        returnedPostDto.setTitle(savedPost.getTitle());
        returnedPostDto.setContent(savedPost.getContent());
        returnedPostDto.setImageName(savedPost.getImageName());
        returnedPostDto.setAddedDate(savedPost.getAddedDate());
        returnedPostDto.setUser(userToDTO(savedPost.getUser()));
        returnedPostDto.setCategory(catToDTO(savedPost.getCategory()));
        return returnedPostDto;

    }

    @Override
    public void deletePost(Integer postId) {
        String errorCode = "PostServiceImpl:deletePost()";
        Post post = postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","Post id",postId,errorCode));
        postRepo.delete(post);

    }

    @Override
    public Page<PostDTO> getAllPost(Pageable pageable) {
        Page<Post> page = postRepo.findAll(pageable);
        List<PostDTO> postDTOS = page.getContent().stream().map(post -> postToDTO(post)).collect(Collectors.toList());
        return new PageImpl<>(postDTOS,pageable,page.getTotalElements());
    }

    @Override
    public List<PostDTO> getAllPost() {

        List<Post> allPosts = postRepo.findAll();
        List<PostDTO> postDtosAll = allPosts.stream().map((post -> postToDTO(post))).collect(Collectors.toList());
        return postDtosAll;
    }

    @Override
    public PostDTO getPostById(Integer postId) {
        String errorCode = "PostServiceImpl:getPostById()";
        Post post = postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","Post id",postId,errorCode));
        return postToDTO(post);
    }

    @Override
    public List<PostDTO> getPostsByCategory(Integer categoryId) {
        String errorCode = "PostServiceImpl:getPostsByCategory()";
        Category category= categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","Category id",categoryId,errorCode));
        List<Post> posts = postRepo.findByCategory(category);

        List<PostDTO> postsDtosCat = posts.stream().map((post -> postToDTO(post))).collect(Collectors.toList());
        return  postsDtosCat;
    }

    @Override
    public List<PostDTO> getPostsByUser(Integer userId) {
        String errorCode = "PostServiceImpl:getPostsByUser()";
        User user= userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","User id",userId,errorCode));
        List<Post> posts = postRepo.findByUser(user);
        ModelMapper modelMapper2 = new ModelMapper();

        List<PostDTO> postsDtosUser = posts.stream().map(post -> {
            PostDTO postDTO = modelMapper2.map(post, PostDTO.class);
            List<LikeDTO> likeDTOS = post.getLikes().stream().map(like -> modelMapper2.map(like, LikeDTO.class)).collect(Collectors.toList());
            postDTO.setLikes(likeDTOS);
            return postDTO;
        }).collect(Collectors.toList());

     String baseUrl = "http://localhost:8080/";
        postsDtosUser.stream().forEach(postDTO -> {

            String imageUrl = baseUrl+"images/"+postDTO.getImageName();
            postDTO.setImageUrl(imageUrl);
        });

        return postsDtosUser;

    }

    @Override
    public int numberOfPostByAUser(String username) {
        return postRepo.countOfPostByUser(username);
    }

    private UserDTO userToDTO(User user){
        UserDTO userDTO=new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFullName(user.getFullName());
        userDTO.setUserHandle(user.getUserHandle());
        userDTO.setEmail(user.getEmail());
        userDTO.setBio(user.getBio());
        userDTO.setPassword(user.getPassword());
        Set<RoleDto> roleDtos = user.getRoles().stream().map(role -> modelMapper.map(role, RoleDto.class)).collect(Collectors.toSet());
        userDTO.setRoles(roleDtos);
        userDTO.setProfilePicUrl(user.getProfilePicUrl());
        return userDTO;

    }

    private CategoryDTO catToDTO(Category category){
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(category.getCategoryId());
        categoryDTO.setCategoryTitle(category.getCategoryTitle());
        categoryDTO.setCategoryDescription(category.getCategoryDescription());
        return categoryDTO;

    }

    private PostDTO postToDTO(Post post){
        PostDTO postDTO = new PostDTO();
        postDTO.setPostId(post.getPostId());
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        postDTO.setImageName(post.getImageName());
        postDTO.setAddedDate(post.getAddedDate());
        postDTO.setUser(userToDTO(post.getUser()));
        postDTO.setCategory(catToDTO(post.getCategory()));
        ModelMapper modelMapper1 = new ModelMapper();
        List<LikeDTO> likeDTOS = post.getLikes().stream().map(x->{

            LikeDTO likeDTO = new LikeDTO();
            likeDTO.setPostDTO(modelMapper1.map(x.getPost(), PostDTO.class));
            likeDTO.setUserDTO(modelMapper1.map(x.getUser(), UserDTO.class));
            likeDTO.setLikeId(x.getLikeId());
            likeDTO.setLikedDate(x.getLikedDate());
            return likeDTO;

        }).collect(Collectors.toList());
        postDTO.setLikes(likeDTOS);

        postDTO.setComments(post.getComments().stream().map(x->modelMapper1.map(x, CommentDTO.class)).collect(Collectors.toList()));
        CommentDTO lastComment = post.getComments().stream().max(Comparator.comparing(Comment::getCommentedDate))
                .map(x->modelMapper1.map(x, CommentDTO.class)).orElse(null);
        if(lastComment!=null) {
            postDTO.setLastComment(lastComment);
        }
        return postDTO;

    }




}
