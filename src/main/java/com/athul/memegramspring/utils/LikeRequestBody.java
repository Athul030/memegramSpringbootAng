package com.athul.memegramspring.utils;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeRequestBody {
        private int userIdOfPersonLiking;
        private int postId;

}
