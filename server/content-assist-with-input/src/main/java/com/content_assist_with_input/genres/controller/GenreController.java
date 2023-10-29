package com.content_assist_with_input.genres.controller;

import com.content_assist_with_input.genres.model.Genre;
import com.content_assist_with_input.genres.repo.GenreRepo;
import com.content_assist_with_input.genres.service.GenreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/film-info-genre")
public class GenreController {

    Logger logger = LoggerFactory.getLogger(GenreController.class);
    private final GenreService service;
    private final GenreRepo repo;

    @Autowired
    public GenreController(GenreService service, GenreRepo repo) {
        this.service = service;
        this.repo = repo;
    }

    @PostMapping("/add-new-genre")
    public String addNewGenre(@RequestParam String genre) {
        Genre gnr = new Genre(genre);
        try {
            repo.save(gnr);
            return "add new genre successfully.";
        } catch (Exception e) {
            return service.saveAgainButWithoutDuplicates(new ArrayList<>(List.of(gnr)));
        }
    }

    @PostMapping("/add-new-genres")
    public String addNewGenres(@RequestBody Map<String, List<Genre>> jsonMap){
        List<Genre> genres = jsonMap.get("genres");
        try {
            repo.saveAll(genres);
            return "new genres have been added successfully.";
        } catch (Exception e) { // DataIntegrityViolationException
            return service.saveAgainButWithoutDuplicates(genres);
        }
    }

    @GetMapping("/get-genre")
    public List<String> findGenre(@RequestParam String stringSequence){
        return service.findMatchedGenres(stringSequence);
    }


}