package com.athul.memegramspring.utils;


import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCommentRequest {

    private int postId;
    private int userId;
    private String commentText;
}
