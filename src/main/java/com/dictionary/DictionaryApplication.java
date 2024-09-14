package com.dictionary;

import com.dictionary.service.CommandHandler;
import com.dictionary.service.DictionaryApiClient;
import com.dictionary.service.WordGame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.IOException;

@SpringBootApplication
public class DictionaryApplication {


    public static void main(String[] args) {
        SpringApplication.run(DictionaryApplication.class, args);



        DictionaryApiClient apiClient = new DictionaryApiClient(); // Assuming ApiClient has a no-arg constructor
        WordGame wordGame = new WordGame(apiClient, new CommandHandler()); // Pass dependencies to WordGame

        // Create the CommandHandler with dependencies
        CommandHandler handler = new CommandHandler(wordGame,apiClient);

    try {
        handler.handleCommand(args);
    }
catch (IOException e){
       e.printStackTrace();
    }


}
}
