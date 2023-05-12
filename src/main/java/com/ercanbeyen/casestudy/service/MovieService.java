package com.ercanbeyen.casestudy.service;

import com.ercanbeyen.casestudy.constant.Type;
import com.ercanbeyen.casestudy.dto.MovieDto;

import java.util.List;

public interface MovieService {
    MovieDto createMovie(MovieDto movie);
    List<MovieDto> getMovies(Type type, String director, Double imdbRating, Boolean sortByImdbRating, Boolean descendingByImdbRating, Integer limit, List<String> languages, String title);
    MovieDto getMovie(String id);
    MovieDto updateMovie(String id, MovieDto movie);
    void deleteMovie(String id);
}
