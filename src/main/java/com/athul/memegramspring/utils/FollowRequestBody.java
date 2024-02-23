package com.athul.memegramspring.utils;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowRequestBody {

    private int followerId;
    private int followingId;
}
