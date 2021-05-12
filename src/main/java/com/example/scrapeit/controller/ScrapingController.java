package com.example.scrapeit.controller;

import com.example.scrapeit.model.File;
import com.example.scrapeit.model.FileData;
import com.example.scrapeit.model.License;
import com.example.scrapeit.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class ScrapingController {

    private final FileService fileService;

    @Autowired
    public ScrapingController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/files")
    public ResponseEntity<List<File>> listUploadedFiles(Model model) {

        List<File> files = fileService.loadAllFiles();

        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @GetMapping("/files/{fileId}")
    @ResponseBody
    public ResponseEntity<FileData> getFileData(@PathVariable("fileId") Long fileId) {

        FileData fileData = fileService.findFileDataById(fileId);
        return new ResponseEntity<>(fileData, HttpStatus.OK);
    }

    @PostMapping("/")
    public String uploadFile(@RequestParam("file") MultipartFile file) {

        fileService.saveFile(file);
        return "You successfully uploaded " + file.getOriginalFilename() + "!";
    }

    @GetMapping("/licenses/{fileId}")
    @ResponseBody
    public ResponseEntity<List<License>> getLicensesById(@PathVariable("fileId") Long fileId) {

        List<License> licenses = fileService.findLicensesById(fileId);
        return new ResponseEntity<>(licenses, HttpStatus.OK);
    }

}
