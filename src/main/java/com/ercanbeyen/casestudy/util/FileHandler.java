package com.ercanbeyen.casestudy.util;

import com.ercanbeyen.casestudy.constant.Genre;
import com.ercanbeyen.casestudy.constant.Type;
import com.ercanbeyen.casestudy.document.Movie;
import com.ercanbeyen.casestudy.exception.FileNotHandled;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
public class FileHandler {
    private static final String FILE_PATH = "MovieWebsiteJson.json";
    private static final String N_A = "N/A";

    private FileHandler() {}

    public static List<Movie> read() {
        List<Movie> list = new ArrayList<>();

        try (FileReader fileReader = new FileReader(FILE_PATH)) {
            JSONParser jsonParser = new JSONParser();
            Object parsedObject = jsonParser.parse(fileReader);
            JSONArray jsonArray = (JSONArray) parsedObject;
            
            for (Object object : jsonArray) {
                JSONObject jsonObject = (JSONObject) object;
                Movie movie = parse(jsonObject);
                list.add(movie);
            }

            return list;
        } catch (ParseException | IOException exception) {
            throw new FileNotHandled(exception.getMessage());
        }
    }

    private static Movie parse(JSONObject jsonObject) {
        String imdbID = (String) jsonObject.get("imdbID");

        String title = (String) jsonObject.get("Title");

        String year = (String) jsonObject.get("Year");

        String rated = (String) jsonObject.get("Rated");

        String localDateString = (String) jsonObject.get("Released");
        LocalDate released = MovieUtils.handleLocalDate(localDateString);

        String runtimeString = (String) jsonObject.get("Runtime");
        Integer runtime = MovieUtils.handleInteger(runtimeString.split(" ")[0]);

        String genreString = (String) jsonObject.get("Genre");
        String[] genreStrings = genreString.split(", ");

        List<Genre> genreList = new ArrayList<>();

        for (String genre : genreStrings) {
            Genre current = Genre.getGenreByUpperCaseName(genre);
            genreList.add(current);
        }

        String directorName = (String) jsonObject.get("Director");

        String actorsString = (String) jsonObject.get("Actors");
        String[] actorArray = actorsString.split(", ");
        List<String> actorList = Arrays.asList(actorArray);

        String writersString = (String) jsonObject.get("Writer");
        String[] writersArray = writersString.split(", ");
        List<String> writerList = Arrays.asList(writersArray);

        String plot = (String) jsonObject.get("Plot");

        String langaugesString = (String) jsonObject.get("Language");
        String[] languageArray = langaugesString.split(", ");
        List<String> languageList = Arrays.asList(languageArray);

        String countryString = (String) jsonObject.get("Country");
        String[] countryArray = countryString.split(", ");
        List<String> countryList = Arrays.asList(countryArray);

        String awards = (String) jsonObject.get("Awards");
        String posterUrl = (String) jsonObject.get("Poster");

        String metascoreString = (String) jsonObject.get("Metascore");
        Integer metascore = MovieUtils.handleInteger(metascoreString);

        String imdbRatingString = (String) jsonObject.get("imdbRating");
        Double imdbRating = MovieUtils.handleDouble(imdbRatingString);

        String imdbVotesString = (String) jsonObject.get("imdbVotes");
        imdbVotesString = imdbVotesString.replace(",", "");
        Integer imdbVotes = MovieUtils.handleInteger(imdbVotesString);

        String typeString = (String) jsonObject.get("Type");
        Type type = Type.getTypeByUpperCaseName(typeString);

        Boolean comingSoon = null;

        if (jsonObject.get("ComingSoon") != null) {
            String comingSoonString = (String) jsonObject.get("imdbRating");
            comingSoon = Boolean.parseBoolean(comingSoonString);
        }

        Movie movie;

        if (type == Type.SERIES) {
            String totalSeasonsString = (String) jsonObject.get("totalSeasons");
            Integer totalSeasons = Integer.parseInt(totalSeasonsString);

            movie = Movie.builder()
                    .imdbID(imdbID)
                    .title(title)
                    .year(year)
                    .released(released)
                    .languages(languageList)
                    .rated(rated)
                    .runtime(runtime)
                    .genres(genreList)
                    .director(directorName)
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
                    .director(directorName)
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
                    .build();
        }

        log.info(String.valueOf(movie));
        return movie;
    }

    public static void write(List<Movie> list) {
        try (FileWriter fileWriter = new FileWriter(FILE_PATH)) {
            // Convert Movie to the particular data format
            List<JSONObject> jsonObjectList = new ArrayList<>();

            for (Movie movie : list) {
                JSONObject jsonObject = convertToJSON(movie);
                jsonObjectList.add(jsonObject);
            }

            fileWriter.write(jsonObjectList.toString()); // Overwrite the file
            fileWriter.flush();
        } catch (IOException exception) {
            throw new FileNotHandled(exception.getMessage());
        }
    }

    private static JSONObject convertToJSON(Movie movie) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("Title", movie.getTitle());
        map.put("Year", movie.getYear());
        map.put("Rated", movie.getRated());
        LocalDate released = movie.getReleased();

        if (released == null) {
            map.put("Released", N_A);
        } else {
            int monthDay = released.getDayOfMonth();
            String month = MovieUtils.getMonth(released.getMonthValue());
            int year = released.getYear();
            String releasedYear = monthDay + " " + month + " " + year;
            map.put("Released", releasedYear);
        }

        updateMap(movie.getRuntime(), map, "runtime");
        StringBuilder runtimeValue = new StringBuilder(map.get("runtime").toString());

        if (!runtimeValue.toString().equals("N/A")) {
            runtimeValue.append(" min");
        }

        map.put("Runtime", runtimeValue.toString());

        String genre = convertToString(movie.getGenres());
        map.put("Genre", genre);

        map.put("Director", movie.getDirector());

        String writer = convertToString(movie.getWriters());
        map.put("Writer", writer);

        String actors = convertToString(movie.getActors());
        map.put("Actors", actors);

        map.put("Plot", movie.getPlot());


        String language = convertToString(movie.getLanguages());
        map.put("Language", language);


        String country = convertToString(movie.getCountries());
        map.put("Country", country);

        map.put("Awards", movie.getAwards());
        map.put("Poster", movie.getPosterUrl());

        Integer metascore = movie.getMetascore();
        updateMap(metascore, map, "Metascore");


        updateMap(movie.getImdbID(), map, "imdbID");
        updateMap(movie.getImdbRating(), map, "imdbRating");
        updateMap(movie.getImdbVotes(), map, "imdbVotes");
        updateMap(movie.getTotalSeasons(), map, "totalSeasons");
        map.put("Type", movie.getType().label);


        return new JSONObject(map);
    }

    private static String convertToString(List<?> list) {
        StringBuilder stringBuilder = new StringBuilder();
        final int LIST_SIZE = list.size();

        for (int i = 0; i < LIST_SIZE; i++) {
            Object current = list.get(i);

            stringBuilder.append(current);

            if (i != LIST_SIZE - 1) {
                stringBuilder.append(", ");
            }
        }

        return stringBuilder.toString();
    }

    private static void updateMap(Object object, HashMap<String, Object> map, String key) {
        if (object == null) {
            map.put(key, N_A);
        } else {
            map.put(key, object.toString());
        }
    }

}
