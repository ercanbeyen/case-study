package com.ercanbeyen.casestudy.controller;

import com.ercanbeyen.casestudy.dto.MovieDto;
import com.ercanbeyen.casestudy.entity.Movie;
import com.ercanbeyen.casestudy.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @PostMapping
    public ResponseEntity<Object> createMovie(@Valid @RequestBody MovieDto movie) {
        MovieDto newMovie = movieService.addMovie(movie);
        return new ResponseEntity<>(newMovie, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Object> getMovies() {
        List<MovieDto> movieList = movieService.getMovies();
        return ResponseEntity.ok(movieList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMovie(@PathVariable String id) {
        MovieDto movie = movieService.getMovie(id);
        return ResponseEntity.ok(movie);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMovie(@PathVariable String id, @Valid @RequestBody MovieDto movie) {
        MovieDto updatedMovie = movieService.updateMovie(id, movie);
        return ResponseEntity.ok(updatedMovie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMovie(@PathVariable String id) {
        String message = movieService.deleteMovie(id);
        return ResponseEntity.ok(message);
    }
}
