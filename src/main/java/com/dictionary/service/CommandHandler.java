package com.dictionary.service;

import com.dictionary.Model.Definition;
import com.dictionary.Model.DictionaryEntry;
import com.dictionary.Model.Meaning;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommandHandler {

    private DictionaryApiClient apiClient ;

    private WordGame wordGame;

    CommandHandler(){}

    public CommandHandler(WordGame wordGame, DictionaryApiClient apiClient ) {
        this.wordGame = wordGame;
        this.apiClient = apiClient;
    }

    public void handleCommand(String[] args) throws IOException {
        String command;
        String word = null;

        // Check if no command or word is provided
        if (args.length == 0) {
            command = "full";
            word = apiClient.getRandomWord(); // Fetch a random word from the API
            System.out.println("Word of the Day: " + word);
        } else if (args.length == 1) {
            command = args[0]; // The argument is either a command or word
            if (!"play".equals(command)) {
                word = command; // If it's not play, assume it's the word
                command = "full"; // Treat it as full dict
            }
        } else {
            command = args[0]; // First argument is the command
            word = args[1]; // Second argument is the word
        }

        // Log the command and word for debugging
        System.out.println("Command: " + command + ", Word: " + word);

        if ("play".equals(command)) {
            wordGame.playGame();
        } else {
            // Fetch word data from the API
            DictionaryEntry[] entries = apiClient.getWordData(word);
            if (entries.length == 0) {
                System.out.println("Word not found.");
                return;
            }

            // Get the first dictionary entry
            DictionaryEntry entry = entries[0];

            // Handle other commands
            switch (command) {
                case "def":
                    displayDefinitions(entry);
                    break;
                case "syn":
                    displaySynonyms(entry);
                    break;
                case "ant":
                    displayAntonyms(entry);
                    break;
                case "ex":
                    displayExamples(entry);
                    break;
                case "full":
                    displayFullDict(entry);
                    break;
                default:
                    System.out.println("Unknown command: " + command);
                    break;
            }
        }
    }

    private void displayDefinitions(DictionaryEntry entry) {
        System.out.println("Definitions for " + entry.getWord() + ":");
        for (Meaning meaning : entry.getMeanings()) {
            for (Definition definition : meaning.getDefinitions()) {
                System.out.println("-" + definition.getDefinition());
            }
        }
    }

    private void displaySynonyms(DictionaryEntry entry) {
        System.out.println("Synonyms for " + entry.getWord() + ":");
        for (Meaning meaning : entry.getMeanings()) {
            for (String synonym : meaning.getSynonyms()) {
                if (!synonym.isEmpty()) {
                    System.out.println("-" + String.join(", ", synonym));

                }
            }
        }
    }


    private void displayAntonyms(DictionaryEntry entry) {
        System.out.println("Antonyms for " + entry.getWord() + ":");
        for (Meaning meaning : entry.getMeanings()) {
            for (String antonym : meaning.getAntonyms()) {
                if (!antonym.isEmpty()) {
                    System.out.println("-" + String.join(",", antonym));

                }
            }
        }
    }

    private void displayExamples(DictionaryEntry entry) {
        System.out.println("Examples for " + entry.getWord() + ":");
        for (Meaning meaning : entry.getMeanings()) {
            for (Definition definition : meaning.getDefinitions()) {
                if (definition.getExample() != null) {
                    System.out.println("-" + definition.getExample());

                }
            }
        }
    }

    public void displayFullDict(DictionaryEntry entry) {
        displayDefinitions(entry);
        displaySynonyms(entry);
        displayAntonyms(entry);
        displayExamples(entry);

    }

    public List<List<String>> getdisplayFullDict(DictionaryEntry entry) {
       List<List<String>>  list = new ArrayList<>();
        list.add(getDefinitions(entry));
        list.add(getSynonyms(entry));
        list.add(getAntonyms(entry));
     return list;
    }


    //Method to get Antonyms from a DictionaryEntry
    public List<String> getAntonyms(DictionaryEntry entry) {
        System.out.println("Antonyms for " + entry.getWord() + ":");
        List<String> antonymList = new ArrayList<>();
        // Iterate over the meanings of the entry
        for (Meaning meaning : entry.getMeanings()) {
            for (String antonym : meaning.getAntonyms()) {
                // Add non-empty antonyms to the list
                if (!antonym.isEmpty()) {
                    antonymList.add(antonym);
                }
            }
        }
        return antonymList;
    }

    // Method to get Synonyms from a DictionaryEntry
    public List<String> getSynonyms(DictionaryEntry entry) {
        System.out.println("Synonyms for " + entry.getWord() + ":");
        List<String> synonymList = new ArrayList<>();
        for (Meaning meaning : entry.getMeanings()) {
            for (String synonym : meaning.getSynonyms()) {
                if (!synonym.isEmpty()) {
                    System.out.println("- " + synonym);
                    synonymList.add(synonym);
                }
            }
        }
        return synonymList;
    }

    public List<String> getDefinitions(DictionaryEntry entry) {
        List<String> defList = new ArrayList<>();
        for (Meaning meaning : entry.getMeanings()) {
            for (Definition definition : meaning.getDefinitions()) {
                // Add the definition to the list
                defList.add(definition.getDefinition());
            }
        }
        return defList;
    }


}


