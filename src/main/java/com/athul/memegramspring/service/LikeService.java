package com.athul.memegramspring.service;

import com.athul.memegramspring.dto.LikeDTO;
import com.athul.memegramspring.entity.Follow;
import com.athul.memegramspring.entity.Like;

public interface LikeService {

    LikeDTO likeContent(int userIdOfPersonLiking, int postId);
    LikeDTO unLikeContent(int userIdOfPersonUnliking,int postId);

    int noOfLikes(int postId);

    LikeDTO getDetailsOfLike(int userIdOfPerson, int postId);
}
