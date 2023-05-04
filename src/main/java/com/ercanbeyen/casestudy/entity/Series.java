package com.ercanbeyen.casestudy.entity;

import com.ercanbeyen.casestudy.constant.Genre;
import com.ercanbeyen.casestudy.constant.Type;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@SuperBuilder(toBuilder = true)
public class Series extends WatchingItem {
    private Integer totalSeasons;

    public Series(String imdbID, String title, String year, String rated, LocalDate released, Integer runtime, ArrayList<Genre> genres, String directorName, List<String> writers, String plot, List<String> languages, List<String> countries, String awards, String posterUrl, Integer metascore, Double imdbRating, Double imdbVotes, Type type, Boolean comingSoon, Integer totalSeasons) {
        super(imdbID, title, year, rated, released, runtime, genres, directorName, writers, plot, languages, countries, awards, posterUrl, metascore, imdbRating, imdbVotes, type, comingSoon);
        this.totalSeasons = totalSeasons;
    }
}
