package com.dictionary.service;

import com.dictionary.Model.DictionaryEntry;
import com.dictionary.service.CommandHandler;
import com.dictionary.service.DictionaryApiClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class WordGame {
    private CommandHandler handler ;
    private DictionaryApiClient apiClient ;

    WordGame(){

    }

    public WordGame(DictionaryApiClient apiClient, CommandHandler handler) {
        this.apiClient = apiClient;
        this.handler = handler;
    }

    public void playGame() throws IOException {


        String word = apiClient.getRandomWord();

        DictionaryEntry[] entries = apiClient.getWordData(word);
        if (entries == null || entries.length == 0) {
            System.out.println("Word not found. Please try again later.");
            return;
        }

        DictionaryEntry entry = entries[0];
        List<String> definitions = handler.getDefinitions(entry);
        if (!definitions.isEmpty()) {
            System.out.println("Definition: " + definitions.get(0));
        } else {
            System.out.println("No definitions available for the word.");
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter the word: ");
            String userInput = scanner.nextLine().trim().toLowerCase();
            List<String> synonyms = handler.getSynonyms(entry);
            if (userInput.equals(word) || synonyms.contains(userInput)) {
                System.out.println("Correct! You've guessed the word.");
                break;
            } else {
                System.out.println("Incorrect.");
                System.out.println("1. Try again");
                System.out.println("2. Hint");
                System.out.println("3. Quit");

                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        // User chose to try again
                        continue;
                    case "2":
                        // User chose to get a hint
                        System.out.println("Hint: " + getHint(word, entry));
                        break;
                    case "3":
                        // User chose to quit, reveal the word and dictionary data
                        System.out.println("The word was \"" + word + "\". Full dictionary entry: " + handler.getdisplayFullDict(entry).toString());
                        return; // Exit the game
                    default:
                        // Handle invalid input
                        System.out.println("Invalid option. Please choose 1, 2, or 3.");
                }
            }
        }
    }

    // Method to generate a hint for the word
    private String getHint(String word, DictionaryEntry entry) {
        List<String> hints = new ArrayList<>();

        // Add a shuffled version of the word as a hint
        hints.add("Jumbled word: " + shuffleWord(word));

        // Add available definitions, synonyms, and antonyms as hints
        hints.addAll(handler.getDefinitions(entry));
        hints.addAll(handler.getSynonyms(entry));
        hints.addAll(handler.getAntonyms(entry));

        // Return a random hint from the list
        if (hints.isEmpty()) {
            return "No hints available.";
        } else {
            return hints.get(new Random().nextInt(hints.size()));
        }
    }

    // Method to shuffle the characters of the word for a hint
    private static String shuffleWord(String word) {
        List<Character> characters = new ArrayList<>();
        for (char c : word.toCharArray()) {
            characters.add(c);
        }
        // Shuffle the list of characters
        Collections.shuffle(characters);

        // Rebuild the word from the shuffled characters
        StringBuilder shuffledWord = new StringBuilder();
        for (char c : characters) {
            shuffledWord.append(c);
        }

        return shuffledWord.toString();
    }


    // Setter for apiClient, in case it needs to be set externally
    public void setApiClient(DictionaryApiClient apiClient) {
        this.apiClient = apiClient;
    }
}
