package com.athul.memegramspring.utils;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserContainerForNotif {

    //This class not in user
    private int userId;
    private String email;
    private String fullName;
}
