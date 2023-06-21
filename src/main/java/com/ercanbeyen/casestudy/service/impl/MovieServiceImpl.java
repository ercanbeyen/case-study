package com.ercanbeyen.casestudy.service.impl;

import com.ercanbeyen.casestudy.constant.Message;
import com.ercanbeyen.casestudy.constant.enums.OrderBy;
import com.ercanbeyen.casestudy.constant.enums.Type;
import com.ercanbeyen.casestudy.dto.CustomPage;
import com.ercanbeyen.casestudy.dto.MovieDto;
import com.ercanbeyen.casestudy.dto.convert.MovieDtoConverter;
import com.ercanbeyen.casestudy.document.Movie;
import com.ercanbeyen.casestudy.exception.DocumentAlreadyExistException;
import com.ercanbeyen.casestudy.exception.DocumentNotFoundException;
import com.ercanbeyen.casestudy.repository.MovieRepository;
import com.ercanbeyen.casestudy.service.MovieService;
import com.ercanbeyen.casestudy.util.FileHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.MessageFormat;
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

    /**
     *
     * @param movieDto is the template object which stores information of the new movie object
     * @return movieDto format of the newly created movie object
     */
    @Override
    public MovieDto createMovie(MovieDto movieDto) {
        String id = movieDto.getImdbID();

        if (repository.existsById(id)) {
            log.warn(MessageFormat.format(Message.ALREADY_EXIST, id));
            throw new DocumentAlreadyExistException(MessageFormat.format(Message.ALREADY_EXIST, id));
        }

        log.info("Movie is not in the database. ImdbID is unique");

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
        log.info(MessageFormat.format(Message.SUCCESSFUL_OPERATION_IN_SERVICE, "created"));

        return converter.convert(newMovie);
    }

    /**
     *
     * @param type is Type of the object
     * @param director refers to name of the director
     * @param imdbRating is rating of the object in the imdb
     * @param orderBy is sorting rule
     * @param maximumSize is the limit value of the returned value
     * @param pageable stores values of the page (number and size)
     * @return filtered movies
     */
    @Override
    public List<MovieDto> filterMovies(Type type, String director, Double imdbRating, OrderBy orderBy, Long maximumSize, Pageable pageable) {
        Predicate<Movie> filteringMovie =
                movie -> (
                        (type == null || movie.getType() == type)
                        && (StringUtils.isBlank(director) || movie.getDirector().equals(director))
                        && (imdbRating == null || (movie.getImdbRating() != null && movie.getImdbRating() >= imdbRating))
                );

        List<Movie> movieList = repository.findAll(pageable).toList(); // Lazy Loading

        if (maximumSize == null) {
            maximumSize = repository.count();
            log.info("There is no is maximum size value");
        }

        if (orderBy == null) {
            log.info("List is not sorted");
            return movieList
                    .stream()
                    .filter(filteringMovie)
                    .limit(maximumSize)
                    .collect(Collectors.toList())
                    .stream()
                    .map(converter::convert)
                    .collect(Collectors.toList());
        }


        Comparator<Movie> movieComparator = Comparator.comparing(Movie::getImdbRating);
        log.info("List will be sorted as ascending");

        if (orderBy == OrderBy.DESCENDING) {
            movieComparator = movieComparator.reversed();
            log.info("Sorting status is changed, list will be sorted as descending");
        }

        log.info("List is being sorted");

        return movieList
                .stream()
                .filter(filteringMovie)
                .sorted(movieComparator)
                .limit(maximumSize)
                .collect(Collectors.toList())
                .stream()
                .map(converter::convert)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param id is imdbID of the movie
     * @return found movie
     */
    @Override
    public MovieDto getMovie(String id) {
        Movie movieInDb = findById(id);
        log.info(MessageFormat.format(Message.SUCCESSFUL_OPERATION_IN_SERVICE, "fetched from the database"));

        return converter.convert(movieInDb);
    }

    /**
     *
     * @param title is search criteria of the method
     * @param pageable stores values of the page (number and size)
     * @return matching movieDto objects after operation completes
     */
    @Override
    public List<MovieDto> searchMovies(String title, Pageable pageable) {
        return repository.findAllByTitleLikeIgnoreCase(title, pageable)
                .stream()
                .map(converter::convert)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param pageable stores values of the page (number and size)
     * @return custom page contains movie dto objects
     */
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

    /**
     *
     * @param id is imdbID of the movie
     * @param movieDto is the template object which stores information of the new movie object
     * @return movieDto format of the updated movie object
     */
    @Override
    public MovieDto updateMovie(String id, MovieDto movieDto) {
        Movie movieInDb = findById(id);
        log.info(MessageFormat.format(Message.MOVIE_FOUND, id));

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
            log.info(MessageFormat.format(Message.TYPE_OF_MOVIE, "series"));
        } else {
            movieInDb.setTotalSeasons(null);
            log.info(MessageFormat.format(Message.TYPE_OF_MOVIE, "movie"));
        }

        Movie savedMovie = repository.save(movieInDb);
        log.info(MessageFormat.format(Message.SUCCESSFUL_OPERATION_IN_SERVICE, "updated"));

        return converter.convert(savedMovie);
    }

    /**
     *
     * @param id is imdbID of the movie
     */
    @Override
    public void deleteMovie(String id) {
        if (!repository.existsById(id)) {
            throw new DocumentNotFoundException(MessageFormat.format(Message.NOT_FOUND, id));
        }
        log.info(MessageFormat.format(Message.MOVIE_FOUND, id));

        repository.deleteById(id);
        log.info(MessageFormat.format(Message.SUCCESSFUL_OPERATION_IN_SERVICE, "deleted"));
    }

    /**
     *
     * @return verification message about the restore of the database
     */
    @Override
    public String restoreDatabase() {
        if (repository.count() > 0) {
            log.info("Database is not empty");
            repository.deleteAll();
        }

        log.info("Before read");
        List<Movie> movieList = FileHandler.read();
        log.info("After read");

        repository.saveAll(movieList);
        log.info(Message.DATABASE_RESTORED);

        return Message.DATABASE_RESTORED;
    }

    private Movie findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(MessageFormat.format(Message.NOT_FOUND, id)));
    }
}
