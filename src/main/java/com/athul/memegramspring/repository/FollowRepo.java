package com.athul.memegramspring.repository;

import com.athul.memegramspring.entity.Follow;
import com.athul.memegramspring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepo extends JpaRepository<Follow,Integer> {

    @Query(" SELECT f FROM Follow f WHERE f.following=:following_id AND f.follower=:follower_id")
    Optional<Follow> findFollowConnection(User follower_id, User following_id);

    @Query(" SELECT f.follower FROM Follow f WHERE f.following=:mainUser")
    List<User> followerList(User mainUser);
    @Query(" SELECT COUNT(f.follower) FROM Follow f WHERE f.following=:mainUser")
    int followerCount(User mainUser);

    @Query(" SELECT f.following FROM Follow f WHERE f.follower=:mainUser")
    List<User> followingList(User mainUser);
    @Query(" SELECT COUNT(f.following) FROM Follow f WHERE f.follower=:mainUser")
    int followingCount(User mainUser);



}
