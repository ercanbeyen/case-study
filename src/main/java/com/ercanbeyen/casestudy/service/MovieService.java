package com.ercanbeyen.casestudy.service;

import com.ercanbeyen.casestudy.entity.Movie;

import java.util.List;

public interface MovieService {
    Movie add(Movie movie);
    List<Movie> getMovies(String id);
    Movie getMovie(String id);
    String deleteMovie(String id);
}
