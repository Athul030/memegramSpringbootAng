package com.athul.memegramspring.dto;

import lombok.*;

import java.net.http.WebSocket;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignalDataDTO {

    private String usersId;
    private String type;
    private String data;
    private String toUid;

}
