package com.dictionary.service;

import com.dictionary.Model.DictionaryEntry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class DictionaryApiClient {

    private static final String DICTIONARY_API_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";
    private static final String RANDOM_WORD_API_URL = "https://random-word-api.herokuapp.com/word";
    private final Gson gson = new Gson();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Fetches word data from the dictionary API
    public DictionaryEntry[] getWordData(String word) throws IOException {
        String url = DICTIONARY_API_URL + word;
        String response = makeHttpGetRequest(url);

        if (response == null) {
            System.out.println("Word not found: " + word);
            return null;
        }

        return gson.fromJson(response, DictionaryEntry[].class);
    }

    // Fetches a random word from the random word API
    public String getRandomWord() throws IOException {
        String response = makeHttpGetRequest(RANDOM_WORD_API_URL);

        if (response != null) {
            JsonNode jsonNode = objectMapper.readTree(response);
            if (jsonNode.isArray() && jsonNode.size() > 0) {
                return jsonNode.get(0).asText();
            }
        }

        throw new IOException("Failed to fetch random word.");
    }

    // Utility method to make HTTP GET requests
    private String makeHttpGetRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        System.setProperty("http.agent", "Chrome");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
            return null;
        }

        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to fetch data from " + urlString + ". Response code: " + responseCode);
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            return content.toString();
        } finally {
            conn.disconnect();
        }
    }
}

