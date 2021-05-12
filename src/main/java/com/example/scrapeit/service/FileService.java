package com.example.scrapeit.service;

import com.example.scrapeit.exception.FileParserException;
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
import java.util.regex.Pattern;

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
        return fileRepository.findFileDataById(fileId);
    }

    @Transactional
    public String saveFile(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        if (fileRepository.findByName(fileName) != null){
            return "File with given name already exists in database and wont be added";
        }
        File file = new File(fileName);
        int count = parseAndCountLines(multipartFile, file);
        FileData fileData = new FileData(fileName, count - 1,new Date(System.currentTimeMillis()));
        file.setFileData(fileData);
        fileRepository.save(file);
        return "File " + fileName + " uploaded successfully";
    }

    public List<License> findLicensesById(Long fileId) {
        return fileRepository.findAllLicensesById(fileId);
    }

    private int parseAndCountLines(MultipartFile multipartFile, File file) {
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
                License license = parseLineToLicenseObject(nextLine);
                file.addLicense(license);
            }
        } catch (IOException e) {
            throw new FileParserException("Couldn't parse file");
        }
        return count;
    }

    private License parseLineToLicenseObject(String nextLine) {
        String[] split = nextLine.split(Pattern.quote("|"));
        return new License(split[0], split[1], split[2], split[3], split[4], split[5], split[6], split[7], split[8], split[9]);
    }

    public String findFileNameById(Long fileId) {
        return fileRepository.findNameById(fileId);
    }
}
