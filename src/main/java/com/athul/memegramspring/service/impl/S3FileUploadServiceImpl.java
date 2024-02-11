package com.athul.memegramspring.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.athul.memegramspring.service.S3FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class S3FileUploadServiceImpl implements S3FileUploadService {

   private String s3BaseUrl ="s3-us-east-2.amazonaws.com/";
   private String bucketName = "memegramawsbucket1";

   private String folderName = "/myfolder/images/";

   private final AmazonS3 amazonS3;

    @Override
    public Map<String, String> uploadFileToS3(MultipartFile multipartFile) throws FileNotFoundException {

        Map<String, String> response = new HashMap<>();

        if(multipartFile!=null && !multipartFile.isEmpty()){
            String filePathName = multipartFile.getOriginalFilename();
            File file = new File(filePathName);
            try(FileOutputStream fos = new FileOutputStream(file)){
                if(!file.exists()){
                    file.createNewFile();
                }
                fos.write(multipartFile.getBytes());
                fos.flush();

                //upload files to s3
                amazonS3.putObject(new PutObjectRequest(bucketName,folderName+file.getName(),file)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

                /*Url location of the upload file in s3, I should save the s3fileAccessUrl in the DB*/
                String replacedFileName = file.getName().replaceAll("\\s","+");
                StringBuffer s3FileAccessUrlBuff = new StringBuffer();
                s3FileAccessUrlBuff.append("https://").append(bucketName).append(".").append(s3BaseUrl).append(folderName).append(replacedFileName);
                String s3FileAccessUrl = s3FileAccessUrlBuff.toString();
                response.put("fileUrl",s3FileAccessUrl);
                System.out.println("Check the value"+ response);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("here");
        return  response;
    }

}
