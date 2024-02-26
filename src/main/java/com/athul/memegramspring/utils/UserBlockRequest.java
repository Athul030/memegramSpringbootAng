package com.athul.memegramspring.utils;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBlockRequest {

    private int blockingUserId;
    private int blockedUserId;

}
