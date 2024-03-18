package com.athul.memegramspring.repository;

import com.athul.memegramspring.entity.Notifications;
import com.athul.memegramspring.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepo extends JpaRepository<Notifications, Integer> {

    Optional<List<Notifications>> getAllByNotificationTo(int userId);

    @Query(value = " SELECT * FROM Notifications WHERE notification_to=:userId AND notification_type=:notificationType ",nativeQuery = true)
    Optional<List<Notifications>> fetchMessageNotificationsOfAUser(int userId, NotificationType notificationType);

    //check later for working HQL
//    List<Notifications> getAllByNotificationToAndNotificationTypeIsLikeMESSAGE(int userId, NotificationType notificationType);


}
