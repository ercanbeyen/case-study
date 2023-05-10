package com.ercanbeyen.casestudy.service;

import com.ercanbeyen.casestudy.dto.MovieDto;

import java.util.List;

public interface MovieService {
    MovieDto addMovie(MovieDto movie);
    List<MovieDto> getMovies();
    MovieDto getMovie(String id);
    MovieDto updateMovie(String id, MovieDto movie);
    String deleteMovie(String id);
}
