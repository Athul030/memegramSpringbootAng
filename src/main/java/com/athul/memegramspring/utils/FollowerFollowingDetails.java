package com.athul.memegramspring.utils;

import com.athul.memegramspring.dto.UserDTO;
import com.athul.memegramspring.entity.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowerFollowingDetails {

    private int followerNumber;
    private int followingNumber;

    private List<UserDTO> followerList;
    private List<UserDTO> followingList;

}
