package com.athul.memegramspring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "follows",uniqueConstraints = {@UniqueConstraint(columnNames = {"follower_id","following_id"})})
@NoArgsConstructor
public class Follow {
    @Id
    @Column(name = "follow_Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="follower_id",referencedColumnName = "id")
    private User followerId;

    @ManyToOne
    @JoinColumn(name="following_id",referencedColumnName = "id")
    private User followingId;

    private LocalDateTime followedDate;


}
