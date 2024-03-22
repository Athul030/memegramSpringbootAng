package com.athul.memegramspring.service;

import com.athul.memegramspring.dto.NotificationsDTO;

import java.util.List;

public interface NotificationService {

    NotificationsDTO saveNotification(NotificationsDTO notificationsDTO);

    List<NotificationsDTO> getAllNotificationsOfAUser(int userId);

    List<NotificationsDTO> getNotificationsOfAUserMessagesOnly(int userId);

    boolean notificationIsRead(int notId);

    boolean deleteNotification(int notId);

    boolean setChatNotificationStatus(String roomId);
}
