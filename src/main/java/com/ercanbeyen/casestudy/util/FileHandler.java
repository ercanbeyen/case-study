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
import java.util.*;

@Slf4j
public class FileHandler {
    private static final String filePath = "docs/MovieWebsiteJson.json";
    private static final String N_A = "N/A";

    public static List<Movie> readFile() {
        List<Movie> list = new ArrayList<>();

        try {
            JSONParser jsonParser = new JSONParser();
            FileReader fileReader = new FileReader(filePath);
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
        LocalDate released = MovieUtils.handleLocalDate(localDate_string);

        String runtime_string = (String) jsonObject.get("Runtime");
        Integer runtime = MovieUtils.handleInteger(runtime_string.split(" ")[0]);

        String genre_string = (String) jsonObject.get("Genre");
        String[] genre_strings = genre_string.split(", ");

        List<Genre> genreList = new ArrayList<>();

        for (String genre : genre_strings) {
            Genre current = Genre.getGenreByUpperCaseName(genre);
            genreList.add(current);
        }

        String directorName = (String) jsonObject.get("Director");

        String actors_string = (String) jsonObject.get("Actors");
        String[] actorArray = actors_string.split(", ");
        List<String> actorList = Arrays.asList(actorArray);

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
        Integer metascore = MovieUtils.handleInteger(metascore_string);

        String imdbRating_string = (String) jsonObject.get("imdbRating");
        Double imdbRating = MovieUtils.handleDouble(imdbRating_string);

        String imdbVotes_string = (String) jsonObject.get("imdbVotes");
        imdbVotes_string = imdbVotes_string.replaceAll(",", "");
        /*StringBuilder stringBuilder_imdbVotes = new StringBuilder();
        int commaIndex = imdbVotes_string.indexOf(",");
        stringBuilder_imdbVotes.append(imdbVotes_string, 0, commaIndex);
        stringBuilder_imdbVotes.append(imdbVotes_string.substring(commaIndex + 1));
        Integer imdbVotes = MovieUtils.handleInteger(stringBuilder_imdbVotes.toString());*/
        Integer imdbVotes = MovieUtils.handleInteger(imdbVotes_string);

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

    public static void writeFile(List<Movie> list) {
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            // Convert Movie to the particular data format
            List<JSONObject> jsonObjectList = new ArrayList<>();

            for (Movie movie : list) {
                JSONObject jsonObject = convertToJSON(movie);
                jsonObjectList.add(jsonObject);
            }

            fileWriter.write(jsonObjectList.toString()); // Overwrite the file
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void appendFile(Movie movie) {
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            JSONObject jsonObject = convertToJSON(movie);
            fileWriter.append(jsonObject.toJSONString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            //Month month = released.getMonth();
            String month = MovieUtils.getMonth(released.getMonthValue());
            int year = released.getYear();
            String releasedYear = monthDay + " " + month + " " + year;
            map.put("Released", releasedYear);
        }

        updateMap(movie.getRuntime(), map, "runtime");
        StringBuilder runtime_value = new StringBuilder(map.get("runtime").toString());

        if (!runtime_value.toString().equals("N/A")) {
            runtime_value.append(" min");
        }

        map.put("Runtime", runtime_value.toString());

        StringBuilder genre = new StringBuilder();
        List<Genre> genreList = movie.getGenres();

        for (int i = 0; i < genreList.size(); i++) {
            Genre current = genreList.get(i);

            genre.append(current);

            if (i != movie.getGenres().size() - 1) {
                genre.append(", ");
            }
        }

        map.put("Genre", genre.toString());
        map.put("Director", movie.getDirector());

        StringBuilder writer = new StringBuilder();
        List<String> writerList = movie.getWriters();

        for (int i = 0; i < writerList.size(); i++) {
            String current = writerList.get(i);

            writer.append(current);

            if (i != writerList.size() - 1) {
                writer.append(", ");
            }
        }

        map.put("Writer", writer.toString());

        StringBuilder actors = new StringBuilder();
        List<String> actorList = movie.getActors();

        for (int i = 0; i < actorList.size(); i++) {
            String current = actorList.get(i);

            actors.append(current);

            if (i != actorList.size() - 1) {
                actors.append(", ");
            }
        }

        map.put("Actors", actors.toString());
        map.put("Plot", movie.getPlot());


        StringBuilder language = new StringBuilder();
        List<String> languageList = movie.getLanguages();

        for (int i = 0; i < languageList.size(); i++) {
            String current = languageList.get(i);

            language.append(current);

            if (i != languageList.size() - 1) {
                language.append(", ");
            }
        }

        map.put("Language", language.toString());


        StringBuilder country = new StringBuilder();
        List<String> countryList = movie.getCountries();

        for (int i = 0; i < countryList.size(); i++) {
            String current = countryList.get(i);

            country.append(current);

            if (i != languageList.size() - 1) {
                country.append(", ");
            }
        }

        map.put("Country", country.toString());

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

    private static void updateMap(Object object, HashMap<String, Object> map, String key) {
        if (object == null) {
            map.put(key, N_A);
        } else {
            map.put(key, object.toString());
        }
    }

}
