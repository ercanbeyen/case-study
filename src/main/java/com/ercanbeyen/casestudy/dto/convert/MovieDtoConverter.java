package com.ercanbeyen.casestudy.dto.convert;

import com.ercanbeyen.casestudy.dto.MovieDto;
import com.ercanbeyen.casestudy.entity.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieDtoConverter {
    public MovieDto convert(Movie movie) {
        return MovieDto.builder()
                .imdbID(movie.getImdbID())
                .title(movie.getTitle())
                .year(movie.getYear())
                .rated(movie.getRated())
                .released(movie.getReleased())
                .languages(movie.getLanguages())
                .runtime(movie.getRuntime())
                .genres(movie.getGenres())
                .director(movie.getDirector())
                .actors(movie.getActors())
                .countries(movie.getCountries())
                .writers(movie.getWriters())
                .awards(movie.getAwards())
                .plot(movie.getPlot())
                .imdbRating(movie.getImdbRating())
                .imdbVotes(movie.getImdbVotes())
                .metascore(movie.getMetascore())
                .posterUrl(movie.getPosterUrl())
                .comingSoon(movie.getComingSoon())
                .type(movie.getType())
                .build();
    }
}
