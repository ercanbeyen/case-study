package com.ercanbeyen.casestudy.entity;

import com.ercanbeyen.casestudy.constant.Genre;
import com.ercanbeyen.casestudy.constant.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@MappedSuperclass
public abstract class WatchingItem implements Serializable {
    protected String imdbID;
    protected String title;
    protected String year;
    protected String rated;
    @JsonFormat(pattern = "d-MMM-yyyy")
    protected LocalDate released; // TODO: Check type of the released
    protected Integer runtime;
    protected List<Genre> genres;
    protected String directorName;
    protected List<String> writers;
    protected String plot;
    protected List<String> languages;
    protected List<String> countries;
    protected String awards;
    protected String posterUrl;
    protected Integer metascore;
    protected Double imdbRating;
    protected Double imdbVotes;
    protected Type type;
    protected Boolean comingSoon;
    //protected Optional<Boolean> comingSoon;
}
