package com.athul.memegramspring.utils;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtherUserRequest {
    private String roomId;
    private int currentUserId;
}
