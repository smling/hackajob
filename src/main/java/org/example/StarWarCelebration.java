package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StarWarCelebration {
    public static String run(String film, String character) throws UnsupportedEncodingException {
        try {
            String encodedFilm = URLEncoder.encode(film, java.nio.charset.StandardCharsets.UTF_8.toString());
            String films = film + ": " + renderRequest("https://challenges.hackajob.co/swapi/api/films/?search=" + encodedFilm + "&format=json",
                    "characters",
                    "name");
            String encodedCharacter = URLEncoder.encode(character, java.nio.charset.StandardCharsets.UTF_8.toString());
            String characters = character + ": " + renderRequest("https://challenges.hackajob.co/swapi/api/people/?search=" + encodedCharacter + "&format=json",
                    "films",
                    "title");
            return films + "; " + characters;
        } catch(Exception ex) {
            throw(ex);
        }
    }

    public static String renderRequest(String endpoint, String targetItemUrlPropertyName, String targetPropertyName) {
        String json = getJsonByHttpRequest(endpoint);
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        List<String> items = new ArrayList<>();
        int count = jsonObject.get("count").getAsInt();
        if(count == 0) {
            return "none";
        }

        JsonArray searchResult = jsonObject.getAsJsonArray("results");
        for(int i = 0; i < searchResult.size(); i++) {
            JsonObject searchResultObject = searchResult.get(i).getAsJsonObject();
            JsonArray targetItemUrls = searchResultObject.getAsJsonArray(targetItemUrlPropertyName);
            for(int j = 0; j< targetItemUrls.size(); j++) {
                String targetItemUrl = targetItemUrls.get(j).getAsString();
                String targetItemJson = getJsonByHttpRequest(targetItemUrl+"?format=json");
                JsonObject targetItem = new JsonParser().parse(targetItemJson).getAsJsonObject();
                String name = targetItem.get(targetPropertyName).getAsString();
                items.add(name);
            }
        }
        items = items.stream().sorted().collect(Collectors.toList());
        return String.join(", ", items);
    }

    public static String getJsonByHttpRequest(String endpoint) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return result.toString();
    }
}
