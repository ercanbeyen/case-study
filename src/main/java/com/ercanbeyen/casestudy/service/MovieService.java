package com.ercanbeyen.casestudy.service;

import com.ercanbeyen.casestudy.constant.Type;
import com.ercanbeyen.casestudy.dto.MovieDto;

import java.util.List;

public interface MovieService {
    MovieDto addMovie(MovieDto movie);
    List<MovieDto> getMovies(Type type, String director, Double imdbRating, Boolean sort,  Boolean descending, String title);
    MovieDto getMovie(String id);
    MovieDto updateMovie(String id, MovieDto movie);
    void deleteMovie(String id);
}
