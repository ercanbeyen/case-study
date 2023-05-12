package com.ercanbeyen.casestudy.impl;

import com.ercanbeyen.casestudy.constant.Genre;
import com.ercanbeyen.casestudy.constant.Type;
import com.ercanbeyen.casestudy.dto.MovieDto;
import com.ercanbeyen.casestudy.dto.convert.MovieDtoConverter;
import com.ercanbeyen.casestudy.entity.Movie;
import com.ercanbeyen.casestudy.exception.EntityAlreadyExist;
import com.ercanbeyen.casestudy.exception.EntityNotFound;
import com.ercanbeyen.casestudy.repository.InMemoryMovieRepository;
import com.ercanbeyen.casestudy.service.impl.MovieServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class MovieServiceImplTest {
    @InjectMocks
    private MovieServiceImpl service;
    @Mock
    private InMemoryMovieRepository repository;
    @Spy
    private MovieDtoConverter converter;

    private List<Movie> getMockMovieList() {
        String imdbID = "id1";
        String title = "title1";
        String year = "year1";
        String rated = "rated1";
        LocalDate released = LocalDate.of(2023, 5, 29);
        Integer runtime = 10;
        List<Genre> genreList = Arrays.asList(Genre.ACTION, Genre.CRIME);
        String director = "director1";
        List<String> writerList = Arrays.asList("writer1", "writer2");
        List<String> actorList = Arrays.asList("actor1", "actor2");
        String plot = "plot1";
        List<String> languageList = Arrays.asList("English", "Russian", "Turkish");
        List<String> countryList = Arrays.asList("UK", "Russia", "Turkey");
        String awards = "awards1";
        String posterUrl = "https://posterUrl";
        Integer metascore = 75;
        double imdbRating = 8.5;
        Integer imdbVotes = 5000;
        Type type = Type.MOVIE;

        Movie movie1 = Movie.builder()
                .imdbID(imdbID)
                .title(title)
                .year(year)
                .released(released)
                .languages(languageList)
                .rated(rated)
                .runtime(runtime)
                .genres(genreList)
                .director(director)
                .actors(actorList)
                .countries(countryList)
                .writers(writerList)
                .awards(awards)
                .plot(plot)
                .imdbRating(imdbRating)
                .imdbVotes(imdbVotes)
                .metascore(metascore)
                .posterUrl(posterUrl)
                .comingSoon(null)
                .type(type)
                .totalSeasons(null)
                .build();

        imdbID = "id2";
        title = "title2";
        year = "year2";
        released = LocalDate.of(2017, 11, 5);
        genreList = Arrays.asList(Genre.ACTION, Genre.CRIME);
        imdbRating = 6.8;
        languageList = Arrays.asList("Spanish", "Russian", "Turkish");
        countryList = Arrays.asList("Spain", "Russia", "Turkey");
        type = Type.SERIES;
        Integer totalSeasons = 3;
        Boolean comingSoon = true;

        Movie movie2 = Movie.builder()
                .imdbID(imdbID)
                .title(title)
                .year(year)
                .released(released)
                .languages(languageList)
                .rated(rated)
                .runtime(runtime)
                .genres(genreList)
                .director(director)
                .actors(actorList)
                .countries(countryList)
                .writers(writerList)
                .awards(awards)
                .plot(plot)
                .imdbRating(imdbRating)
                .imdbVotes(imdbVotes)
                .metascore(metascore)
                .posterUrl(posterUrl)
                .comingSoon(comingSoon)
                .type(type)
                .totalSeasons(totalSeasons)
                .build();

        return Arrays.asList(movie1, movie2);
    }

    private List<MovieDto> getMockMovieDtoList() {
        List<Movie> movieList = getMockMovieList();
        return movieList
                .stream()
                .map(converter::convert)
                .collect(Collectors.toList());
    }

    @Test
    @DisplayName("When createMovie Called With Not Existed MovieDto It Should Return MovieDto")
    public void whenCreateMovieCalledWithNotExistedMovieDto_itShouldReturnMovieDto() {
        Movie movie = getMockMovieList().get(0);
        MovieDto movieDto = getMockMovieDtoList().get(0);

        Mockito.when(repository.existsById(movieDto.getImdbID())).thenReturn(false);
        Mockito.when(repository.save(any(Movie.class))).thenReturn(movie);
        Mockito.when(converter.convert(movie)).thenReturn(movieDto);

        MovieDto result = service.createMovie(movieDto);

        assertEquals(movieDto, result);

        Mockito.verify(repository).existsById(movie.getImdbID());
        Mockito.verify(repository).save(any(Movie.class));
        //Mockito.verify(converter).convert(movie);
    }

    @Test
    @DisplayName("When createMovie Called With Existed MovieDto It Should Throw Exception")
    public void whenCreateMovieCalledWithExistedMovieDto_itShouldThrowException() {
        MovieDto movieDto = getMockMovieDtoList().get(0);
        Movie movie = getMockMovieList().get(0);
        String imdbID = movie.getImdbID();
        //repository.save(movie);

        Mockito.when(repository.existsById(imdbID)).thenReturn(true);

        RuntimeException exception = assertThrows(EntityAlreadyExist.class, () -> service.createMovie(movieDto));

        String message = "Movie " + imdbID + " already exists";

        assertEquals(message, exception.getMessage());

        //repository.deleteById(imdbID);

        Mockito.verify(repository).existsById(imdbID);
    }

    @Test
    @DisplayName("When GetMovie Called With Existed Id It Should Return MovieDto")
    public void whenGetMovieCalledWithExistedId_itShouldReturnMovieDto() {
        MovieDto movieDto = getMockMovieDtoList().get(0);
        Movie movie = getMockMovieList().get(0);
        Optional<Movie> optionalMovie = Optional.of(movie);
        String imdbID = movie.getImdbID();

        Mockito.when(repository.findById(imdbID)).thenReturn(optionalMovie);
        Mockito.when(converter.convert(movie)).thenReturn(movieDto);

        MovieDto result = service.getMovie(imdbID);

        assertEquals(movieDto, result);

        Mockito.verify(repository).findById(imdbID);
       // Mockito.verify(converter).convert(movie);
    }

    @Test
    @DisplayName("When GetMovie Called With Not Existed Id It Should Throw Exception")
    public void whenGetMovieCalledWithNotExistedId_itShouldThrowException() {
        String imdbID = "id-NotFound";
        Optional<Movie> optionalMovie = Optional.empty();

        Mockito.when(repository.findById(imdbID)).thenReturn(optionalMovie);

        RuntimeException exception = assertThrows(EntityNotFound.class, () -> service.getMovie(imdbID));

        String message = "Movie " + imdbID + " is not found";

        assertEquals(message, exception.getMessage());

        Mockito.verify(repository).findById(imdbID);
        Mockito.verifyNoInteractions(converter);
    }

    @Test
    @DisplayName("When UpdateMovie Called With Existed Id And Valid MovieDto It Should Return MovieDto")
    public void whenUpdateMovieCalledWithExistedIdAndValidMovieDto_itShouldReturnMovieDto() {
        MovieDto movieDto = getMockMovieDtoList().get(0);
        String imdbID = movieDto.getImdbID();
        Movie movie = getMockMovieList().get(0);
        Optional<Movie> optionalMovie = Optional.of(movie);

        Mockito.when(repository.findById(imdbID)).thenReturn(optionalMovie);
        Mockito.when(repository.save(any(Movie.class))).thenReturn(movie);
        Mockito.when(converter.convert(movie)).thenReturn(movieDto);

        MovieDto result = service.updateMovie(imdbID, movieDto);

        assertEquals(movieDto, result);

        Mockito.verify(repository).findById(imdbID);
        Mockito.verify(repository).save(movie);
    }

    @Test
    @DisplayName("When UpdateMovie Called With Not Existed Id And ValidDto It Should Throw Exception")
    public void whenUpdateMovieCalledWithNotExistedIdAndValidDto_itShouldThrowException() {
        MovieDto movieDto = getMockMovieDtoList().get(0);
        String imdbID = movieDto.getImdbID();
        Optional<Movie> optionalMovie = Optional.empty();


        Mockito.when(repository.findById(imdbID)).thenReturn(optionalMovie);

        RuntimeException exception = assertThrows(EntityNotFound.class, () -> service.updateMovie(imdbID, movieDto));

        String message = "Movie " + imdbID + " is not found";

        assertEquals(message, exception.getMessage());

        Mockito.verify(repository).findById(imdbID);
    }
}
