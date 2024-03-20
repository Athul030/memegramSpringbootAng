package com.athul.memegramspring.dto;

import com.athul.memegramspring.enums.NotificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NotificationsDTO {

    private Integer notificationId;

    private int count;

    private LocalDateTime notTimeStamp;
    private String chatRoomId;
    private int videoCallRoomId;
    private NotificationType notificationType;

    private int notificationFrom;
    private int notificationFromUserId;
    private String notificationFromEmail;
    private String notificationFromFullName;

    private int notificationTo;
    private int notificationToUserId;
    private String notificationToEmail;
    private String notificationToFullName;

    private boolean read;
}
