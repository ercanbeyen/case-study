package com.ercanbeyen.casestudy.entity;

import com.ercanbeyen.casestudy.constant.Genre;
import com.ercanbeyen.casestudy.constant.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie implements Serializable {
    private String imdbID; // Primary Key
    private String title;
    private String year;
    private String rated;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate released;
    private Integer runtime;
    private List<Genre> genres;
    private String director;
    private List<String> writers;
    private List<String> actors;
    private String plot;
    private List<String> languages;
    private List<String> countries;
    private String awards;
    private String posterUrl;
    private Integer metascore;
    private Double imdbRating;
    private Integer imdbVotes;
    @Enumerated(EnumType.STRING)
    private Type type;
    private Boolean comingSoon;
    private Integer totalSeasons;
}
