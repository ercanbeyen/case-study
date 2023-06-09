package com.ercanbeyen.casestudy.service;

import com.ercanbeyen.casestudy.constant.enums.OrderBy;
import com.ercanbeyen.casestudy.constant.enums.Type;
import com.ercanbeyen.casestudy.document.Movie;
import com.ercanbeyen.casestudy.dto.CustomPage;
import com.ercanbeyen.casestudy.dto.MovieDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {
    MovieDto createMovie(MovieDto movie);
    List<MovieDto> filterMovies(Type type, String director, Double imdbRating, OrderBy orderBy, Long maximumSize, Pageable pageable);
    MovieDto getMovie(String id);
    List<MovieDto> searchMovies(String title, Pageable pageable);
    CustomPage<Movie, MovieDto> pagination(Pageable pageable);
    MovieDto updateMovie(String id, MovieDto movie);
    void deleteMovie(String id);
    String restoreDatabase();
}
