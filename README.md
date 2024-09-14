# Dictionary-Cli-with-Java

## Overview
This command-line dictionary tool allows users to retrieve word definitions, synonyms, antonyms, and examples directly from the command line. It also includes a word guessing game and a Word of the Day feature, powered by an external dictionary API.

## Features
1. **Word Definitions**: Displays the definition of a word.


2. **Word Synonyms**: Displays the synonyms of a word.


3. **Word Antonyms**: Displays the antonyms of a word.


4. **Word Examples**: Displays example sentences using the word.


5. **Full Dictionary**: Displays definitions, synonyms, antonyms, and examples for a given word.


6. **Word of the Day**: Fetches a random word and displays its full dictionary information.


7. **Word Guessing Game**: A word guessing game where the user has to guess a word based on its definition, synonyms, or antonyms.




## Prerequisites
- **Java 8** or higher
- **Gradle** (for building the project)
- **Internet connection** (for making API calls)

## Setup

1. **Clone the repository**:
```bash
git clone https://github.com/niranjanchavadi/Dictionary-Cli-with-Java.git

2 create jar
 ./gradlew bootJar

3 Than
cd build/libs

Here is the theoretical description for each command:

**Word Definitions**
Fetch and display the definitions of the word provided as input.
java -jar dictionary-0.0.1-SNAPSHOT.jar def <word>

**Word Synonyms**
Display a list of synonyms for the given word to understand its alternative meanings.
java -jar dictionary-0.0.1-SNAPSHOT.jar syn <word>

**Word Antonyms**
Display the antonyms of the given word, showing opposite meanings.
java -jar dictionary-0.0.1-SNAPSHOT.jar ant <word>

**Word Examples**
Provide usage examples of the word in sentences to give context.
java -jar dictionary-0.0.1-SNAPSHOT.jar ex <word>

**Word Full Dict**
Display comprehensive information about the word, including definitions, synonyms, antonyms, and examples.
java -jar dictionary-0.0.1-SNAPSHOT.jar <word>

**Word of the Day Full Dict**
Use the API to fetch and display all the information (definitions, synonyms, antonyms, examples) for a randomly chosen word.
java -jar dictionary-0.0.1-SNAPSHOT.jar

**Word Game**
Engage in an interactive game where the user guesses a word based on definitions, synonyms, or antonyms, with options for hints and retries.
java -jar dictionary-0.0.1-SNAPSHOT.jar play

Each command provides a specific feature of the dictionary, ranging from displaying specific word data to engaging in an interactive word-guessing game.
