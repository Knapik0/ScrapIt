package com.example.scrapeit.service;

import com.example.scrapeit.model.File;
import com.example.scrapeit.model.FileData;
import com.example.scrapeit.model.License;
import com.example.scrapeit.repository.FileRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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
    @Mock
    private MultipartFile mockMultipartFile;

    @BeforeEach
    void setUp() {
        underTest = new FileService(fileRepository);
        mockMultipartFile = new MockMultipartFile("Test.txt", new byte[]{});
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
        FileData fileData = new FileData("Test.txt", 2, new Date(System.currentTimeMillis()));
        given(fileRepository.findFileDataById(anyLong())).willReturn(fileData);

        //when
        underTest.findFileDataById(anyLong());

        //then
        verify(fileRepository).findFileDataById(anyLong());
        assertEquals("Test.txt", underTest.findFileDataById(1L).getFileName());
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
    void findLicensesById() {
        //when
        underTest.findLicensesById(anyLong());

        //then
        verify(fileRepository).findAllLicensesById(anyLong());
    }

    @Test
    void findFileNameById() {
        //when
        underTest.findFileNameById(anyLong());

        //then
        verify(fileRepository).findNameById(anyLong());
    }
}