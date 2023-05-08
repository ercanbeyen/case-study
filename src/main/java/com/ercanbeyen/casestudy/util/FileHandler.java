package com.ercanbeyen.casestudy.util;

import com.ercanbeyen.casestudy.constant.Genre;
import com.ercanbeyen.casestudy.constant.Type;
import com.ercanbeyen.casestudy.entity.Movie;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class FileHandler {
    private static final String filePath = "docs/MovieWebsiteJson.json";

    public static List<Movie> readFile() {
        List<Movie> list = new ArrayList<>();

        try {
            JSONParser jsonParser = new JSONParser();
            FileReader fileReader = new FileReader("docs/MovieWebsiteJson.json");
            Object parsedObject = jsonParser.parse(fileReader);
            JSONArray jsonArray = (JSONArray) parsedObject;
            
            for (Object object : jsonArray) {
                JSONObject jsonObject = (JSONObject) object;
                Movie movie = parseMovie(jsonObject);
                list.add(movie);
            }

            fileReader.close();
            return list;
        } catch (ParseException | IOException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    private static Movie parseMovie(JSONObject jsonObject) {
        String imdbID = (String) jsonObject.get("imdbID");

        String title = (String) jsonObject.get("Title");

        String year = (String) jsonObject.get("Year");

        String rated = (String) jsonObject.get("Rated");

        String localDate_string = (String) jsonObject.get("Released");
        LocalDate released = WatchingItemUtils.handleLocalDate(localDate_string);

        String runtime_string = (String) jsonObject.get("Runtime");
        Integer runtime = WatchingItemUtils.handleInteger(runtime_string.split(" ")[0]);

        String genre_string = (String) jsonObject.get("Genre");
        String[] genre_strings = genre_string.split(", ");

        List<Genre> genreList = new ArrayList<>();

        for (String genre : genre_strings) {
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
        Integer metascore = WatchingItemUtils.handleInteger(metascore_string);

        String imdbRating_string = (String) jsonObject.get("imdbRating");
        Double imdbRating = WatchingItemUtils.handleDouble(imdbRating_string);

        String imdbVotes_string = (String) jsonObject.get("imdbRating");
        Double imdbVotes = WatchingItemUtils.handleDouble(imdbVotes_string);

        String type_string = (String) jsonObject.get("Type");
        Type type = Type.getTypeByUpperCaseName(type_string);

        Boolean comingSoon = null;

        if (jsonObject.get("ComingSoon") != null) {
            String comingSoon_string = (String) jsonObject.get("imdbRating");
            comingSoon = Boolean.parseBoolean(comingSoon_string);
        }

        Movie movie;

        if (type == Type.SERIES) {
            String totalSeasons_string = (String) jsonObject.get("totalSeasons");
            Integer totalSeasons = Integer.parseInt(totalSeasons_string);

            movie = Movie.builder()
                    .imdbID(imdbID)
                    .title(title)
                    .year(year)
                    .released(released)
                    .languages(languageList)
                    .rated(rated)
                    .runtime(runtime)
                    .genres(genreList)
                    .directorName(directorName)
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
        } else {
            movie = Movie.builder()
                    .imdbID(imdbID)
                    .title(title)
                    .year(year)
                    .rated(rated)
                    .released(released)
                    .languages(languageList)
                    .runtime(runtime)
                    .genres(genreList)
                    .directorName(directorName)
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
                    .build();
        }

        log.info(String.valueOf(movie));
        return movie;
    }

    public static void writeFile(List<Movie> list) {
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            JSONArray jsonMovieArray = (JSONArray) list;
            fileWriter.write(jsonMovieArray.toJSONString()); // Overwrite the file
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
