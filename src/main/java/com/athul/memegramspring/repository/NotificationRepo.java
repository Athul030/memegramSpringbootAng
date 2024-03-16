package com.athul.memegramspring.repository;

import com.athul.memegramspring.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepo extends JpaRepository<Notifications, Integer> {

}
