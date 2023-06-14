package com.ercanbeyen.casestudy.controller;

import com.ercanbeyen.casestudy.constant.enums.OrderBy;
import com.ercanbeyen.casestudy.constant.enums.Type;
import com.ercanbeyen.casestudy.document.Movie;
import com.ercanbeyen.casestudy.dto.CustomPage;
import com.ercanbeyen.casestudy.dto.MovieDto;
import com.ercanbeyen.casestudy.exception.DocumentAlreadyExistException;
import com.ercanbeyen.casestudy.exception.DocumentNotFoundException;
import com.ercanbeyen.casestudy.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Create movie",
            description = "Create movie from the given object",
            tags = {"post"}
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Movie Dto Object",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = MovieDto.class
                            )
                    )
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = Movie.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = DocumentAlreadyExistException.class
                                    )
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Object> createMovie(@Valid @RequestBody MovieDto movieDto) {
        MovieDto newMovie = movieService.createMovie(movieDto);
        return new ResponseEntity<>(newMovie, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Filter movies",
            description = "Get a page containing all movie data with the title containing the given string",
            tags = {"get"}
    )
    @Parameters(
            value = {
                    @Parameter(
                            name = "type",
                            in = ParameterIn.QUERY,
                            description = "Type to filter movies"
                    ),
                    @Parameter(
                            name = "director",
                            in = ParameterIn.QUERY,
                            description = "Name of the director"
                    ),
                    @Parameter(
                            name = "rating",
                            in = ParameterIn.QUERY,
                            description = "Imdb Rating of the movie"
                    ),
                    @Parameter(
                            name = "orderBy",
                            in = ParameterIn.QUERY,
                            description = "Sorting style"
                    ),
                    @Parameter(
                            name = "maxSize",
                            in = ParameterIn.QUERY,
                            description = "Maximum rendered Movie amount"
                    )
            }

    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/hal+json",
                    array = @ArraySchema(
                            schema = @Schema(
                                    implementation = Movie.class
                            )
                    )
            )
    )
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Object> filterMovies(
            @RequestParam(required = false) Type type,
            @RequestParam(required = false) String director,
            @RequestParam(required = false, value = "rating") Double imdbRating,
            @RequestParam(required = false, value = "orderBy") OrderBy orderBy,
            @RequestParam(required = false, value = "maxSize") Long maximumSize,
            Pageable pageable) {
        List<MovieDto> movieDtoList = movieService.filterMovies(type, director, imdbRating, orderBy, maximumSize, pageable);

        movieDtoList.forEach(movieDto -> {
            Link link = WebMvcLinkBuilder.linkTo(MovieController.class).slash(movieDto.getImdbID()).withSelfRel();
            movieDto.add(link);
        });

        return ResponseEntity.ok(movieDtoList);
    }

    @Operation(
            summary = "Search movies",
            description = "Get a page containing all movie data with the title containing the given title",
            tags = {"get"}
    )
    @Parameters(
            value = {
                    @Parameter(
                            name = "title",
                            in = ParameterIn.QUERY,
                            description = "Title of the movie"
                    ),
                    @Parameter(
                            name = "page",
                            in = ParameterIn.QUERY,
                            description = "Page number"
                    ),
                    @Parameter(
                            name = "size",
                            in = ParameterIn.QUERY,
                            description = "Page size"
                    )
            }

    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/hal+json",
                    array = @ArraySchema(
                            schema = @Schema(
                                    implementation = Movie.class
                            )
                    )
            )
    )
    @GetMapping(value = "/search", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Object> searchMovies(@RequestParam String title, Pageable pageable) {
        List<MovieDto> movieDtoList = movieService.searchMovies(title, pageable);

        movieDtoList.forEach(movieDto -> {
            Link link = WebMvcLinkBuilder.linkTo(MovieController.class).slash(movieDto.getImdbID()).withSelfRel();
            movieDto.add(link);
        });

        return ResponseEntity.ok(movieDtoList);
    }

    @Operation(
            summary = "Get movies in a page",
            description = "Get a page containing all movie data with the title containing the given title",
            tags = {"get"}
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(
                                    implementation = Movie.class
                            )
                    )
            )
    )
    @GetMapping("/pagination")
    public ResponseEntity<Object> pagination(Pageable pageable) {
        CustomPage<Movie, MovieDto> page = movieService.pagination(pageable);
        return ResponseEntity.ok(page);
    }

    @Operation(
            summary = "Get movie",
            description = "Get the movie object with the given imdbID",
            tags = {"get"}
    )
    @Parameter(
            name = "id",
            in = ParameterIn.QUERY,
            description = "Imdb ID of the movie"
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/hal+json",
                    array = @ArraySchema(
                            schema = @Schema(
                                    implementation = Movie.class
                            )
                    )
            )
    )
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Object> getMovie(@PathVariable String id) {
        MovieDto movieDto = movieService.getMovie(id);

        Link selfLink = WebMvcLinkBuilder.linkTo(MovieController.class).slash(movieDto.getImdbID()).withSelfRel();
        Link posterLink = WebMvcLinkBuilder.linkTo(MovieController.class).slash(movieDto.getPosterUrl()).withRel("poster");

        movieDto.add(selfLink, posterLink);
        return ResponseEntity.ok(movieDto);
    }

    @Operation(
            summary = "Update movie",
            description = "Update the movie object with the given id and Movie Dto object",
            tags = {"put"}
    )
    @Parameter(
            name = "id",
            in = ParameterIn.QUERY,
            description = "Imdb ID of the movie"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Movie Dto Object",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = MovieDto.class
                            )
                    )
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = Movie.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = DocumentNotFoundException.class
                                    )
                            )
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMovie(@PathVariable String id, @Valid @RequestBody MovieDto movieDto) {
        MovieDto updatedMovieDto = movieService.updateMovie(id, movieDto);
        return ResponseEntity.ok(updatedMovieDto);
    }

    @Operation(
            summary = "Delete movie",
            description = "Delete the movie object with the given id",
            tags = {"delete"}
    )
    @Parameter(
            name = "id",
            in = ParameterIn.QUERY,
            description = "Imdb ID of the movie"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = DocumentNotFoundException.class
                                    )
                            )
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMovie(@PathVariable String id) {
        movieService.deleteMovie(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Restore movie database",
            description = "First clear the database, then add all the objects inside MovieWebSiteJSON file",
            tags = {"post"}
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            title = "Message confirms that database is restored"
                    )
            )
    )
    @PostMapping("/restore/database")
    public ResponseEntity<Object> restoreDatabase() {
        String message = movieService.restoreDatabase();
        return ResponseEntity.ok(message);
    }

}
