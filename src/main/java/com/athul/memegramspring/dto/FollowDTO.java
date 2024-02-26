package com.athul.memegramspring.dto;

import com.athul.memegramspring.entity.User;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class FollowDTO {

    private int id;

    private UserDTO follower;

    private UserDTO following;

    private LocalDateTime followedDate;
}
