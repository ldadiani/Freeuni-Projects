/*
`	 * File: NameSurferEntry.java
 * --------------------------
 * This class represents a single entry in the database.  Each
 * NameSurferEntry contains a name and a list giving the popularity
 * of that name for each decade stretching back to 1900.
 */

import acm.util.*;
import java.util.*;

public class NameSurferEntry implements NameSurferConstants {
	private String name = "";
	private int decadeMassive[] = new int[NDECADES];

	/* Constructor: NameSurferEntry(line) */
	/**
	 * Creates a new NameSurferEntry from a data line as it appears
	 * in the data file.  Each line begins with the name, which is
	 * followed by integers giving the rank of that name for each
	 * decade.
	 */
	public NameSurferEntry(String line) {
		StringTokenizer st = new StringTokenizer(line);
		name = st.nextToken();
		fillDecades(st, line);
	}

	/*
	 * this method fills the massive with the numbers(numbers that are given in the
	 * names-data.txt as the statistic of name usage)
	 */
	private void fillDecades(StringTokenizer st, String line) {
		int i = 0;
		while (st.hasMoreTokens()) {
			decadeMassive[i] = Integer.parseInt(st.nextToken());
			i++;
		}

	}
	/* Method: getName() */
	/**
	 * Returns the name associated with this entry.
	 */

	public String getName() {
		return name;
	}

	/* Method: getRank(decade) */
	/**
	 * Returns the rank associated with an entry for a particular
	 * decade.  The decade value is an integer indicating how many
	 * decades have passed since the first year in the database,
	 * which is given by the constant START_DECADE.  If a name does
	 * not appear in a decade, the rank value is 0.
	 */
	public int getRank(int decade) {
		return decadeMassive[decade];
	}

	/* Method: toString() */
	/**
	 * Returns a string that makes it easy to see the value of a
	 * NameSurferEntry.
	 */
	public String toString() {
		return (name + " " + Arrays.toString(decadeMassive));
	}
}
