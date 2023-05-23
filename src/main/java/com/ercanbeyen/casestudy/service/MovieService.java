package com.ercanbeyen.casestudy.service;

import com.ercanbeyen.casestudy.constant.enums.Type;
import com.ercanbeyen.casestudy.dto.MovieDto;

import java.util.List;

public interface MovieService {
    MovieDto createMovie(MovieDto movie);
    List<MovieDto> getMovies(Type type, String director, Double imdbRating, Boolean sortByImdbRating, Boolean descendingByImdbRating, Integer limit, String title);
    MovieDto getMovie(String id);
    MovieDto updateMovie(String id, MovieDto movie);
    void deleteMovie(String id);
    String restoreDatabase();
}
