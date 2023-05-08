package com.ercanbeyen.casestudy.service.impl;

import com.ercanbeyen.casestudy.entity.Movie;
import com.ercanbeyen.casestudy.exception.EntityNotFound;
import com.ercanbeyen.casestudy.repository.InMemoryMovieRepository;
import com.ercanbeyen.casestudy.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j
public class MovieServiceImpl implements MovieService {
    private final InMemoryMovieRepository inMemoryMovieRepository;
    @Override
    public Movie add(Movie movie) {
        return inMemoryMovieRepository.save(movie);
    }

    @Override
    public List<Movie> getMovies(String id) {
        return inMemoryMovieRepository.findAll();
    }

    @Override
    public Movie getMovie(String id) {
        return inMemoryMovieRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFound("Movie " + id + " is not found"));
    }

    @Override
    public String deleteMovie(String id) {
        return inMemoryMovieRepository.deleteById(id);
    }
}
