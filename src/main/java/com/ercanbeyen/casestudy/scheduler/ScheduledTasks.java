package com.ercanbeyen.casestudy.scheduler;

import com.ercanbeyen.casestudy.constant.Message;
import com.ercanbeyen.casestudy.constant.enums.Genre;
import com.ercanbeyen.casestudy.constant.enums.Type;
import com.ercanbeyen.casestudy.dto.MovieDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.*;

@Component
@Async
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {
    private final RestTemplate restTemplate;
    private static final String COLLECTION_URL = "http://localhost:8080/api/v1/movies";
    private static final String DOCUMENT_URL = COLLECTION_URL + "/{id}";

    @Scheduled(fixedRate = 10000) // Every 10 seconds
    public void checkForFilterMovies() {
        final String checkedMethod = "filterMovies";
        log.info(MessageFormat.format(Message.TASK_STARTED, checkedMethod));

        Pageable pageable = PageRequest.of(0, 1);

        log.info(Message.BEFORE_REQUEST);
        MovieDto[] movieDtoArray = restTemplate.getForObject(
                COLLECTION_URL + "?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize(),
                MovieDto[].class
        );
        log.info(Message.AFTER_REQUEST);

        assert movieDtoArray != null;

        displayMovieDtoArray(movieDtoArray);

        log.info(MessageFormat.format(Message.TASK_COMPLETED, checkedMethod));
    }

    @Scheduled(cron = "0/15 * * * * *") // Every 15 seconds
    public void checkGetMovie() {
        final String checkedMethod = "getMovie";
        log.info(MessageFormat.format(Message.TASK_STARTED, checkedMethod));

        Map<String, String> parameters = new HashMap<>();
        String imdbID = "tt0499549";
        parameters.put("id", imdbID);

        log.info(Message.BEFORE_REQUEST);
        MovieDto movieDto = restTemplate.getForObject(DOCUMENT_URL, MovieDto.class, parameters);
        log.info(Message.AFTER_REQUEST);

        log.info("MovieDto: {}", movieDto);
        assert movieDto != null;

        if (movieDto.getImdbID() == null) {
            log.error(Message.NO_SUCH_MOVIE);
        } else {
            log.info("Movie {} is fetched from the database", movieDto.getImdbID());
        }

        log.info(MessageFormat.format(Message.TASK_COMPLETED, checkedMethod));
    }

    @Scheduled(cron = "0 * * * * *") // Every minute
    public void checkForUpdateMovie() {
        final String checkedMethod = "updateMovie";
        log.info(MessageFormat.format(Message.TASK_STARTED, checkedMethod));
        String imdbID = "tt0499549";

        MovieDto movieDto = MovieDto
                .builder()
                .title("Updated Avatar")
                .year("2009")
                .rated("PG-13")
                .released(LocalDate.of(2009, 12, 18))
                .runtime(162)
                .genres(Arrays.asList(Genre.ACTION, Genre.ADVENTURE, Genre.FANTASY))
                .director("James Cameron")
                .writers(Collections.singletonList("James Cameron"))
                .actors(Arrays.asList("Sam Worthington", "Zoe Saldana", "Sigourney Weaver", "Stephen Lang"))
                .plot("A paraplegic marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.")
                .languages(Arrays.asList("English", "Spanish"))
                .countries(Arrays.asList("USA", "UK"))
                .awards("Won 3 Oscars. Another 80 wins & 121 nominations.")
                .posterUrl("http://ia.media-imdb.com/images/M/MV5BMTYwOTEwNjAzMl5BMl5BanBnXkFtZTcwODc5MTUwMw@@._V1_SX300.jpg")
                .metascore(83)
                .imdbRating(7.9)
                .imdbVotes(890_617)
                .imdbID(imdbID)
                .type(Type.MOVIE)
                .build();

        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", imdbID);

        log.info(Message.BEFORE_REQUEST);
        HttpEntity<MovieDto> request = new HttpEntity<>(movieDto);
        restTemplate.put(DOCUMENT_URL, request, parameters);
        log.info(Message.AFTER_REQUEST);

        MovieDto updatedMovie = restTemplate.getForObject(DOCUMENT_URL, MovieDto.class, parameters);

        assert updatedMovie != null;

        log.info("Updated Movie: {}", updatedMovie);

        if (updatedMovie.getImdbID() == null) {
            log.error(Message.NO_SUCH_MOVIE);
        } else if (!updatedMovie.getTitle().equals(movieDto.getTitle())) {
            log.error(MessageFormat.format(Message.UNSUCCESSFUL_OPERATION_IN_TASK, checkedMethod));
        } else {
            log.info(MessageFormat.format(Message.SUCCESSFUL_OPERATION_IN_TASK, checkedMethod));
        }

        log.info(MessageFormat.format(Message.TASK_COMPLETED, checkedMethod));
    }

    @Scheduled(fixedRate = 45000) // Every 45 seconds
    public void checkForCreateMovie() {
        final String checkedMethod = "createMovie";
        log.info(MessageFormat.format(Message.TASK_STARTED, checkedMethod));
        String imdbID = "ttx0test";

        MovieDto newMovieDto = MovieDto
                .builder()
                .title("Test Movie")
                .year("2009")
                .rated("PG-13")
                .released(LocalDate.of(2009, 12, 18))
                .runtime(162)
                .genres(Arrays.asList(Genre.ACTION, Genre.ADVENTURE, Genre.FANTASY))
                .director("James North")
                .writers(Collections.singletonList("James North"))
                .actors(Arrays.asList("Sam Worthington", "Zoe Saldana", "Sigourney Weaver", "Stephen Lang"))
                .plot("A paraplegic marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.")
                .languages(Arrays.asList("English", "Spanish"))
                .countries(Arrays.asList("USA", "UK"))
                .awards("Won 3 Oscars. Another 80 wins & 121 nominations.")
                .posterUrl("http://ia.media-imdb.com/images/M/MV5BMTYwOTEwNjAzMl5BMl5BanBnXkFtZTcwODc5MTUwMw@@._V1_SX300.jpg")
                .metascore(83)
                .imdbRating(7.9)
                .imdbVotes(890_617)
                .imdbID(imdbID)
                .type(Type.MOVIE)
                .build();

        log.info(Message.BEFORE_REQUEST);
        MovieDto createdMovieDto = restTemplate.postForObject(COLLECTION_URL, newMovieDto, MovieDto.class);
        log.info(Message.AFTER_REQUEST);

        assert createdMovieDto != null;

        if (createdMovieDto.getImdbID() == null) {
            log.error(MessageFormat.format(Message.UNSUCCESSFUL_OPERATION_IN_TASK, checkedMethod));
        } else {
            log.info(MessageFormat.format(Message.SUCCESSFUL_OPERATION_IN_TASK, checkedMethod));
        }

        log.info(MessageFormat.format(Message.TASK_COMPLETED, checkedMethod));
    }

    @Scheduled(fixedRate = 50000) // Every 50 seconds
    public void checkForDeleteMovie() {
        final String checkedMethod = "deleteMovie";
        log.info(MessageFormat.format(Message.TASK_STARTED, checkedMethod));
        String imdbID = "ttx0test";

        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", imdbID);

        log.info(Message.BEFORE_REQUEST);
        restTemplate.delete(DOCUMENT_URL, parameters);
        log.info(Message.AFTER_REQUEST);

        MovieDto deletedMovieDto = restTemplate.getForObject(DOCUMENT_URL, MovieDto.class, parameters);

        assert deletedMovieDto != null;

        if (deletedMovieDto.getImdbID() != null) {
            log.error(MessageFormat.format(Message.UNSUCCESSFUL_OPERATION_IN_TASK, checkedMethod));
        } else {
            log.info(MessageFormat.format(Message.SUCCESSFUL_OPERATION_IN_TASK, checkedMethod));
        }

        log.info(MessageFormat.format(Message.TASK_COMPLETED, checkedMethod));
    }

    @Scheduled(cron = "0 0/2 * * * *") // Every 2 minutes
    public void checkForSearchMovies() {
        final String checkedMethod = "searchMovies";
        log.info(MessageFormat.format(Message.TASK_STARTED, checkedMethod));
        String title = "avataR";
        Pageable pageable = PageRequest.of(0, 1);

        log.info(Message.BEFORE_REQUEST);
        MovieDto[] movieDtoArray = restTemplate.getForObject(
                COLLECTION_URL + "?title=" + title + "&page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize(),
                MovieDto[].class
        );
        log.info(Message.AFTER_REQUEST);

        assert movieDtoArray != null;

        displayMovieDtoArray(movieDtoArray);

        log.info(MessageFormat.format(Message.TASK_COMPLETED, checkedMethod));
    }

    public void displayMovieDtoArray(MovieDto[] movieDtoArray) {
        if (movieDtoArray.length == 0) {
            log.info("There is no movie in the database");
        } else {
            for (MovieDto movieDto : movieDtoArray) {
                log.info("ImdbID: {}", movieDto.getImdbID());
            }
        }
    }
}








