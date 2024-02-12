package com.athul.memegramspring.service.impl;

import com.athul.memegramspring.dto.CategoryDTO;
import com.athul.memegramspring.dto.PostDTO;
import com.athul.memegramspring.dto.RoleDto;
import com.athul.memegramspring.dto.UserDTO;
import com.athul.memegramspring.entity.Category;
import com.athul.memegramspring.entity.Post;
import com.athul.memegramspring.entity.User;
import com.athul.memegramspring.enums.PostType;
import com.athul.memegramspring.exceptions.ResourceNotFoundException;
import com.athul.memegramspring.repository.CategoryRepo;
import com.athul.memegramspring.repository.PostRepo;
import com.athul.memegramspring.repository.UserRepo;
import com.athul.memegramspring.service.PostService;
import com.athul.memegramspring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

        List<PostDTO> postsDtosUser = posts.stream().map((post -> postToDTO(post))).collect(Collectors.toList());
        return postsDtosUser;

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
        return postDTO;

    }


}
