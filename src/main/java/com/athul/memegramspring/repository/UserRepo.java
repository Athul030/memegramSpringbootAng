package com.athul.memegramspring.repository;

import com.athul.memegramspring.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {


    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u  WHERE u.email= :email")
    User findByEmails(String email);

    //override
    @Query(nativeQuery = true,value = "SELECT * FROM users u JOIN user_role r ON u.id = r.users WHERE r.role=2 ")
    List<User> findAll();


    //override
    @Query(nativeQuery = true,value = "SELECT * FROM users u JOIN user_role r ON u.id = r.users WHERE r.role=2 ")
    Page<User> findAllPageable(Pageable pageable);
}
