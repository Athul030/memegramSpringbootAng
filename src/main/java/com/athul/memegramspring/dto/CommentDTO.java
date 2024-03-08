package com.athul.memegramspring.dto;

import com.athul.memegramspring.entity.Post;
import com.athul.memegramspring.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentDTO {

    private Integer commentId;
    private String commentText;
    private LocalDateTime commentedDate;
    private LocalDateTime editedDate;
    private UserDTO user;

    private PostDTO post;

}
