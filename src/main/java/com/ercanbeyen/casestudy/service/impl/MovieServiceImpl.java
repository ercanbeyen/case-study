package com.ercanbeyen.casestudy.service.impl;

import com.ercanbeyen.casestudy.constant.Type;
import com.ercanbeyen.casestudy.dto.MovieDto;
import com.ercanbeyen.casestudy.dto.convert.MovieDtoConverter;
import com.ercanbeyen.casestudy.entity.Movie;
import com.ercanbeyen.casestudy.exception.EntityAlreadyExist;
import com.ercanbeyen.casestudy.exception.EntityNotFound;
import com.ercanbeyen.casestudy.repository.InMemoryMovieRepository;
import com.ercanbeyen.casestudy.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieServiceImpl implements MovieService {
    private final InMemoryMovieRepository repository;
    private final MovieDtoConverter converter;
    @Override
    public MovieDto addMovie(MovieDto movieDto) {
        String id = movieDto.getImdbID();

        if (repository.existsById(id)) {
            throw new EntityAlreadyExist("Movie " + id + " already exists");
        }

        Movie movie = Movie.builder()
                .imdbID(movieDto.getImdbID())
                .title(movieDto.getTitle())
                .year(movieDto.getYear())
                .rated(movieDto.getYear())
                .released(movieDto.getReleased())
                .languages(movieDto.getLanguages())
                .runtime(movieDto.getRuntime())
                .genres(movieDto.getGenres())
                .director(movieDto.getDirector())
                .actors(movieDto.getActors())
                .countries(movieDto.getCountries())
                .writers(movieDto.getWriters())
                .awards(movieDto.getAwards())
                .plot(movieDto.getPlot())
                .imdbRating(movieDto.getImdbRating())
                .imdbVotes(movieDto.getImdbVotes())
                .metascore(movieDto.getMetascore())
                .posterUrl(movieDto.getPosterUrl())
                .comingSoon(movieDto.getComingSoon())
                .type(movieDto.getType())
                .build();

        return converter.convert(repository.save(movie));
    }

    @Override
    public List<MovieDto> getMovies() {
        return repository
                .findAll()
                .stream()
                .map(converter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public MovieDto getMovie(String id) {
        Movie movieInDb = repository
                .findById(id)
                .orElseThrow(() -> new EntityNotFound("Movie " + id + " is not found"));

        return converter.convert(movieInDb);
    }

    @Override
    public MovieDto updateMovie(String id, MovieDto movieDto) {
        Movie movieInDb = repository
                .findById(id)
                .orElseThrow(() -> new EntityNotFound("Movie " + id + " is not found"));

        if (movieInDb.getType() == Type.SERIES) {
            movieInDb.setTotalSeasons(movieDto.getTotalSeasons());
        }

        movieInDb.setTitle(movieDto.getTitle());
        movieInDb.setYear(movieDto.getYear());
        movieInDb.setReleased(movieDto.getReleased());
        movieInDb.setGenres(movieDto.getGenres());
        movieInDb.setAwards(movieDto.getAwards());
        movieInDb.setCountries(movieDto.getCountries());
        movieInDb.setLanguages(movieDto.getLanguages());
        movieInDb.setRuntime(movieDto.getRuntime());
        movieInDb.setComingSoon(movieDto.getComingSoon());
        movieInDb.setPlot(movieDto.getPlot());
        movieInDb.setRated(movieDto.getRated());
        movieInDb.setDirector(movieDto.getDirector());
        movieInDb.setWriters(movieInDb.getWriters());
        movieInDb.setImdbRating(movieInDb.getImdbRating());
        movieInDb.setImdbVotes(movieDto.getImdbVotes());
        movieInDb.setImdbVotes(movieDto.getImdbVotes());
        movieInDb.setPosterUrl(movieDto.getPosterUrl());
        movieInDb.setMetascore(movieDto.getMetascore());

        return converter.convert(repository.save(movieInDb));
    }

    @Override
    public String deleteMovie(String id) {
        return repository.deleteById(id);
    }
}
