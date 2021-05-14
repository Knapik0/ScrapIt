package com.example.scrapeit.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.example.scrapeit.CSV.CSVHelper;
import com.example.scrapeit.exception.FileOfGivenIdNotFoundException;
import com.example.scrapeit.model.License;
import com.example.scrapeit.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CSVService {

    final FileRepository fileRepository;

    @Autowired
    public CSVService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public ByteArrayInputStream load(Long fileId) {
        List<License> licenses = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileOfGivenIdNotFoundException("File with id  " + fileId + " was not found in database"))
                .getLicenses();

        return CSVHelper.licencesToCSV(licenses);
    }
}
