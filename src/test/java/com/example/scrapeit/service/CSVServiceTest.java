package com.example.scrapeit.service;

import com.example.scrapeit.CSV.CSVHelper;
import com.example.scrapeit.model.File;
import com.example.scrapeit.model.License;
import com.example.scrapeit.repository.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CSVServiceTest {

    @Mock
    private FileRepository fileRepository;
    private CSVService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CSVService(fileRepository);
    }

    @Test
    void loadsCorrectByteArrayInputStream() {
        //given
        License license = new License("11111|Henderson|Aron|Von|Miami|FL|Active|11/12/2012|11/12/2002|NO");
        License license1 = new License("22222|White|Dwayne||Miami|FL|Active|11/12/2012|11/12/2002|NO");
        List<License> licenses = new ArrayList<>();
        licenses.add(license);
        licenses.add(license1);
        File file = new File("Test.txt");
        file.addLicense(license);
        file.addLicense(license1);

        given(fileRepository.findAllLicensesById(anyLong())).willReturn(licenses);

        //then
        assertEquals(CSVHelper.licencesToCSV(licenses).read(), underTest.load(0L).read());

    }
}