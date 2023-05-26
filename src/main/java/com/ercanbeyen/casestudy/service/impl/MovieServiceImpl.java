package com.ercanbeyen.casestudy.service.impl;

import com.ercanbeyen.casestudy.constant.Message;
import com.ercanbeyen.casestudy.constant.enums.Type;
import com.ercanbeyen.casestudy.dto.CustomPage;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
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
            log.warn("Movie already exists");
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

        Movie newMovie = repository.save(movie);
        log.info("Movie is created");

        return converter.convert(newMovie);
    }

    @Override
    public List<MovieDto> filterMovies(Type type, String director, Double imdbRating, Boolean sortByImdbRating, Boolean descendingByImdbRating, Long maximumSize, String title) {
        Predicate<Movie> filteringMovie =
                movie -> (
                        (type == null || movie.getType() == type)
                        && (StringUtils.isBlank(director) || movie.getDirector().equals(director))
                        && (StringUtils.isBlank(title) || movie.getDirector().equals(director))
                        && (imdbRating == null || (movie.getImdbRating() != null && movie.getImdbRating() >= imdbRating))
                );

        List<Movie> movieList = repository.findAll(); // Eager Loading
        Comparator<Movie> movieComparator = Comparator.comparing(Movie::getImdbRating);

        if (maximumSize == null) {
            maximumSize = repository.count();
            log.info("There is no is maximum size value");
        }

        if (!Boolean.TRUE.equals(sortByImdbRating)) {
            movieList = movieList
                    .stream()
                    .filter(filteringMovie)
                    .limit(maximumSize)
                    .collect(Collectors.toList());
            log.info("List is not sorted");

        } else if (Boolean.TRUE.equals(descendingByImdbRating)) {
            movieList = movieList
                    .stream()
                    .filter(filteringMovie)
                    .sorted(movieComparator.reversed())
                    .limit(maximumSize)
                    .collect(Collectors.toList());
            log.info("List is sorted as descending");
        } else {
            movieList = movieList
                    .stream()
                    .filter(filteringMovie)
                    .sorted(movieComparator)
                    .limit(maximumSize)
                    .collect(Collectors.toList());
            log.info("List is sorted as ascending");
        }

        return movieList
                .stream()
                .map(converter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public MovieDto getMovie(String id) {
        Movie movieInDb = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(Message.NOT_FOUND, id)));
        log.info("Movie is fetched from the database");

        return converter.convert(movieInDb);
    }

    @Override
    public List<MovieDto> searchMovies(String title) {
        return repository.findAllByTitleLikeIgnoreCase(title)
                .stream()
                .map(converter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public CustomPage<Movie, MovieDto> pagination(Pageable pageable) {
        Page<Movie> page = repository.findAll(pageable);
        log.info("Pagination is applied for movie list");

        List<MovieDto> movieDtoList = page.getContent()
                .stream()
                .map(converter::convert)
                .collect(Collectors.toList());
        log.info("Pagination is applied for movie dto list");

        return new CustomPage<>(page, movieDtoList);
    }

    @Override
    public MovieDto updateMovie(String id, MovieDto movieDto) {
        Movie movieInDb = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(Message.NOT_FOUND, id)));
        log.info("Movie is found from the database");

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
            log.info("Type is series");
        } else {
            movieInDb.setTotalSeasons(null);
            log.info("Type is movie");
        }

        Movie savedMovie = repository.save(movieInDb);
        log.info("Movie is updated");

        return converter.convert(savedMovie);
    }

    @Override
    public void deleteMovie(String id) {
        if (!repository.existsById(id)) {
            log.warn("Id is not found");
            throw new EntityNotFoundException(String.format(Message.NOT_FOUND, id));
        }

        log.info("Movie is found");

        repository.deleteById(id);
        log.info("Movie is successfully deleted");
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
        log.info("Database is restored");

        return "Database is restored";
    }
}
