package com.ercanbeyen.casestudy.dto;

import com.ercanbeyen.casestudy.constant.annotation.ImdbIDRequest;
import com.ercanbeyen.casestudy.constant.enums.Genre;
import com.ercanbeyen.casestudy.constant.enums.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;


@Data
@Builder
public class MovieDto {
    @ImdbIDRequest
    private String imdbID; // Primary Key
    @NotBlank(message = "Title should not be blank")
    private String title;
    private String year;
    private String rated;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate released;
    private Integer runtime;
    private List<Genre> genres;
    @NotBlank(message = "Director should not be blank")
    private String director;
    private List<String> writers;
    private List<String> actors;
    private String plot;
    private List<String> languages;
    private List<String> countries;
    private String awards;
    private String posterUrl;
    @Max(100)
    @Min(0)
    private Integer metascore;
    @NotNull(message = "Imdb Rating should not be null")
    @Max(10)
    @Min(0)
    private Double imdbRating;
    @Min(0)
    private Integer imdbVotes;
    @Enumerated(EnumType.STRING)
    private Type type;
    private Boolean comingSoon;
    @Min(0)
    private Integer totalSeasons;
}
