package com.athul.memegramspring.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.Map;

public interface S3FileUploadService {

    Map<String,String> uploadFileToS3(MultipartFile multipartFile) throws FileNotFoundException;
}
