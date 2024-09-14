package com.dictionary.service;
import com.dictionary.Model.Definition;
import com.dictionary.Model.DictionaryEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommandHandler {
    private final WordGame wordGame;
    private final DictionaryApiClient apiClient;

    @Autowired
    public CommandHandler(@Lazy WordGame wordGame, DictionaryApiClient apiClient) {
        this.wordGame = wordGame;
        this.apiClient = apiClient;
    }


    public void handleCommand(String[] args) throws IOException {
        String command;
        String word = null;

        if (args.length == 0) {
            command = "full";
            word = apiClient.getRandomWord();
            System.out.println("Word of the Day: " + word);
        } else if (args.length == 1) {
            command = args[0];
            if (!"play".equals(command)) {
                word = command;
                command = "full";
            }
        } else {
            command = args[0];
            word = args[1];
        }

        System.out.println("Command: " + command + ", Word: " + word);

        if ("play".equals(command)) {
            wordGame.playGame();
        } else {
            DictionaryEntry[] entries = apiClient.getWordData(word);
            if (entries.length == 0) {
                System.out.println("Word not found.");
                return;
            }

            DictionaryEntry entry = entries[0];
            processCommand(command, entry);
        }
    }

    private void processCommand(String command, DictionaryEntry entry) {
        switch (command) {
            case "def":
                displayInformation(entry, this::getDefinitions, "Definitions");
                break;
            case "syn":
                displayInformation(entry, this::getSynonyms, "Synonyms");
                break;
            case "ant":
                displayInformation(entry, this::getAntonyms, "Antonyms");
                break;
            case "ex":
                displayInformation(entry, this::getExamples, "Examples");
                break;
            case "full":
                displayFullDict(entry);
                break;
            default:
                System.out.println("Unknown command: " + command);
        }
    }

    private void displayInformation(DictionaryEntry entry, InfoExtractor extractor, String label) {
        List<String> info = extractor.extract(entry);
        System.out.println(label + " for " + entry.getWord() + ":");
        info.forEach(item -> System.out.println("- " + item));
    }

    public void displayFullDict(DictionaryEntry entry) {
        displayInformation(entry, this::getDefinitions, "Definitions");
        displayInformation(entry, this::getSynonyms, "Synonyms");
        displayInformation(entry, this::getAntonyms, "Antonyms");
        displayInformation(entry, this::getExamples, "Examples");
    }

    // Extractors

    public List<String> getDefinitions(DictionaryEntry entry) {
        return entry.getMeanings().stream()
                .flatMap(meaning -> meaning.getDefinitions().stream())
                .map(Definition::getDefinition)
                .collect(Collectors.toList());
    }

    public List<String> getSynonyms(DictionaryEntry entry) {
        return entry.getMeanings().stream()
                .flatMap(meaning -> meaning.getSynonyms().stream())
                .filter(synonym -> !synonym.isEmpty())
                .collect(Collectors.toList());
    }

    public List<String> getAntonyms(DictionaryEntry entry) {
        return entry.getMeanings().stream()
                .flatMap(meaning -> meaning.getAntonyms().stream())
                .filter(antonym -> !antonym.isEmpty())
                .collect(Collectors.toList());
    }

    private List<String> getExamples(DictionaryEntry entry) {
        return entry.getMeanings().stream()
                .flatMap(meaning -> meaning.getDefinitions().stream())
                .map(Definition::getExample)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @FunctionalInterface
    private interface InfoExtractor {
        List<String> extract(DictionaryEntry entry);
    }
}


