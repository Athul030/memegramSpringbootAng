package com.athul.memegramspring.entity;

import com.athul.memegramspring.enums.PostType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="post")
@Getter
@Setter
@Data
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;

    private String title;

    private String content;

    private String imageName;

    private Date addedDate;

    @OneToMany(mappedBy = "post",fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Like> likes;

    @OneToMany(mappedBy = "post",fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    private String imageUrl;

    @Column(name = "reported_count", nullable = false, columnDefinition = "integer default 0")
    private int reportedCount;

    private boolean block;


}
