package com.athul.memegramspring.entity;

import com.athul.memegramspring.enums.NotificationType;
import com.athul.memegramspring.utils.UserContainerForNotif;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationId;

    private int count;

    private NotificationType notificationType;
    private String chatRoomId;


    private int notificationFrom;
    private int notificationFromUserId;
    private String notificationFromEmail;
    private String notificationFromFullName;



    private int notificationTo;
    private int notificationToUserId;
    private String notificationToEmail;
    private String notificationToFullName;


    private boolean read;

    private LocalDateTime notTimeStamp;
    public Notifications(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void increment(){
        this.count++;
    }
}
