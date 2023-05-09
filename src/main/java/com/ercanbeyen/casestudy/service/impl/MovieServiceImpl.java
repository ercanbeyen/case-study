package com.ercanbeyen.casestudy.service.impl;

import com.ercanbeyen.casestudy.constant.Type;
import com.ercanbeyen.casestudy.entity.Movie;
import com.ercanbeyen.casestudy.exception.EntityNotFound;
import com.ercanbeyen.casestudy.repository.InMemoryMovieRepository;
import com.ercanbeyen.casestudy.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieServiceImpl implements MovieService {
    private final InMemoryMovieRepository inMemoryMovieRepository;
    @Override
    public Movie addMovie(Movie movie) {
        return inMemoryMovieRepository.save(movie);
    }

    @Override
    public List<Movie> getMovies() {
        return inMemoryMovieRepository.findAll();
    }

    @Override
    public Movie getMovie(String id) {
        return inMemoryMovieRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFound("Movie " + id + " is not found"));
    }

    @Override
    public Movie updateMovie(String id, Movie movie) {
        Movie movieInDb = inMemoryMovieRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFound("Movie " + id + " is not found"));

        if (movieInDb.getType() == Type.SERIES) {
            movieInDb.setTotalSeasons(movie.getTotalSeasons());
        }

        movieInDb.setTitle(movie.getTitle());
        movieInDb.setYear(movie.getYear());
        movieInDb.setReleased(movie.getReleased());
        movieInDb.setGenres(movie.getGenres());
        movieInDb.setAwards(movie.getAwards());
        movieInDb.setCountries(movie.getCountries());
        movieInDb.setLanguages(movie.getLanguages());
        movieInDb.setRuntime(movie.getRuntime());
        movieInDb.setComingSoon(movie.getComingSoon());
        movieInDb.setPlot(movie.getPlot());
        movieInDb.setRated(movie.getRated());
        movieInDb.setDirector(movie.getDirector());
        movieInDb.setWriters(movieInDb.getWriters());
        movieInDb.setImdbRating(movieInDb.getImdbRating());
        movieInDb.setImdbVotes(movie.getImdbVotes());
        movieInDb.setImdbVotes(movie.getImdbVotes());
        movieInDb.setPosterUrl(movie.getPosterUrl());
        movieInDb.setMetascore(movie.getMetascore());

        return inMemoryMovieRepository.save(movieInDb);

    }

    @Override
    public String deleteMovie(String id) {
        return inMemoryMovieRepository.deleteById(id);
    }
}
