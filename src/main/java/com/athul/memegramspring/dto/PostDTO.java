package com.athul.memegramspring.dto;

import com.athul.memegramspring.entity.Category;
import com.athul.memegramspring.entity.Comment;
import com.athul.memegramspring.entity.Like;
import com.athul.memegramspring.entity.User;
import com.athul.memegramspring.enums.PostType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties({"comments"})  // Add this annotation to ignore the "comments" property during serialization

@NoArgsConstructor
public class PostDTO {

    private Integer postId;

    @NotBlank
    private String title;
    @NotBlank
    private String content;

    private String imageName;


    private Date addedDate;

    private CategoryDTO category;

    private UserDTO user;

    private String imageUrl;

    private boolean isDeleted;

    private List<LikeDTO> likes;

    private List<CommentDTO> comments;

    private CommentDTO lastComment;

}
