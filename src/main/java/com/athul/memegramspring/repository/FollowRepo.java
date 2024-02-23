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

    @Query(" SELECT f FROM Follow f WHERE f.followingId=:following_id AND f.followerId=:follower_id")
    Optional<Follow> findFollowConnection(User follower_id, User following_id);

    @Query(" SELECT f.followerId FROM Follow f WHERE f.followingId=:mainUser")
    List<User> followerList(User mainUser);
    @Query(" SELECT COUNT(f.followerId) FROM Follow f WHERE f.followingId=:mainUser")
    int followerCount(User mainUser);

    @Query(" SELECT f.followingId FROM Follow f WHERE f.followerId=:mainUser")
    List<User> followingList(User mainUser);
    @Query(" SELECT COUNT(f.followingId) FROM Follow f WHERE f.followerId=:mainUser")
    int followingCount(User mainUser);
}
