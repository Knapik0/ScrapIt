package com.example.scrapeit.controller;

import com.example.scrapeit.ScrapeItApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
class ScrapingControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MockMultipartFile file = new MockMultipartFile("file", "Test.txt", "text/plain", "licenseNumber|lastName|firstName|middleName|city|state|status|issueDate|expirationDate|boardAction\r\n11111|Henderson|Aron|Von|Miami|FL|Active|11/12/2012|11/12/2002|NO\r\n22222|White|Dwayne||Miami|FL|Active|11/12/2012|11/12/2002|NO\r\n".getBytes());
        mvc.perform(multipart("/").file(file))
                .andExpect(status().isOk());
    }

    @Test
    void listUploadedFiles() throws Exception {
        String uri = "/files";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("[{\"id\":1,\"name\":\"Test.txt\"}]", content);
    }

    @Test
    void getFileData() throws Exception {
        String uri = "/files/1";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
        assertTrue(content.startsWith("{\"fileId\":1,\"fileName\":\"Test.txt\""));
    }

    @Test
    void uploadFileResponsesWithAlreadyExistsWhenTryingToUploadExistingFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "Test.txt", "text/plain", "licenseNumber|lastName|firstName|middleName|city|state|status|issueDate|expirationDate|boardAction\r\n11111|Henderson|Aron|Von|Miami|FL|Active|11/12/2012|11/12/2002|NO\r\n22222|White|Dwayne||Miami|FL|Active|11/12/2012|11/12/2002|NO\r\n".getBytes());
        MvcResult mvcResult = mvc.perform(multipart("/").file(file)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("File with given name already exists in database and wont be added", content);
    }

    @Test
    void uploadFileResponsesWithUploadSuccessfulWhenUploadingNewFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "Test1.txt", "text/plain", "licenseNumber|lastName|firstName|middleName|city|state|status|issueDate|expirationDate|boardAction\r\n11111|Henderson|Aron|Von|Miami|FL|Active|11/12/2012|11/12/2002|NO\r\n22222|White|Dwayne||Miami|FL|Active|11/12/2012|11/12/2002|NO\r\n".getBytes());
        MvcResult mvcResult = mvc.perform(multipart("/").file(file)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("File uploaded successfully", content);
    }

    @Test
    void getLicensesById() throws Exception {
        String uri = "/licenses/1";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.length() > 0);
    }

    @Test
    void getLicensesAsCSVById() throws Exception {
        String uri = "/licensesAsCSV/1";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.length() > 0);
        assertTrue(content.startsWith("license"));
    }
}