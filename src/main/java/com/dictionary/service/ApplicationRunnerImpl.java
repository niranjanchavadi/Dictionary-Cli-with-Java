package com.dictionary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class ApplicationRunnerImpl implements CommandLineRunner {

    private final CommandHandler commandHandler;

    @Autowired
    public ApplicationRunnerImpl(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void run(String... args) throws IOException {
        commandHandler.handleCommand(args);
    }
}
