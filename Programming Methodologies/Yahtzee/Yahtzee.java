/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import java.util.ArrayList;
import java.util.Arrays;

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	/* Private instance variables */
	private int TURNS = 3;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();
	private int[][] categories;
	private int[][] scores;
	private int[] dice;
	private int nPlayers;
	private int BONUS_SCORE_BOUND = 63;
	private int BONUS = 35;

	public static void main(String[] args) {
		new Yahtzee().start(args);
	}

	/* this void sets the interface of a game and runs the game */
	public void run() {
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
	}

	/* this void lets the player play the game */
	private void playGame() {
		categories = new int[nPlayers][N_CATEGORIES];
		scores = new int[nPlayers][N_CATEGORIES];
		for (int i = 0; i < N_SCORING_CATEGORIES; i++) {
			RollDice();
		}
		CalculateScore();
		tellWinner();
	}

	/* this void tells to players who is the winner */
	private void tellWinner() {
		int win = 0;
		int winner = 0;
		for (int i = 1; i < nPlayers; i++) {
			if (scores[i][TOTAL - 1] > win) {
				win = scores[i][TOTAL - 1];
				winner = i;
			}
		}
		display.printMessage(
				"Congratulations " + playerNames[winner] + " you're the winner, with a total score of " + win);
	}

	/* this void calculates the bonus, lower score , upper score and total */
	private void CalculateScore() {
		for (int i = 0; i < nPlayers; i++) {
			int upperScore = catlculateUpperScore(i);
			display.updateScorecard(UPPER_SCORE, i + 1, upperScore);
			int bonus = getBonus(upperScore);
			display.updateScorecard(UPPER_BONUS, i + 1, bonus);
			int lowerScore = calculateLowerScore(i);
			display.updateScorecard(LOWER_SCORE, i + 1, lowerScore);
			int total = calculateTotal(i, upperScore, bonus, lowerScore);
			display.updateScorecard(TOTAL, i + 1, total);
		}
	}

	/* this void calculates total score */
	private int calculateTotal(int i, int upperScore, int bonus, int lowerScore) {
		int sum = upperScore + bonus + lowerScore;
		scores[i][TOTAL - 1] = sum;
		return sum;
	}

	/* this void calculates lower score */
	private int calculateLowerScore(int i) {
		int lowscore = 0;
		for (int j = 8; j < LOWER_SCORE; j++) {
			lowscore += scores[i][j];
		}
		scores[i][LOWER_SCORE - 1] = lowscore;
		return lowscore;
	}

	/*
	 * this void gives you a bonus if your upper score is more than
	 * BONUS_SCORE_BOUND
	 */
	private int getBonus(int upperScore) {
		if (upperScore >= BONUS_SCORE_BOUND) {
			return BONUS;
		}
		return 0;
	}

	/* this void calculates upper score */
	private int catlculateUpperScore(int i) {
		int uppscore = 0;
		for (int j = 0; j < UPPER_SCORE; j++) {
			uppscore += scores[i][j];
		}
		scores[i][UPPER_SCORE - 1] = uppscore;
		return uppscore;
	}

	/* this void lets you to roll the dice */
	private void RollDice() {
		dice = new int[N_DICE];
		for (int i = 0; i < nPlayers; i++) {
			display.printMessage(playerNames[i] + "'s turn! Click \"Roll Dice\" button to roll the dice.");
			firstRoll(i);
			OtherRolls(i);
		}
	}

	/*
	 * this void lets you to select a dice and reroll it , also if you have not
	 * chosen dices it lets you to select a category automatically
	 */
	private void OtherRolls(int i) {
		int isDieSelected = 0;
		for (int t = 1; t < TURNS; t++) {
			display.printMessage(playerNames[i] + " select the dice you wish to re-roll and click \"Roll Again\"");
			display.waitForPlayerToSelectDice();
			for (int n = 0; n < N_DICE; n++) {
				if (display.isDieSelected(n)) {
					isDieSelected++;
				}
			}
			if (isDieSelected == 0)
				break;
			for (int index = 0; index < N_DICE; index++) {
				if (display.isDieSelected(index)) {
					dice[index] = rgen.nextInt(1, 6);
				}
			}
			display.displayDice(dice);
			isDieSelected = 0;
		}
		display.printMessage("Select a category for this roll");
		SelectAndSetCategories(i);
	}

	/* this void lets you to select and set a category */
	private void SelectAndSetCategories(int i) {
		int category = display.waitForPlayerToSelectCategory();
		if (categories[i][category - 1] != 1) {
			categories[i][category - 1] = 1;
			int score = getScore(i, category);
			display.updateScorecard(category, i + 1, score);
		} else {
			display.printMessage("This category is already selected, please choose another one.");
			SelectAndSetCategories(i);
		}
	}

	/* this void calculates the score for the selected category */
	private int getScore(int i, int category) {
		int score = 0;
		if (category <= SIXES) {
			score = getScoreForOneToSix(category);
		} else if (category == THREE_OF_A_KIND || category == FOUR_OF_A_KIND) {
			score = getScoreThreeFourOfAKind(category);
		} else if (category == FULL_HOUSE) {
			score = getScoreFullHouse(category);
		} else if (category == SMALL_STRAIGHT || category == LARGE_STRAIGHT) {
			score = getScoreSmallLargeStraght(category);
		} else if (category == YAHTZEE) {
			score = getScoreYahtzee(category);
		} else if (category == CHANCE) {
			score = getScoreChance(category);
		}
		scores[i][category - 1] = score;
		return score;
	}

	/* this void calculates the score for category CHANCE */
	private int getScoreChance(int category) {
		int score = 0;
		for (int i = 0; i < N_DICE; i++) {
			score = score + dice[i];
		}
		return score;
	}

	/* this void calculates the score for category YAHTZEE */
	private int getScoreYahtzee(int category) {
		boolean isYahtzee = true;
		int score = 0;
		for (int i = 1; i < N_DICE; i++) {
			if (dice[i] != dice[i - 1]) {
				isYahtzee = false;
			}
		}
		if (isYahtzee) {
			score = 50;
			return score;
		} else
			return 0;
	}

	/*
	 * this void calculates the score for category SMALL STRAIGHT and LARGE STRAIGHT
	 */
	private int getScoreSmallLargeStraght(int category) {
		Arrays.sort(dice);
		int counter = 0;
		int score = 0;
		for (int j = 1; j <= 6; j++) {
			for (int i = 0; i < N_DICE; i++) {
				if (dice[i] == j) {
					counter++;
				}
			}
			if (category == SMALL_STRAIGHT) {
				if (counter >= 4) {
					score = 30;
					return score;
				}
			} else if (category == LARGE_STRAIGHT) {
				if (counter >= 5) {
					score = 40;
					return score;
				}
			}
		}
		return 0;
	}

	/* this void calculates the score for category FULL HOUSE */
	private int getScoreFullHouse(int category) {
		int[] counter = new int[3];
		int score = 0;
		for (int i = 0; i < N_DICE; i++) {
			counter[0] = 0;
			for (int j = 0; j < N_DICE; j++) {
				if (dice[j] == dice[i]) {
					counter[0]++;
				}
				if (j == 4 && counter[0] == 3) {
					counter[2]++;
				} else if (j == 4 && counter[0] == 2) {
					counter[1]++;
				}
			}
		}
		if (counter[2] == 3 && counter[1] == 2) {
			score = 25;
			return score;
		}
		return 0;
	}

	/*
	 * this void calculates the score for category THREE OT A KIND and FOUR OF A
	 * KIND
	 */
	private int getScoreThreeFourOfAKind(int category) {
		int counter = 0;
		int score = 0;
		for (int i = 0; i < N_DICE; i++) {
			counter = 0;
			for (int j = 0; j < N_DICE; j++) {
				if (dice[j] == dice[i]) {
					counter++;
				}
				if (category == FOUR_OF_A_KIND) {
					if (counter >= 4) {
						for (int n = 0; n < N_DICE; n++) {
							score = score + dice[n];
						}
						return score;
					}
				} else if (category == THREE_OF_A_KIND) {
					if (counter >= 3) {
						for (int n = 0; n < N_DICE; n++) {
							score = score + dice[n];
						}
						return score;
					}
				}
			}
		}
		return 0;
	}

	/* this void calculates the score for categories from 1 to 6 */
	private int getScoreForOneToSix(int category) {
		int score = 0;
		for (int k = ONES; k <= SIXES; k++) {
			if (category == k) {
				for (int i = 0; i < N_DICE; i++) {
					if (dice[i] == k) {
						score = score + k;
					}
				}
				return score;
			}
		}
		return 0;
	}

	/* this void lets you to roll the first time */
	private void firstRoll(int i) {
		for (int j = 0; j < N_DICE; j++) {
			dice[j] = rgen.nextInt(1, 6);
		}
		display.waitForPlayerToClickRoll(i + 1);
		display.displayDice(dice);
	}
}
