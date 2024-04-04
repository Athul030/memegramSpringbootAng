package com.athul.memegramspring.repository;

import com.athul.memegramspring.entity.Category;
import com.athul.memegramspring.entity.Post;
import com.athul.memegramspring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post,Integer> {

    List<Post> findByUser(User user);
    List<Post> findByCategory(Category category);

    @Query("SELECT COUNT(*) FROM Post p JOIN User u ON p.user.id=u.id WHERE p.user.email=:username AND p.block=false ")
    int countOfPostByUser(String username);

}
