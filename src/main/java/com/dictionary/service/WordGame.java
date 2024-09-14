package com.dictionary.service;
import com.dictionary.Model.DictionaryEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class WordGame {

    private final DictionaryApiClient apiClient;
    private CommandHandler handler;

    @Autowired
    public WordGame(DictionaryApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Autowired
    public void setCommandHandler(CommandHandler commandHandler) {
        this.handler = commandHandler;
    }

    // Main game logic
    public void playGame() throws IOException {
        String word = apiClient.getRandomWord();
        DictionaryEntry[] entries = apiClient.getWordData(word);

        if (entries == null || entries.length == 0) {
            System.out.println("Word not found. Please try again later.");
            return;
        }

        DictionaryEntry entry = entries[0];
        Optional<String> definitionOpt = handler.getDefinitions(entry).stream().findFirst();

        if (definitionOpt.isPresent()) {
            System.out.println("Definition: " + definitionOpt.get());
        } else {
            System.out.println("No definitions available for the word.");
        }

        playGameLoop(word, entry);
    }

    private void playGameLoop(String word, DictionaryEntry entry) {
        Scanner scanner = new Scanner(System.in);
        boolean isGameOver = false;  // Flag to stop the game when the user chooses "quit"

        while (!isGameOver) {  // Continue until user chooses to quit
            System.out.print("Enter the word: ");
            String userInput = scanner.nextLine().trim().toLowerCase();

            if (isCorrectGuess(word, userInput, entry)) {
                System.out.println("Correct! You've guessed the word.");
                break;
            } else {
                System.out.println("Incorrect.");
                isGameOver = showOptions(scanner, word, entry);  // Pass the result of user's choice
            }
        }
    }


    // Checks if the user's input is correct (either the word or a synonym)
    private boolean isCorrectGuess(String word, String userInput, DictionaryEntry entry) {
        return userInput.equals(word) || handler.getSynonyms(entry).contains(userInput);
    }

    private boolean showOptions(Scanner scanner, String word, DictionaryEntry entry) {
        System.out.println("1. Try again");
        System.out.println("2. Hint");
        System.out.println("3. Quit");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                // User chose to try again
                return false;  // Continue the game
            case "2":
                // User chose to get a hint
                System.out.println("Hint: " + getHint(word, entry));
                return false;  // Continue the game
            case "3":
                // User chose to quit
                System.out.println("The word was \"" + word + "\".");
                handler.displayFullDict(entry);
                return true;  // End the game
            default:
                // Handle invalid input
                System.out.println("Invalid option. Please choose 1, 2, or 3.");
                return false;  // Continue the game if invalid option
        }
    }

    // Generates a hint by shuffling the word or giving definitions, synonyms, or antonyms
    private String getHint(String word, DictionaryEntry entry) {
        List<String> hints = new ArrayList<>(handler.getDefinitions(entry));
        hints.addAll(handler.getSynonyms(entry));
        hints.addAll(handler.getAntonyms(entry));

        if (hints.isEmpty()) {
            return "No hints available.";
        }

        // Adding a jumbled version of the word as a hint
        hints.add("Jumbled word: " + shuffleWord(word));

        return hints.get(new Random().nextInt(hints.size()));
    }

    // Shuffles the characters of the word for a hint
    private String shuffleWord(String word) {
        List<Character> characters = word.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(characters);
        return characters.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}
