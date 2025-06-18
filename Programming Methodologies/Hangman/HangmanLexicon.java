/*
 * File: HangmanLexicon.java
 * -------------------------
 * This file contains a stub implementation of the HangmanLexicon
 * class that you will reimplement for Part III of the assignment.
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import acmx.export.java.io.FileReader;

public class HangmanLexicon {

	private ArrayList<String> arrayList = new ArrayList<String>();

	/*
	 * this method finds the text file, reads it and saves in an array list as
	 * strings. it stops when there are no more lines
	 */
	public HangmanLexicon() {
		BufferedReader rd = null;
		try {
			rd = new BufferedReader(new FileReader("HangmanLexicon.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			while (true) {
				String word = rd.readLine();
				if (word == null)
					break;
				arrayList.add(word);
			}

			rd.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			rd.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Returns the number of words in the array. */
	public int getWordCount() {
		return arrayList.size();
	}

	/** Returns the int index-th word from the list. */
	public String getWord(int index) {

		return arrayList.get(index);
	}
}
