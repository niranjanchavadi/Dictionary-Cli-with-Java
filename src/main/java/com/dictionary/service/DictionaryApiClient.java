package com.dictionary.service;

import com.dictionary.Model.DictionaryEntry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class DictionaryApiClient {
    private static final String DICTIONARY_API_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";

    public DictionaryEntry[] getWordData(String word) throws IOException {
        URL url = new URL(DICTIONARY_API_URL + word);
        System.setProperty("http.agent", "Chrome");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
            System.out.println("Word not found: " + word);
            return null;
        }

        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to fetch data for word: " + word + ". Response code: " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        conn.disconnect();

        Gson gson = new Gson();
        return gson.fromJson(content.toString(), DictionaryEntry[].class);
    }

    public String getRandomWord() throws IOException {
        String randomWordApiUrl = "https://random-word-api.herokuapp.com/word";  // URL for random word API
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(randomWordApiUrl);
            try (CloseableHttpResponse response = client.execute(request)) {
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new IOException("Failed to fetch random word: HTTP error code " + response.getStatusLine().getStatusCode());
                }
                String jsonResponse = EntityUtils.toString(response.getEntity());
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(jsonResponse);
                if (jsonNode.isArray() && jsonNode.size() > 0) {
                    return jsonNode.get(0).asText();
                } else {
                    throw new IOException("Random word not found in the response.");
                }
            }
        }
    }
}
