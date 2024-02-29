package com.athul.memegramspring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;
    private String commentText;
    private LocalDateTime commentedDate;

    @ManyToOne
    @JoinColumn(name="id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

}
