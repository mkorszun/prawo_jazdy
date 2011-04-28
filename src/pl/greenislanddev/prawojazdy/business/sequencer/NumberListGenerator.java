package pl.greenislanddev.prawojazdy.business.sequencer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

class NumberListGenerator {

	/**
	 * Generates a random list of numbers.
	 * 
	 * @param minimum
	 *            A minimum value.
	 * @param maximum
	 *            A maximum value.
	 * @param numberOfEntries
	 *            Number of entries on the list.
	 * @return a list of randomly generated values.
	 */
	public static List<Long> generateNumberSet(int minimum, int maximum, int numberOfEntries) {

		List<Long> results = null;
		if (minimum > 0 && minimum < maximum && maximum - minimum > numberOfEntries) {
			results = new ArrayList<Long>(numberOfEntries);
			Random rand = new Random(new Date().getTime());

			for (int i = 0; i < numberOfEntries;) {
				Long generatedValue = Integer.valueOf(rand.nextInt(maximum - minimum + 1) + minimum).longValue();
				if (!results.contains(generatedValue)) {
					results.add(generatedValue);
					i++;
				}
			}
			return results;
		} else {
			return Collections.emptyList();
		}
	}

}
