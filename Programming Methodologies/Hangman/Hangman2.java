/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game from
 * Assignment #4.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;

public class Hangman2 extends ConsoleProgram {
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private HangmanLexicon lexicon = new HangmanLexicon();
	private HangmanCoolCanvas canvas;

	public void init() {
		canvas = new HangmanCoolCanvas();
		add(canvas);
	}

	// void run runs the program
	public void run() {
		String incorrectGuess = "";
		String nDash = "";
		String dashes = "";
		String guess = "";
		char letter = 'g';
		boolean guessedOrNot = false;
		int incorrect = 0;
		char guessChar = 'g';
		boolean guessed = false;
		int num = 0;
		int which = 0;
		String save = "";
		String replaced = "";
		String saveIncorrect = "";
		String incorrectNotes = "";

		String randWord = chooseWord();
		String randWordDashes = dashes(randWord, nDash, dashes);
		canvas.reset(randWordDashes);
		println(randWordDashes);
		startGuessing(incorrectNotes, saveIncorrect, replaced, save, which, num, guessed, guessChar, incorrect,
				incorrectGuess, guessedOrNot, randWord, randWordDashes, guess, letter);
	}

	
	/*
	 * this void defines if your guess is correct , if it is it shows result on the
	 * canvas
	 */
	private void startGuessing(String incorrectNotes, String saveIncorrect, String replaced, String save, int which,
			int num, boolean guessed, char guessChar, int incorrect, String incorrectGuess, boolean guessedOrNot,
			String randWord, String randWordDashes, String guess, char letter) {
		int count = 0;
		String replacedDashes = "";

		// count is how many tries you failed
		while (count != 8) {
			guess = readLine("Your guess: ");
			guessChar = input(guess, letter);
			guessed = checkIfGuessed(num, guessChar, randWord, randWordDashes, replacedDashes, guessedOrNot);

			// also if your guess is true you will see replaced letters on the canvas too
			if (guessed == true) {
				save = replacedDashes;
				replacedDashes = updateGame(count, save, replaced, num, which, randWord, guessChar, randWordDashes);
				if (replacedDashes.equals(randWord)) {
					break;
				}
			} else if (guess.length() == 1 && (guessChar >= 'A' && guessChar <= 'Z')) {
				count++;
				saveIncorrect = notGuessed(saveIncorrect, guessed, count, incorrect, incorrectGuess, guessChar);
			}
			// if you put incorrect letter 8 times you lost the game
			if (count == 8) {
				loseGame(randWord);
			}
		}
	}

	
	/*
	 * if you did not guess the letter String of incorrect letters will receive new
	 * letter(if you put the incorrect letter more than once it will not be written
	 * in the string)
	 */
	private String notGuessed(String saveIncorrect, boolean guessed, int count, int incorrect, String incorrectGuess,
			char guessChar) {
		for (int n = saveIncorrect.length() - 1; n >= 0; n--) {
			if (saveIncorrect.charAt(n) == guessChar) {
				incorrect++;
			}
		}
		if (incorrect == 0) {
			incorrectGuess = saveIncorrect + guessChar;
		}
		if (incorrect != 0) {
			incorrectGuess = saveIncorrect;
			incorrect = 0;
		}
		//if your input is incorrect you will lose points
		canvas.losePoints();
		canvas.noteIncorrectGuess(incorrectGuess, count);
		println("There are no " + guessChar + "'s in the word.");
		guessed = false;
		println("You have " + (8 - count) + " guesses left.");
		println(incorrectGuess);
		return incorrectGuess;
	}

	
	/*
	 * if you guessed the letter the game will show result, how your word looks like
	 * now.
	 */
	private String updateGame(int count, String save, String replaced, int num, int which, String randWord,
			char guessChar, String randWordDashes) {
		for (int i = 0; i < randWordDashes.length(); i++) {
			if (randWord.charAt(i) == guessChar) {
				//if your guess is true you will receive point per one letter
				canvas.addPoints();
				num = i;
				replaced = replaceDashes(save, num, randWord, guessChar);
				save = replaced;
				canvas.displayWord(replaced);
			}
		}
		println("Your guess is true");
		println("You have " + (8 - count) + " guesses left.");
		if (replaced.equals(randWord)) {
			winGame(randWord);
		} else {
			println("The word now looks like this: " + replaced);
		}
		return replaced;
	}

	/*
	 * boolean checkIfGuessed tells you if you guessed the letter or not
	 */
	private boolean checkIfGuessed(int num, char guessChar, String randWord, String randWordDashes,
			String replacedDashes, boolean guessedOrNot) {
		for (int i = 0; i < randWordDashes.length(); i++) {
			if (randWord.charAt(i) == guessChar) {
				guessedOrNot = true;
			}
		}
		return guessedOrNot;
	}

	/*
	 * char input saves the letter you wrote, no matter you put small letter or big,
	 * ,also if you put something else than one letter it will tell you that your
	 * input is incorrect
	 */
	private char input(String guess, char letter) {
		letter = guess.charAt(0);
		if (guess.length() == 1 && letter >= 'a' && letter <= 'z') {
			letter = (char) (letter - 32);
		} else if (guess.length() != 1 || (letter < 'A' || letter > 'Z')) {
			println("Your input is incorrent, you have to enter a letter!");
		}
		return letter;
	}

	// this void tells you that you won the game
	private void winGame(String randWord) {
		println("You guessed the word: " + randWord);
		println("you win!");
	}

	// this void tells you that you lost the game
	private void loseGame(String randWord) {
		println("You are completely hung");
		println("The word was " + randWord);
		println("You lose :(");
	}

	// this string replaces the dashes with letters you guessed
	private String replaceDashes(String save, int num, String randWord, char guessChar) {
		String newDashes = "";
		char symbol = 's';
		for (int i = 0; i < randWord.length(); i++) {
			if (i == num) {
				symbol = randWord.charAt(num);
			} else {
				if (save.length() != 0) {
					symbol = save.charAt(i);
				} else {
					symbol = '-';
				}
			}
			newDashes = newDashes + symbol;
		}
		return newDashes;
	}

	// this string chooses a random word from a lexicon
	private String chooseWord() {
		int n = rgen.nextInt(0, lexicon.getWordCount());
		String word = lexicon.getWord(n);
		return word;
	}

	// this string chooses a random word from a lexicon
	private String dashes(String randWord, String nDash, String dashes) {
		int nchars = randWord.length();
		dashes = printDashes(nDash, nchars);
		return dashes;
	}

	// this void prints the dashes
	private String printDashes(String nDash, int nchars) {
		for (int i = 0; i < nchars; i++) {
			nDash = nDash + "-";
		}
		return nDash;
	}
}