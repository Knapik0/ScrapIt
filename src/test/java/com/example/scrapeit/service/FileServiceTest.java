package com.example.scrapeit.service;

import com.example.scrapeit.exception.FileOfGivenIdNotFoundException;
import com.example.scrapeit.model.File;
import com.example.scrapeit.model.FileData;
import com.example.scrapeit.model.License;
import com.example.scrapeit.repository.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private FileRepository fileRepository;
    private FileService underTest;
    private MockMultipartFile mockMultipartFile = new MockMultipartFile("data", "Test.txt", "text/plain", "licenseNumber|lastName|firstName|middleName|city|state|status|issueDate|expirationDate|boardAction\r\n11111|Henderson|Aron|Von|Miami|FL|Active|11/12/2012|11/12/2002|NO\r\n22222|White|Dwayne||Miami|FL|Active|11/12/2012|11/12/2002|NO\r\n".getBytes());
    private MockMultipartFile duplicatedLicensesFile = new MockMultipartFile("data", "Test.txt", "text/plain", "licenseNumber|lastName|firstName|middleName|city|state|status|issueDate|expirationDate|boardAction\r\n11111|Henderson|Aron|Von|Miami|FL|Active|11/12/2012|11/12/2002|NO\r\n22222|White|Dwayne||Miami|FL|Active|11/12/2012|11/12/2002|NO\r\n".getBytes());

    @BeforeEach
    void setUp() {
        underTest = new FileService(fileRepository);
    }

    @Test
    void loadAllFiles() {
        //when
        underTest.loadAllFiles();

        //then
        verify(fileRepository).findAll();
    }

    @Test
    void findFileDataById() {
        //given
        File file = new File("Test.txt");
        FileData fileData = new FileData("Test.txt", 2, 0 ,new Date(System.currentTimeMillis()));
        file.setFileData(fileData);
        given(fileRepository.findById(anyLong())).willReturn(java.util.Optional.of(file));

        //then
        assertEquals("Test.txt", underTest.findFileDataById(1L).getFileName());
        verify(fileRepository).findById(anyLong());
    }

    @Test
    void findLicensesById() {
        //when
        License license = new License("11111|Henderson|Aron|Von|Miami|FL|Active|11/12/2012|11/12/2002|NO");
        License license1 = new License("22222|White|Dwayne||Miami|FL|Active|11/12/2012|11/12/2002|NO");
        File file = new File("Test.txt");
        file.addLicense(license);
        file.addLicense(license1);
        given(fileRepository.findById(anyLong())).willReturn(java.util.Optional.of(file));

        //then
        assertEquals(2, underTest.findLicensesById(1L).size());
        verify(fileRepository).findById(anyLong());
    }

    @Test
    void findFileNameById() {
        //when
        File file = new File("Test.txt");
        given(fileRepository.findById(anyLong())).willReturn(java.util.Optional.of(file));

        //then
        assertEquals("Test.txt",underTest.findFileNameById(1L));
        verify(fileRepository).findById(anyLong());
    }

    @Test
    void throwsExceptionWhenFileOfGivenIdDoesntExist() {
        FileOfGivenIdNotFoundException fileException = assertThrows(FileOfGivenIdNotFoundException.class,
                () -> underTest.findFileNameById(1L));
        assertEquals("File with id  1 was not found in database", fileException.getMessage());
    }

    @Test
    void canSaveMultipartFile() {
        //when
        underTest.saveFile(mockMultipartFile);

        //then
        ArgumentCaptor<File> fileArgumentCaptor = ArgumentCaptor.forClass(File.class);
        verify(fileRepository).save(fileArgumentCaptor.capture());
    }

    @Test
    void cantSaveFileWhileFileNameAlreadyInDatabase() {
        //given
        given(fileRepository.findByName(mockMultipartFile.getOriginalFilename())).willReturn(new File("Test.txt"));

        //then
        assertEquals("File with given name already exists in database and wont be added", underTest.saveFile(mockMultipartFile));
    }

    @Test
    void canSaveFileWhileContainsDuplicatedLicenses() {
        //when
        underTest.saveFile(duplicatedLicensesFile);

        //then
        ArgumentCaptor<File> fileArgumentCaptor = ArgumentCaptor.forClass(File.class);
        verify(fileRepository).save(fileArgumentCaptor.capture());
    }
}