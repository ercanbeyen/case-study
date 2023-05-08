package com.ercanbeyen.casestudy.repository.impl;

import com.ercanbeyen.casestudy.constant.Type;
import com.ercanbeyen.casestudy.entity.Movie;
import com.ercanbeyen.casestudy.exception.EntityNotFound;
import com.ercanbeyen.casestudy.repository.InMemoryMovieRepository;
import com.ercanbeyen.casestudy.util.FileHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class InMemoryMovieRepositoryImpl implements InMemoryMovieRepository, CommandLineRunner {
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
                if (movieInDb.getType() == Type.SERIES) {
                    movieInDb.setTotalSeasons(movie.getTotalSeasons());
                }

                if (movieInDb.getImdbID().equals(id)) {
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
                    movieInDb.setDirectorName(movie.getDirectorName());
                    movieInDb.setWriters(movieInDb.getWriters());
                    movieInDb.setImdbRating(movieInDb.getImdbRating());
                    movieInDb.setImdbVotes(movie.getImdbVotes());
                    movieInDb.setImdbVotes(movie.getImdbVotes());
                    movieInDb.setPosterUrl(movie.getPosterUrl());
                    movieInDb.setMetascore(movie.getMetascore());
                    break;
                }
            }
        }

        FileHandler.writeFile(movieList);
        return movie;
    }

    public String deleteById(String id) {
        Optional<Movie> watchingItem = movieList.stream()
                .filter(item -> item.getImdbID().equals(id))
                .findFirst();

        if (!watchingItem.isPresent()) {
            throw new EntityNotFound("Item" + id + "is not found");
        }

        movieList.remove(watchingItem.get());

        if (existsById(id)) {
            log.error("Item {} is not deleted from the database", id);
            return "Item" + id +  "is not deleted from the database";
        }

        FileHandler.writeFile(movieList); // delete the object and rewrite the updated list into the file
        return "Item" + id +  "is successfully deleted from the database";
    }

    public boolean existsById(String id) {
        return movieList.stream()
                .anyMatch(watchingItem -> watchingItem.getImdbID().equals(id));
    }
}
