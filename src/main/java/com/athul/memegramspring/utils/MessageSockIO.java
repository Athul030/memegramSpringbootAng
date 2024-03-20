package com.athul.memegramspring.utils;

import lombok.Data;

@Data
public class MessageSockIO {

    private String senderName;
    private String targetUserName;
    private String message;

}
