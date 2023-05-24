package com.ercanbeyen.casestudy.controller;

import com.ercanbeyen.casestudy.constant.enums.Type;
import com.ercanbeyen.casestudy.document.Movie;
import com.ercanbeyen.casestudy.dto.CustomPage;
import com.ercanbeyen.casestudy.dto.MovieDto;
import com.ercanbeyen.casestudy.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<Object> createMovie(@Valid @RequestBody MovieDto movieDto) {
        MovieDto newMovie = movieService.createMovie(movieDto);
        return new ResponseEntity<>(newMovie, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Object> filterMovies(
            @RequestParam(required = false) Type type,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) Double imdbRating,
            @RequestParam(required = false) Boolean sortByImdbRating,
            @RequestParam(required = false) Boolean descendingByImdbRating,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String title) {
        List<MovieDto> movieDtoList = movieService.filterMovies(type, director, imdbRating, sortByImdbRating, descendingByImdbRating, limit, title);
        return ResponseEntity.ok(movieDtoList);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchMovies(@RequestParam String title) {
        List<MovieDto> movieDtoList = movieService.searchMovies(title);
        return ResponseEntity.ok(movieDtoList);
    }

    @GetMapping("/page")
    public ResponseEntity<Object> pagination(Pageable pageable) {
        CustomPage<Movie, MovieDto> page = movieService.pagination(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMovie(@PathVariable String id) {
        MovieDto movieDto = movieService.getMovie(id);
        return ResponseEntity.ok(movieDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMovie(@PathVariable String id, @Valid @RequestBody MovieDto movieDto) {
        MovieDto updatedMovieDto = movieService.updateMovie(id, movieDto);
        return ResponseEntity.ok(updatedMovieDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMovie(@PathVariable String id) {
        movieService.deleteMovie(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/restore/database")
    public ResponseEntity<Object> restoreDatabase() {
        String message = movieService.restoreDatabase();
        return ResponseEntity.ok(message);
    }
}
