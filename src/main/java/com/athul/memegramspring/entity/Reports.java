package com.athul.memegramspring.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints= {@UniqueConstraint(columnNames = {"reported_user_id","reporting_user_id","reported_post_post_id"})})
public class Reports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User reportedUser;

    @ManyToOne
    private User reportingUser;

    private String reportingReason;

    @ManyToOne
    private Post reportedPost;

}
