package com.example.scrapeit.controller;

import com.example.scrapeit.model.File;
import com.example.scrapeit.model.FileData;
import com.example.scrapeit.model.License;
import com.example.scrapeit.service.CSVService;
import com.example.scrapeit.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ScrapingController {

    private final CSVService csvService;
    private final FileService fileService;

    @Autowired
    public ScrapingController(CSVService csvService, FileService fileService) {
        this.csvService = csvService;
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
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {

        String response = fileService.saveFile(file);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/licenses/{fileId}")
    @ResponseBody
    public ResponseEntity<List<License>> getLicensesById(@PathVariable("fileId") Long fileId) {

        List<License> licenses = fileService.findLicensesById(fileId);
        return new ResponseEntity<>(licenses, HttpStatus.OK);
    }

    @GetMapping("/licensesAsCSV/{fileId}")
    @ResponseBody
    public ResponseEntity<Resource> getLicensesAsCSVById(@PathVariable("fileId") Long fileId) {
        String filename = fileService.findFileNameById(fileId);
        InputStreamResource file = new InputStreamResource(csvService.load(fileId));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename).contentType(MediaType.parseMediaType("application/csv")).body(file);
    }

}
