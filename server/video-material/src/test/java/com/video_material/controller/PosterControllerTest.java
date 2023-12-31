package com.video_material.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.video_material.CONSTANTS;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PosterControllerTest {

    @Autowired
    MockMvc mockMvc;
    final String endpointURL = STR."\{CONSTANTS.HOST_AND_PORT}/video-materials/posters";

    String contentType = "multipart/form-data";

    // JPEG
    static String idJPEG;
    String nameJPEG = "sin-city-poster";
    String filenameJPEG = STR."\{nameJPEG}.jpg";
    Path pathJPEG = Paths.get(CONSTANTS.POSTERS_PATH + filenameJPEG);
    byte[] contentJPEG = Files.readAllBytes(pathJPEG);
    MockMultipartFile fileJPEG = new MockMultipartFile("file", filenameJPEG, contentType, contentJPEG);


    // PNG
    static String idPNG;
    String namePNG = "png-image";
    String filenamePNG = STR."\{namePNG}.png";
    Path pathPNG = Paths.get(CONSTANTS.POSTERS_PATH + filenamePNG);
    byte[] contentPNG = Files.readAllBytes(pathPNG);
    MockMultipartFile filePNG = new MockMultipartFile("file", filenamePNG, contentType, contentPNG);

    PosterControllerTest() throws IOException {
    }

    @Test
    @Order(1)
    void uploadJPEGImage() throws Exception {
        String url = STR."\{endpointURL}/upload";
        mockMvc.perform(multipart(url)
                        .file(fileJPEG))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andDo(result -> setId(result.getResponse().getContentAsString(), FORMATS.JPEG));
    }

    @Test
    @Order(2)
    void uploadPNGImage() throws Exception {
        String url = STR."\{endpointURL}/upload";
        mockMvc.perform(multipart(url)
                        .file(filePNG))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andDo(result -> setId(result.getResponse().getContentAsString(), FORMATS.PNG));
    }

    enum FORMATS {
        PNG,
        JPEG
    }

    void setId(String response, FORMATS format) throws JsonProcessingException {
        Gson gson = new Gson();
        Map<String, String> map = gson.fromJson(response, Map.class);
        final String id = map.get("id");
        switch (format) {
            case PNG -> idPNG = id;
            case JPEG -> idJPEG = id;
        }
    }

    @Test
    @Order(3)
    void getById() throws Exception {
        String url = STR."\{endpointURL}/get/byId/\{idJPEG}";
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/jpeg"));

        // * in case if PNG file was uploaded client gets JPEG file anyway
        url = STR."\{endpointURL}/get/byId/\{idPNG}";
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/jpeg"));
    }

    @Test
    @Order(4)
    void cleanDatabaseAfterJPEGFileUpload() throws Exception {
        String url = STR."\{endpointURL}/delete/byId/\{idJPEG}";
        mockMvc.perform(delete(url))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string(STR."video file with id \{idJPEG} successfully deleted."));
    }

    @Test
    @Order(5)
    void cleanDatabaseAfterPNGFileUpload() throws Exception {
        String url = STR."\{endpointURL}/delete/byId/\{idPNG}";
        mockMvc.perform(delete(url))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string(STR."video file with id \{idPNG} successfully deleted."));
    }
}