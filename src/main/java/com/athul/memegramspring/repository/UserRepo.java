package com.athul.memegramspring.repository;

import com.athul.memegramspring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {


    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u  WHERE u.email= :email")
    User findByEmails(String email);
}
