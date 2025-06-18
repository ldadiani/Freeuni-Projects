import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import acm.util.ErrorException;

/*
 * File: NameSurferDataBase.java
 * -----------------------------
 * This class keeps track of the complete database of names.
 * The constructor reads in the database from a file, and
 * the only public method makes it possible to look up a
 * name and get back the corresponding NameSurferEntry.
 * Names are matched independent of case, so that "Eric"
 * and "ERIC" are the same names.
 */

public class NameSurferDataBase implements NameSurferConstants {

	private HashMap<String, NameSurferEntry> nameDataBase = new HashMap<String, NameSurferEntry>();

	/* Constructor: NameSurferDataBase(filename) */
	/**
	 * Creates a new NameSurferDataBase and initializes it using the data in the
	 * specified file. The constructor throws an error exception if the requested
	 * file does not exist or if an error occurs as the file is being read.
	 */
	public NameSurferDataBase(String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				NameSurferEntry entry = new NameSurferEntry(line);
				nameDataBase.put(entry.getName(), entry);
			}
			br.close();
		} catch (IOException e) {
			throw new ErrorException(e);
		}

	}

	/* Method: findEntry(name) */
	/**
	 * Returns the NameSurferEntry associated with this name, if one exists. If the
	 * name does not appear in the database, this method returns null.
	 */
	public NameSurferEntry findEntry(String name) {
		name = writeName(name);
		return nameDataBase.get(name);
	}

	/*
	 * Method: writeName(name)
	 * 
	 * changes the writing of the name
	 */
	private String writeName(String name) {
		char firstLetter = name.charAt(0);
		if (Character.isLowerCase(firstLetter)) {
			firstLetter = Character.toUpperCase(firstLetter);
		}
		String otherLetters = name.substring(1);
		String otherLett = tolower(otherLetters);

		name = firstLetter + otherLett;
		return name;
	}

	/*
	 * Method:tolower(otherletters)
	 * 
	 * lowercases other letters than first
	 */
	private String tolower(String otherLetters) {
		String letters = "";
		for (int i = 0; i < otherLetters.length(); i++) {
			char letter = otherLetters.charAt(i);
			if (letter < 'a' || letter > 'z') {
				letter = Character.toLowerCase(letter);
			}
			letters = letters + letter;
		}
		return letters;
	}
}
