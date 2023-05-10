package com.ercanbeyen.casestudy.repository;

import com.ercanbeyen.casestudy.entity.Movie;
import com.ercanbeyen.casestudy.exception.EntityNotFound;
import com.ercanbeyen.casestudy.util.FileHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
public class InMemoryMovieRepository implements CommandLineRunner {
    private static List<Movie> movieList;

    @Override
    public void run(String... args) throws Exception {
        movieList = FileHandler.readFile();
    }

    public Long count() {
        return (long) movieList.size();
    }

    public List<Movie> findAll() {
        //movieList = FileHandler.readFile();
        return movieList;
    }

    public Optional<Movie> findById(String id) {
        //movieList = FileHandler.readFile();

        return movieList.stream()
                .filter(watchingItem -> watchingItem.getImdbID().equals(id))
                .findFirst();
    }

    public Movie save(Movie movie) {
        String id = movie.getImdbID();

        if (!existsById(id)) {
            movieList.add(movie);
        } else {
            for (Movie movieInDb : movieList) {
                if (movieInDb.getImdbID().equals(id)) {
                    movieInDb = movie;
                    break;
                }
            }
        }

        FileHandler.writeFile(movieList);
        //FileHandler.appendFile(movie);
        return movie;
    }

    public void deleteById(String id) {
        Optional<Movie> watchingItem = movieList.stream()
                .filter(item -> item.getImdbID().equals(id))
                .findFirst();

        watchingItem.ifPresent(movie -> movieList.remove(movie));

        if (existsById(id)) {
            log.error("Movie {} is not deleted from the database", id);
        }

        FileHandler.writeFile(movieList); // delete the object and rewrite the updated list into the file
    }

    public boolean existsById(String id) {
        return movieList.stream()
                .anyMatch(watchingItem -> watchingItem.getImdbID().equals(id));
    }
}
