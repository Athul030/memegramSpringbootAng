package com.athul.memegramspring.service;

import com.athul.memegramspring.dto.AdminDTO;
import com.athul.memegramspring.dto.LoginDTO;
import com.athul.memegramspring.entity.Admin;
import org.springframework.http.ResponseEntity;

public interface AdminService {

    ResponseEntity<String> register(AdminDTO adminDTO);

    ResponseEntity<String> login(LoginDTO loginDTO);

    ResponseEntity<String> logout();
}
