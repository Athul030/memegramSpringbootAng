package com.athul.memegramspring.dto;

import com.athul.memegramspring.entity.Post;
import com.athul.memegramspring.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class LikeDTO {

    private Integer likeId;

    private PostDTO postDTO;

    private UserDTO userDTO;

    private LocalDateTime likedDate;

}
