package com.athul.memegramspring.service;

import com.athul.memegramspring.entity.Follow;
import com.athul.memegramspring.utils.FollowerFollowingDetails;

import java.util.List;

public interface FollowService {

    Follow follow(int followerId,int followingId);
    void unfollow(int followerId,int followingId);

    FollowerFollowingDetails getDetailsOfFollowersAndFollowing(int userId);

}
