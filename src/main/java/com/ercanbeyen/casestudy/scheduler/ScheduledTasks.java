package com.ercanbeyen.casestudy.scheduler;

import com.ercanbeyen.casestudy.constant.enums.Genre;
import com.ercanbeyen.casestudy.constant.enums.Type;
import com.ercanbeyen.casestudy.dto.MovieDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

@Component
@Async
@EnableScheduling
@Slf4j
public class ScheduledTasks {
    private static final String COLLECTION_URL = "http://localhost:8080/api/v1/movies";
    private static final String DOCUMENT_URL = COLLECTION_URL + "/{id}";

    @Scheduled(fixedRate = 10000) // Every 10 seconds
    public void checkForFilterMovies() {
        log.info("Cron job for filterMovies is started");
        Pageable pageable = Pageable.ofSize(1).withPage(0);
        RestTemplate restTemplate = new RestTemplate();

        log.info("Before request sent");
        MovieDto[] movieDtoArray = restTemplate.getForObject(
                COLLECTION_URL + "?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize(),
                MovieDto[].class
        );
        log.info("Response is received");

        assert movieDtoArray != null;

        for (MovieDto movieDto : movieDtoArray) {
            log.info("ImdbID: {}", movieDto.getImdbID());
        }

        log.info("Cron job for filterMovies is finished");
    }

    @Scheduled(cron = "0/15 * * * * *") // Every 15 seconds
    public void checkGetMovie() {
        log.info("Cron job for getMovie is started");

        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> parameters = new HashMap<>();
        String imdbID = "tt0499549";
        parameters.put("id", imdbID);

        log.info("Before sent the request");
        MovieDto movieDto = restTemplate.getForObject(DOCUMENT_URL, MovieDto.class, parameters);
        log.info("After response received");

        log.info("MovieDto: {}", movieDto);
        assert movieDto != null;

        if (movieDto.getImdbID() == null) {
            log.error("There is not such a movie");
        } else {
            log.info("Movie {} is fetched from the database", movieDto.getImdbID());
        }

        log.info("Cron job for getMovie is finished");
    }

    @Scheduled(cron = "0 * * * * *") // Every minute
    public void checkForUpdateMovie() {
        log.info("Cron job for updateMovie is started");
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

        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", imdbID);

        log.info("Before request is sent");
        HttpEntity<MovieDto> request = new HttpEntity<>(movieDto);
        restTemplate.put(DOCUMENT_URL, request, parameters);
        log.info("After received response");

        MovieDto updatedMovie = restTemplate.getForObject(DOCUMENT_URL, MovieDto.class, parameters);

        assert updatedMovie != null;

        log.info("Updated Movie: {}", updatedMovie);

        if (updatedMovie.getImdbID() == null) {
            log.error("There is not such a movie");
        } else if (!updatedMovie.getTitle().equals(movieDto.getTitle())) {
            log.error("Update operation is unsuccessful");
        } else {
            log.info("Update operation is successful");
        }

        log.info("Cron job for updateMovie is completed");
    }

    @Scheduled(fixedRate = 45000) // Every 45 seconds
    public void checkForCreateMovie() {
        log.info("Cron job for createMovie is started");
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

        RestTemplate restTemplate = new RestTemplate();

        log.info("Before request sent");
        MovieDto createdMovieDto = restTemplate.postForObject(COLLECTION_URL, newMovieDto, MovieDto.class);
        log.info("After response received");

        assert createdMovieDto != null;

        if (createdMovieDto.getImdbID() == null) {
            log.error("Error occurred while creating the movie");
        } else {
            log.info("Movie is successfully created");
        }

        log.info("Cron job for createMovie is completed");
    }

    @Scheduled(fixedRate = 50000) // Every 50 seconds
    public void checkForDeleteMovie() {
        log.info("Cron job is started for deleteMovie");
        String imdbID = "ttx0test";

        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", imdbID);

        log.info("Before request is sent");
        restTemplate.delete(DOCUMENT_URL, parameters);
        log.info("After request sent");

        MovieDto deletedMovieDto = restTemplate.getForObject(DOCUMENT_URL, MovieDto.class, parameters);

        assert deletedMovieDto != null;

        if (deletedMovieDto.getImdbID() != null) {
            log.error("Error occurred while deleting the movie");
        } else {
            log.info("Movie is successfully deleted");
        }

        log.info("Cron job for deleteMovie is completed");
    }

}








