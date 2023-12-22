package com.videostorage.controller;

import com.videostorage.model.Video;
import com.videostorage.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequestMapping("/videos")
public class VideoController {

    @Autowired
    private final VideoService service;

    Logger log = LoggerFactory.getLogger(this.getClass());
    public VideoController(VideoService service) {
        this.service = service;
    }

    @PostMapping("/upload/one")
    public ResponseEntity<String> saveVideo(@RequestParam("title") String title, @RequestParam MultipartFile file) {
        try {
            log.info("title: {}", title);
            String successMessage = service.saveVideo(title, file);
            return ResponseEntity.status(200).body(successMessage);
        } catch (IOException exception) {
            exception.printStackTrace();
            return ResponseEntity.status(500).body("Error uploading video");
        }
    }

//    @GetMapping(value = "/get/one/byTitle/{title}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//    public Video sendVideoResponse(@PathVariable String title) throws IOException {
//        return service.getVideo(title);
//    }

    @GetMapping(value = "/delete/one/byTitle/{name}")
    public String deleteVideo(@PathVariable String name) {
        return service.deleteVideo(name);
    }

    @GetMapping(value = "/stream/{title}", produces = "video/mp4")
    public Mono<Resource> streamVideo(@PathVariable String title) throws IOException {
        return service.getVideo(title);
    }

}
