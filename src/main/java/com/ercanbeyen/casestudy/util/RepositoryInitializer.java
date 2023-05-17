package com.ercanbeyen.casestudy.util;

import com.ercanbeyen.casestudy.document.Movie;
import com.ercanbeyen.casestudy.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
@RequiredArgsConstructor
public class RepositoryInitializer implements CommandLineRunner {
    private final MovieRepository movieRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Movie> movieList = FileHandler.read();
        movieRepository.saveAll(movieList);
    }
}
