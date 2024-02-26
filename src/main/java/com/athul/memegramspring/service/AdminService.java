package com.athul.memegramspring.service;


import org.springframework.http.ResponseEntity;

public interface AdminService {

    void blockUser(int userId);

    void unBlockUser(int userId);

}
