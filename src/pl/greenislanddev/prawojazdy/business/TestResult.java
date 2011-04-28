package pl.greenislanddev.prawojazdy.business;

import java.util.Collection;
import java.util.Iterator;

public class TestResult {

	private final static int ALLOWED_MISTAKES = 2;

	private int mistakes;
	private final int total;

	private Collection<Question> results;

	public TestResult(Collection<Question> res, int total) {
		this.results = res;
		this.total = total;
		this.mistakes = total;
	}

	public TestResult check() {
		Iterator<Question> it = results.iterator();
		while (it.hasNext()) {
			if (it.next().isCorrect()) {
				mistakes--;
			}
		}
		return this;
	}

	public int getCorrect() {
		return total - mistakes;
	}

	public int getTotal() {
		return total;
	}

	public int getMistakes() {
		return mistakes;
	}

	public boolean isExamPassed() {
		return mistakes <= ALLOWED_MISTAKES;
	}

}
