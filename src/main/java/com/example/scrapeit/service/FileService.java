package com.example.scrapeit.service;

import com.example.scrapeit.exception.FileDataNotFoundException;
import com.example.scrapeit.model.File;
import com.example.scrapeit.model.FileData;
import com.example.scrapeit.model.License;
import com.example.scrapeit.repository.FileDataRepo;
import com.example.scrapeit.repository.FileRepo;
import com.example.scrapeit.repository.LicenseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class FileService {

    private final FileRepo fileRepo;
    private final FileDataRepo fileDataRepo;
    private final LicenseRepo licenseRepo;

    @Autowired
    public FileService(FileRepo fileRepo, FileDataRepo fileDataRepo, LicenseRepo licenseRepo) {
        this.fileRepo = fileRepo;
        this.fileDataRepo = fileDataRepo;
        this.licenseRepo = licenseRepo;
    }

    public List<File> loadAllFiles() {
        return fileRepo.findAll();
    }
    
    public FileData findFileDataById(Long fileId) {
        return fileDataRepo.findById(fileId).orElseThrow(()-> new FileDataNotFoundException("File with id " + fileId + " was not found"));
    }

    public void saveFile(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        File file = new File(fileName);
        List<License> licenses = new LinkedList<>();
        int count = 0;
        try {
            InputStream inputStream = multipartFile.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String nextLine;
            while (!((nextLine = bufferedReader.readLine()) == null || nextLine.isEmpty())) {
                count++;
                if (count == 1) {
                    continue;
                }
                String[] split = nextLine.split(Pattern.quote("|"));
                License license = new License(split[0], split[1], split[2], split[3], split[4], split[5], split[6], split[7], split[8], split[9]);
                license.setFile(file);
                licenses.add(license);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileData fileData = new FileData(fileName, count - 1,new Date(System.currentTimeMillis()), file);
        fileDataRepo.save(fileData);
        licenseRepo.saveAll(licenses);
//        fileRepo.save(file);
    }
}
