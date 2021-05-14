package com.example.scrapeit.service;

import com.example.scrapeit.exception.FileDuplicateException;
import com.example.scrapeit.exception.FileOfGivenIdNotFoundException;
import com.example.scrapeit.exception.FileParserException;
import com.example.scrapeit.exception.LicenseDuplicateException;
import com.example.scrapeit.model.File;
import com.example.scrapeit.model.FileData;
import com.example.scrapeit.model.License;
import com.example.scrapeit.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class FileService {

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public List<File> loadAllFiles() {
        return fileRepository.findAll();
    }
    
    public FileData findFileDataById(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new FileOfGivenIdNotFoundException("File with id  " + fileId + " was not found in database"))
                .getFileData();
    }

    public List<License> findLicensesById(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new FileOfGivenIdNotFoundException("File with id  " + fileId + " was not found in database"))
                .getLicenses();
    }

    public String findFileNameById(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new FileOfGivenIdNotFoundException("File with id  " + fileId + " was not found in database"))
                .getName();
    }

    @Transactional
    public String saveFile(MultipartFile multipartFile) {
        try {
            File file = createFile(multipartFile);
            fileRepository.save(file);
        } catch (FileDuplicateException e) {
            return "File with given name already exists in database and wont be added";
        }
        return "File uploaded successfully";
    }

    private File createFile(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        if (fileRepository.findByName(fileName) != null){
            throw new FileDuplicateException("File duplicate");
        }
        File file = new File(fileName);
        int count = 0;
        int duplicatesCount = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8));
            String nextLine;
            while ((nextLine = bufferedReader.readLine()) != null) {
                count++;
                if (count == 1) {
                    continue;
                }
                try {
                    License license = new License(nextLine);
                    file.addLicense(license);
                } catch (LicenseDuplicateException e) {
                    duplicatesCount++;
                }
            }
        } catch (IOException e) {
            throw new FileParserException("Couldn't parse file");
        }
        FileData fileData = new FileData(fileName, count - 1, duplicatesCount, new Date(System.currentTimeMillis()));
        file.setFileData(fileData);
        return file;
    }
}
