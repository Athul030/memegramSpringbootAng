package com.athul.memegramspring.service.impl;

import com.athul.memegramspring.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {


    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        //File name
        String name = file.getOriginalFilename();
        //generate a random name
        String randomID= UUID.randomUUID().toString();
        String fileName=randomID.concat(name.substring(name.lastIndexOf(".")));
        //my path
        String filePath = path + File.separator+fileName;

        File f=new File(path);
        if(!f.exists()){
            f.mkdir();
        }
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return  fileName;
    }

    @Override
    public String[] uploadMultipleImage(String path, MultipartFile[] file) throws IOException {
        String[] fileArray=new String[file.length];
        for(int i=0;i<file.length;i++){

            MultipartFile file1=file[i];
            String name=file1.getOriginalFilename();
            String randomId=UUID.randomUUID().toString();
            String fileName=randomId.concat(name.substring(name.lastIndexOf(".")));
            String filePath = path+File.separator+fileName;

            File f=new File(path);
            if(!f.exists()){
                f.mkdir();
            }
            Files.copy(file[i].getInputStream(),Paths.get(filePath));
            fileArray[i]=fileName;
        }
        return fileArray;
    }



    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        String fullPath = path+File.separator+fileName;
        InputStream is=new FileInputStream(fullPath);
        return is;
    }
}
