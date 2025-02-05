# Java Wordle Game

A desktop implementation of the popular word-guessing game Wordle, built using Java Swing.

![gameplay](https://github.com/user-attachments/assets/fcb5e2a2-379d-4e3d-bc7b-0bc967a066a1)


## Description

This game is a Java implementation of Wordle where players attempt to guess a 5-letter word within 6 attempts. After each guess, the game provides feedback using colors:
- Green: Letter is correct and in the right position
- Yellow: Letter is in the word but in the wrong position
- Gray: Letter is not in the word

## Features

- Graphical user interface built with Java Swing
- 6 attempts to guess the word
- Color-coded feedback for each guess
- Score tracking system
- High score leaderboard
- Hint system to reveal the word
- Input validation for dictionary words

## Technical Implementation

The game is structured around the following key components:

- `WordleGame` class (extends JFrame): Main game class that handles the UI and game logic
- Word validation using a dictionary file
- Score tracking and persistence using a text file
- Custom UI components:
  - Grid of text fields for displaying guesses
  - Input field for word entry
  - Guess and hint buttons
  - Lives/chances counter

## Dependencies

- Java AWT
- Java Swing
- Java IO (for file operations)

## File Structure

- `dictionary.txt`: Contains the list of valid 5-letter words
- `scores.txt`: Stores player names and their scores

## Setup

1. Ensure you have Java installed on your system
2. Clone the repository
3. Update the file paths in the code to match your system:
   - Dictionary file path
   - Scores file path
4. Compile and run `WordleGame.java`

## Game Controls

- Enter your 5-letter word guess in the text field
- Click "Guess" to submit your answer
- Click "Show word" if you need a hint
- The game automatically tracks your score and displays it when you win or lose

## Score System

- Players start with 6 lives
- The final score is the number of remaining lives when the word is correctly guessed
- A score of 0 is recorded for losses
- Top 3 scores are displayed after each game
