package com.ercanbeyen.casestudy.repository;

import com.ercanbeyen.casestudy.document.Movie;
import com.ercanbeyen.casestudy.util.FileHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
public class InMemoryMovieRepository {
    private final List<Movie> movieList = new ArrayList<>();

    public Long count() {
        return (long) movieList.size();
    }

    public List<Movie> findAll() {
        return movieList;
    }

    public Optional<Movie> findById(String id) {
        return movieList.stream()
                .filter(watchingItem -> watchingItem.getImdbID().equals(id))
                .findFirst();
    }

    public Movie save(Movie movie) {
        String id = movie.getImdbID();

        if (!existsById(id)) {
            movieList.add(movie);
        } else {
            for (int i = 0; i < count(); i++) {
                Movie current = movieList.get(i);

                if (current.getImdbID().equals(id)) {
                    movieList.set(i, movie);
                    break;
                }
            }
        }

        FileHandler.write(movieList);
        return movie;
    }

    public void deleteById(String id) {
        Optional<Movie> optionalMovie = movieList.stream()
                .filter(item -> item.getImdbID().equals(id))
                .findFirst();

        optionalMovie.ifPresent(movieList::remove);

        if (existsById(id)) {
            log.error("Movie {} is not deleted from the database", id);
        }

        FileHandler.write(movieList); // delete the object and rewrite the updated list into the file
    }

    public boolean existsById(String id) {
        return movieList.stream()
                .anyMatch(watchingItem -> watchingItem.getImdbID().equals(id));
    }
}
