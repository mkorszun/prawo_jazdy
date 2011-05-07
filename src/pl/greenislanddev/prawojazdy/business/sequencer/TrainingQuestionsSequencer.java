package pl.greenislanddev.prawojazdy.business.sequencer;

public class TrainingQuestionsSequencer implements QuestionsSequencer {

	private static final long serialVersionUID = 3134709162175737587L;

	private Long begin;
	private Long current;
	private int numberOfQuestions;

	public TrainingQuestionsSequencer(int maxQuestions) {
		this(1L, maxQuestions);
	}

	public TrainingQuestionsSequencer(Long begin, int maxQuestions) {
		this.begin = begin;
		this.current = begin;
		this.numberOfQuestions = maxQuestions;
	}

	public Long next() {
		return ++current;
	}

	public Long previous() {
		return --current;
	}

	public Long getCurrent() {
		return current;
	}

	public void setCurrent(Long current) {
		this.current = current;
	}

	public void reset() {
		current = begin;
	}

	public int numberOfQuestions() {
		return numberOfQuestions;
	}
}
