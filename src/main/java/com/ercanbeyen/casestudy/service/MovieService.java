package com.ercanbeyen.casestudy.service;

import com.ercanbeyen.casestudy.entity.Movie;

import java.util.List;

public interface MovieService {
    Movie addMovie(Movie movie);
    List<Movie> getMovies();
    Movie getMovie(String id);
    Movie updateMovie(String id, Movie movie);
    String deleteMovie(String id);
}
