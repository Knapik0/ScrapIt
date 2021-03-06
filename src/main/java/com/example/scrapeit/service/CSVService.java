package com.example.scrapeit.service;

import com.example.scrapeit.CSV.CSVHelper;
import com.example.scrapeit.model.License;
import com.example.scrapeit.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
public class CSVService {

    final FileRepository fileRepository;

    @Autowired
    public CSVService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public ByteArrayInputStream load(Long fileId) {
        List<License> licenses = fileRepository.findAllLicensesById(fileId);

        return CSVHelper.licencesToCSV(licenses);
    }
}
