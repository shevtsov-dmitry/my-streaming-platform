package com.content_assist_with_input.flim_info.genres.controller;

import com.content_assist_with_input.flim_info.genres.model.Genre;
import com.content_assist_with_input.flim_info.genres.repo.GenreRepo;
import com.content_assist_with_input.flim_info.genres.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/film-info/genre")
public class GenreController {

    private final GenreService service;
    private final GenreRepo repo;

    @Autowired
    public GenreController(GenreService service, GenreRepo repo) {
        this.service = service;
        this.repo = repo;
    }

    @PostMapping("/add/one")
    public String addNewGenre(@RequestParam String genreName) {
        Genre genre = new Genre(genreName);
        try {
            if (genre.getName().equals("") || genre.getName().equals(" ")) {
                return "incoming parameter data is empty.";
            }
            repo.save(genre);
            return "add new genre successfully.";
        } catch (Exception e) {
            return service.saveWithoutDuplicates(new ArrayList<>(List.of(genre)));
        }
    }

    @PostMapping("/add/many")
    public String addNewGenres(@RequestBody Map<String, List<Genre>> jsonMap) {
        List<Genre> genres = jsonMap.get("genres");

        try {
            genres.removeIf(genre -> genre.getName().equals("") || genre.getName().equals(" "));
            repo.saveAll(genres);
            return "new genres have been added successfully.";
        } catch (Exception e) { // DataIntegrityViolationException
            return service.saveWithoutDuplicates(genres);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/get/many/by-sequence")
    public List<String> findGenre(@RequestParam String sequence) {
        return service.findMatchedGenres(sequence);
    }


}
