package pl.greenislanddev.prawojazdy.business.sequencer;

public class TrainingQuestionsSequencer implements QuestionsSequencer {

	private static final long serialVersionUID = 3134709162175737587L;

	private Long offset;
	private int numberOfQuestions;

	public TrainingQuestionsSequencer(int maxQuestions) {
		this.offset = 1L;
		this.numberOfQuestions = maxQuestions;
	}

	public Long next() {
		return ++offset;
	}

	public Long previous() {
		return --offset;
	}

	public Long getCurrent() {
		return offset;
	}

	public void setCurrent(Long current) {
		offset = current;
	}

	public void reset() {
		offset = 1L;
	}

	public int numberOfQuestions() {
		return numberOfQuestions;
	}
}
