package com.athul.memegramspring.dto;

import com.athul.memegramspring.entity.Post;
import com.athul.memegramspring.entity.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CommentDTO {

    private Integer commentId;
    private String commentText;
    private LocalDate commentedDate;

    private UserDTO user;

    private PostDTO post;

}
