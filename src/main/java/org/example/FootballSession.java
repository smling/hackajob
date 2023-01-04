package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FootballSession {
    public static int run(String teamKey) {
        StringBuilder result = new StringBuilder();;
        try {
            URL url = new URL("https://s3.eu-west-1.amazonaws.com/hackajob-assets1.p.hackajob/challenges/football_session/football.json");
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

        int score1Sum = 0;
        int score2Sum = 0;
        JsonObject jsonObject = new JsonParser().parse(result.toString()).getAsJsonObject();
        com.google.gson.JsonArray rounds = jsonObject.getAsJsonArray("rounds");
        for(int i = 0; i < rounds.size(); i++) {
            JsonObject round = rounds.get(i).getAsJsonObject();
            JsonArray matches = round.getAsJsonArray("matches");
            for(int j = 0; j < matches.size(); j++) {
                JsonObject match = matches.get(j).getAsJsonObject();
                JsonObject team1 = match.getAsJsonObject("team1");
                if(teamKey.equals(team1.get("key").getAsString())) {
                    score1Sum += match.get("score1").getAsInt();
                }
                JsonObject team2 = match.getAsJsonObject("team2");
                if(teamKey.equals(team2.get("key").getAsString())) {
                    score2Sum += match.get("score2").getAsInt();
                }
            }
        }
        return score1Sum + score2Sum;
    }
}
