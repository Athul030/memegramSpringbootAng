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
@Table(name = "follows",uniqueConstraints = {@UniqueConstraint(columnNames = {"follower","following"})})
@NoArgsConstructor
public class Follow {
    @Id
    @Column(name = "follow_Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="follower",referencedColumnName = "id")
    private User follower;

    @ManyToOne
    @JoinColumn(name="following",referencedColumnName = "id")
    private User following;

    private LocalDateTime followedDate;


}
