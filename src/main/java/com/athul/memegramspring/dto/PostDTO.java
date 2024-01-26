package com.athul.memegramspring.dto;

import com.athul.memegramspring.entity.Category;
import com.athul.memegramspring.entity.User;
import com.athul.memegramspring.enums.PostType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
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

}
