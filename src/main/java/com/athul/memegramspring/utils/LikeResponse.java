package com.athul.memegramspring.utils;

import com.athul.memegramspring.dto.LikeDTO;
import com.athul.memegramspring.entity.Like;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeResponse {

    private int likeCount;
    private LikeDTO likeDTO;


}
