package com.athul.memegramspring.service;


import com.athul.memegramspring.dto.CommentDTO;
import com.athul.memegramspring.entity.Comment;

import java.util.List;

public interface CommentService {

    CommentDTO addComment(int postId, int userId, String commentText);
    void deleteComment(int commentId, String loggedInUserName);

    List<CommentDTO> getAllComments(int postId);
}
