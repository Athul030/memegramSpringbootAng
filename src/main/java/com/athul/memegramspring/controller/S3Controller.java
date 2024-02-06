package com.athul.memegramspring.controller;

import com.athul.memegramspring.service.S3FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.Map;

@RestController
@RequestMapping(path = "/files")
@RequiredArgsConstructor
public class S3Controller {

    private final S3FileUploadService s3FileUploadService;

    @PostMapping("/")
    public ResponseEntity<Map<String,String>> uploadFile(
            @RequestPart(name="multipartFile",required = true)MultipartFile multipartFile) throws FileNotFoundException {
        return ResponseEntity.ok(s3FileUploadService.uploadFileToS3(multipartFile));
    }

}
