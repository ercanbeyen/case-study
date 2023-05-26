package com.ercanbeyen.casestudy.controller;

import com.ercanbeyen.casestudy.constant.enums.Type;
import com.ercanbeyen.casestudy.document.Movie;
import com.ercanbeyen.casestudy.dto.CustomPage;
import com.ercanbeyen.casestudy.dto.MovieDto;
import com.ercanbeyen.casestudy.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Object> filterMovies(
            @RequestParam(required = false) Type type,
            @RequestParam(required = false) String director,
            @RequestParam(required = false, value = "rating") Double imdbRating,
            @RequestParam(required = false, value = "sort") Boolean sortByImdbRating,
            @RequestParam(required = false, value = "descending") Boolean descendingByImdbRating,
            @RequestParam(required = false, value = "maxSize") Long maximumSize,
            @RequestParam(required = false) String title) {
        List<MovieDto> movieDtoList = movieService.filterMovies(type, director, imdbRating, sortByImdbRating, descendingByImdbRating, maximumSize, title);

        movieDtoList.forEach(movieDto -> {
            Link link = WebMvcLinkBuilder.linkTo(MovieController.class).slash(movieDto.getImdbID()).withSelfRel();
            movieDto.add(link);
        });

        return ResponseEntity.ok(movieDtoList);
    }

    @GetMapping(value = "/search", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Object> searchMovies(@RequestParam String title) {
        List<MovieDto> movieDtoList = movieService.searchMovies(title);

        movieDtoList.forEach(movieDto -> {
            Link link = WebMvcLinkBuilder.linkTo(MovieController.class).slash(movieDto.getImdbID()).withSelfRel();
            movieDto.add(link);
        });

        return ResponseEntity.ok(movieDtoList);
    }

    @GetMapping("/page")
    public ResponseEntity<Object> pagination(Pageable pageable) {
        CustomPage<Movie, MovieDto> page = movieService.pagination(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Object> getMovie(@PathVariable String id) {
        MovieDto movieDto = movieService.getMovie(id);

        Link selfLink = WebMvcLinkBuilder.linkTo(MovieController.class).slash(movieDto.getImdbID()).withSelfRel();
        Link posterLink = WebMvcLinkBuilder.linkTo(MovieController.class).slash(movieDto.getPosterUrl()).withRel("poster");

        movieDto.add(selfLink, posterLink);
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
