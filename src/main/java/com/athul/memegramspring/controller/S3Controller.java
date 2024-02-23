package com.athul.memegramspring.controller;

import com.athul.memegramspring.dto.UserDTO;
import com.athul.memegramspring.entity.User;
import com.athul.memegramspring.service.S3FileUploadService;
import com.athul.memegramspring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/files")
@RequiredArgsConstructor
public class S3Controller {

    private final S3FileUploadService s3FileUploadService;
    private final UserService userService;

    @PostMapping("/")
    public ResponseEntity<Map<String,String>> uploadFile(
            @RequestPart(name="file",required = true)MultipartFile file , @AuthenticationPrincipal UserDetails userDetails) throws FileNotFoundException {
        String fileName = s3FileUploadService.uploadFileToS3(file).get("fileUrl");
        String username = userDetails.getUsername();
        userService.changeProfilePic(username,fileName);
        Map<String, String> response = new HashMap<>();
        response.put("fileUrl", fileName);
        return ResponseEntity.ok(response);
    }

}
