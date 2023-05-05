package com.ercanbeyen.casestudy.repository;

import com.ercanbeyen.casestudy.util.WatchingItemUtils;
import com.ercanbeyen.casestudy.constant.Genre;
import com.ercanbeyen.casestudy.constant.Type;
import com.ercanbeyen.casestudy.entity.Movie;
import com.ercanbeyen.casestudy.entity.Series;
import com.ercanbeyen.casestudy.entity.WatchingItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class WatchingItemRepository implements CommandLineRunner {
    private static final List<WatchingItem> watchingItemList = new ArrayList<>();

    public List<WatchingItem> getAll() {
        return watchingItemList;
    }

    @Override
    public void run(String... args) throws Exception {
        JSONParser jsonParser = new JSONParser();

        try {
            FileReader fileReader = new FileReader("docs/MovieWebsiteJson.json");
            Object object = jsonParser.parse(fileReader);
            JSONArray jsonMovieArray = (JSONArray) object;
            //System.out.println(movieList);
            /*jsonMovieArray.forEach(
                    jsonMovieObject -> movieList.add((Movie) jsonMovieObject));*/

            jsonMovieArray.forEach(movieObject -> parseWatchingItem((JSONObject) movieObject));
            fileReader.close();
        } catch (FileNotFoundException fileNotFoundException) {
            log.error("FileNotFound: {}", fileNotFoundException.getMessage());
            System.out.println();
        } catch (IOException ioException) {
            log.error("IOException: {}",  ioException.getMessage());
            System.out.println();
        } catch (ParseException parseException) {
            log.error("ParseException: {}", parseException.getMessage());
            System.out.println();
        }
    }

    private static void parseWatchingItem(JSONObject jsonObject) {
        WatchingItem watchingItem;

        String imdbID = (String) jsonObject.get("imdbID");

        String title = (String) jsonObject.get("Title");

        String year = (String) jsonObject.get("Year");

        String rated = (String) jsonObject.get("Rated");

        String localDate_string = (String) jsonObject.get("Released");
        //localDate_string = localDate_string.replaceAll(" ", "-");
        //LocalDate released = LocalDate.parse(localDate_string, DateTimeFormatter.ofPattern("d-MMM-yyyy"));
        LocalDate released = WatchingItemUtils.handleLocalDate(localDate_string);

        String runtime_string = (String) jsonObject.get("Runtime");
        //Integer runtime = Integer.parseInt(runtime_string.split(" ")[0]);
        Integer runtime = WatchingItemUtils.handleInteger(runtime_string.split(" ")[0]);

        String genre_string = (String) jsonObject.get("Genre");
        String[] genre_strings = genre_string.split(", ");

        List<Genre> genreList = new ArrayList<>();

        for (String genre : genre_strings) {
            //genreList.add(Genre.valueOf(genre));
            Genre current = Genre.getGenreByUpperCaseName(genre);
            genreList.add(current);
        }

        String directorName = (String) jsonObject.get("Director");

        String writers_string = (String) jsonObject.get("Writer");
        String[] writers_array = writers_string.split(", ");
        List<String> writerList = Arrays.asList(writers_array);

        String plot = (String) jsonObject.get("Plot");

        String langauges_string = (String) jsonObject.get("Language");
        String[] language_array = langauges_string.split(", ");
        List<String> languageList = Arrays.asList(language_array);

        String country_string = (String) jsonObject.get("Country");
        String[] countryArray = country_string.split(", ");
        List<String> countryList = Arrays.asList(countryArray);

        String awards = (String) jsonObject.get("Awards");
        String posterUrl = (String) jsonObject.get("url");

        String metascore_string = (String) jsonObject.get("Metascore");
        //Integer metascore = Integer.parseInt(metascore_string);
        Integer metascore = WatchingItemUtils.handleInteger(metascore_string);

        String imdbRating_string = (String) jsonObject.get("imdbRating");
        //Double imdbRating = Double.parseDouble(imdbRating_string);
        Double imdbRating = WatchingItemUtils.handleDouble(imdbRating_string);

        String imdbVotes_string = (String) jsonObject.get("imdbRating");
        //Double imdbVotes = Double.parseDouble(imdbVotes_string);
        Double imdbVotes = WatchingItemUtils.handleDouble(imdbVotes_string);

        String type_string = (String) jsonObject.get("Type");
        Type type = Type.getTypeByUpperCaseName(type_string);

        Boolean comingSoon = null;

        if (jsonObject.get("ComingSoon") != null) {
            String comingSoon_string = (String) jsonObject.get("imdbRating");
            comingSoon = Boolean.parseBoolean(comingSoon_string);
        }

        if (type == Type.SERIES) {
            String totalSeasons_string = (String) jsonObject.get("totalSeasons");
            Integer totalSeasons = Integer.parseInt(totalSeasons_string);

            watchingItem = Series.builder()
                    .imdbID(imdbID)
                    .title(title)
                    .year(year)
                    .released(released)
                    .rated(rated)
                    .runtime(runtime)
                    .genres(genreList)
                    .directorName(directorName)
                    .countries(countryList)
                    .writers(writerList)
                    .awards(awards)
                    .imdbRating(imdbRating)
                    .imdbVotes(imdbVotes)
                    .metascore(metascore)
                    .posterUrl(posterUrl)
                    .comingSoon(comingSoon)
                    .type(type)
                    .totalSeasons(totalSeasons)
                    .build();
        } else {
            watchingItem = Movie.builder()
                    .imdbID(imdbID)
                    .title(title)
                    .year(year)
                    .rated(rated)
                    .released(released)
                    .runtime(runtime)
                    .genres(genreList)
                    .directorName(directorName)
                    .countries(countryList)
                    .writers(writerList)
                    .awards(awards)
                    .imdbRating(imdbRating)
                    .imdbVotes(imdbVotes)
                    .metascore(metascore)
                    .posterUrl(posterUrl)
                    .comingSoon(comingSoon)
                    .type(type)
                    .build();
        }

        log.info(String.valueOf(watchingItem));
        watchingItemList.add(watchingItem);
        log.info(String.valueOf(watchingItemList.size()));
    }
}
