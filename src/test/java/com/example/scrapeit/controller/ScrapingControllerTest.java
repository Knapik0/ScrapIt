package com.example.scrapeit.controller;

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
        //given
        String uri = "/files";

        //when
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //then
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEquals("[{\"id\":1,\"name\":\"Test.txt\"}]", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void getFileData() throws Exception {
        //given
        String uri = "/files/1";

        //when
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //then
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertTrue(mvcResult.getResponse().getContentAsString().startsWith("{\"fileId\":1,\"fileName\":\"Test.txt\""));
    }

    @Test
    void uploadFileResponsesWithAlreadyExistsWhenTryingToUploadExistingFile() throws Exception {
        //given
        String uri = "/";
        MockMultipartFile file = new MockMultipartFile("file", "Test.txt", "text/plain", "licenseNumber|lastName|firstName|middleName|city|state|status|issueDate|expirationDate|boardAction\r\n11111|Henderson|Aron|Von|Miami|FL|Active|11/12/2012|11/12/2002|NO\r\n22222|White|Dwayne||Miami|FL|Active|11/12/2012|11/12/2002|NO\r\n".getBytes());

        //when
        MvcResult mvcResult = mvc.perform(multipart(uri)
                .file(file)).andReturn();

        //then
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEquals("File with given name already exists in database and wont be added", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void uploadFileResponsesWithUploadSuccessfulWhenUploadingNewFile() throws Exception {
        //given
        String uri = "/";
        MockMultipartFile file = new MockMultipartFile("file", "Test1.txt", "text/plain", "licenseNumber|lastName|firstName|middleName|city|state|status|issueDate|expirationDate|boardAction\r\n11111|Henderson|Aron|Von|Miami|FL|Active|11/12/2012|11/12/2002|NO\r\n22222|White|Dwayne||Miami|FL|Active|11/12/2012|11/12/2002|NO\r\n".getBytes());

        //when
        MvcResult mvcResult = mvc.perform(multipart(uri).file(file)).andReturn();

        //then
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEquals("File uploaded successfully", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void getLicensesById() throws Exception {
        //given
        String uri = "/licenses/1";

        //when
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //then
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertTrue(mvcResult.getResponse().getContentAsString().lines().count() > 0);
    }

    @Test
    void getLicensesAsCSVById() throws Exception {
        //given
        String uri = "/licensesAsCSV/1";

        //when
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //then
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertTrue(mvcResult.getResponse().getContentAsString().lines().count() > 0);
    }
}