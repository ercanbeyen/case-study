package com.ercanbeyen.casestudy.service.impl;

import com.ercanbeyen.casestudy.constant.Message;
import com.ercanbeyen.casestudy.constant.Type;
import com.ercanbeyen.casestudy.dto.MovieDto;
import com.ercanbeyen.casestudy.dto.convert.MovieDtoConverter;
import com.ercanbeyen.casestudy.document.Movie;
import com.ercanbeyen.casestudy.exception.EntityAlreadyExistException;
import com.ercanbeyen.casestudy.exception.EntityNotFoundException;
import com.ercanbeyen.casestudy.repository.MovieRepository;
import com.ercanbeyen.casestudy.service.MovieService;
import com.ercanbeyen.casestudy.util.FileHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieServiceImpl implements MovieService {
    private final MovieRepository repository;
    private final MovieDtoConverter converter;

    @Override
    public MovieDto createMovie(MovieDto movieDto) {
        String id = movieDto.getImdbID();

        if (repository.existsById(id)) {
            throw new EntityAlreadyExistException(String.format(Message.ALREADY_EXIST, id));
        }

        Movie movie = Movie.builder()
                .imdbID(movieDto.getImdbID())
                .title(movieDto.getTitle())
                .year(movieDto.getYear())
                .rated(movieDto.getRated())
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
    public List<MovieDto> getMovies(Type type, String director, Double imdbRating, Boolean sortByImdbRating, Boolean descendingByImdbRating, Integer limit, String title) {
        List<Movie> movieList = repository.findAll();
        log.info("Movies are fetched from the database");

        movieList = filterByType(type, movieList);
        movieList = filterByDirector(director, movieList);
        movieList = filterByImdbRating(imdbRating, movieList);
        movieList = sortByImdbRating(sortByImdbRating, descendingByImdbRating, limit, movieList);
        movieList = filterByTitle(title, movieList);

        return movieList
                .stream()
                .map(converter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public MovieDto getMovie(String id) {
        Movie movieInDb = repository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(Message.NOT_FOUND, id)));

        log.info("Movie is fetched from the database");

        return converter.convert(movieInDb);
    }

    @Override
    public MovieDto updateMovie(String id, MovieDto movieDto) {
        Movie movieInDb = repository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(Message.NOT_FOUND, id)));

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
        movieInDb.setPosterUrl(movieDto.getPosterUrl());
        movieInDb.setMetascore(movieDto.getMetascore());

        if (movieInDb.getType() == Type.SERIES) {
            movieInDb.setTotalSeasons(movieDto.getTotalSeasons());
        } else {
            movieInDb.setTotalSeasons(null);
        }

        Movie savedMovie = repository.save(movieInDb);
        log.info("Movie is updated");

        return converter.convert(savedMovie);
    }

    @Override
    public void deleteMovie(String id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException(String.format(Message.NOT_FOUND, id));
        }

        repository.deleteById(id);
    }

    @Override
    public String restoreDatabase() {
        if (repository.count() > 0) {
            log.info("Repository is empty");
            repository.deleteAll();
        }

        log.info("Before read");

        List<Movie> movieList = FileHandler.read();
        log.info("File is read");

        repository.saveAll(movieList);

        return "Database is restored";
    }

    private List<Movie> filterByType(Type type, List<Movie> movieList) {
        if (type != null) {
            movieList = movieList
                    .stream()
                    .filter(movie -> movie.getType() == type)
                    .collect(Collectors.toList());

            log.info("Movies are filtered by type");
        }

        return movieList;
    }

    private List<Movie> filterByDirector(String director, List<Movie> movieList) {
        if (!StringUtils.isBlank(director)) {
            movieList = movieList
                    .stream()
                    .filter(movie -> movie.getDirector().equals(director))
                    .collect(Collectors.toList());

            log.info("Movies are filtered by director");
        }

        return movieList;
    }

    private List<Movie> filterByImdbRating(Double imdbRating, List<Movie> movieList) {
        if (imdbRating != null) {
            movieList = movieList
                    .stream()
                    .filter(movie -> movie.getImdbRating() != null && movie.getImdbRating() >= imdbRating)
                    .collect(Collectors.toList());

            log.info("Movies are filtered by the imdb rating");
        }

        return movieList;
    }

    private List<Movie> sortByImdbRating(Boolean sortByImdbRating, Boolean descendingByImdbRating, Integer limit, List<Movie> movieList) {
        if (sortByImdbRating != null && sortByImdbRating) {
            movieList = movieList
                    .stream()
                    .sorted((movie1, movie2) -> {
                        double imdbRating1 = movie1.getImdbRating();
                        double imdbRating2 = movie2.getImdbRating();

                        if (descendingByImdbRating != null && descendingByImdbRating) {
                            return Double.compare(imdbRating2, imdbRating1);
                        } else {
                            return Double.compare(imdbRating1, imdbRating2);
                        }
                    })
                    .collect(Collectors.toList());

            log.info("Movies are sorted");

            if (limit != null) {
                movieList = movieList
                        .stream()
                        .limit(limit)
                        .collect(Collectors.toList());

                log.info("Top {} movie are selected", limit);
            }
        }

        return movieList;
    }

    private List<Movie> filterByTitle(String title, List<Movie> movieList) {
        if (StringUtils.isNoneBlank(title)) {
            movieList = movieList
                    .stream()
                    .filter(movie -> movie.getTitle().equals(title))
                    .collect(Collectors.toList());

            log.info("Movie search is searched");
        }

        return movieList;
    }
}
