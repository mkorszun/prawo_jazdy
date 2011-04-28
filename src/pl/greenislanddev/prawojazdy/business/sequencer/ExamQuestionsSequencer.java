package pl.greenislanddev.prawojazdy.business.sequencer;

import java.util.List;

public class ExamQuestionsSequencer implements QuestionsSequencer {

	private static final long serialVersionUID = 8140364495538603841L;

	private List<Long> questionIds;
	private int index;
	private int numberOfQuestions;

	public ExamQuestionsSequencer(int maxQuestions, int maxExamQuestions) {
		this.questionIds = NumberListGenerator.generateNumberSet(1, maxQuestions, maxExamQuestions);
		this.numberOfQuestions = maxExamQuestions;
		this.index = 0;
	}

	public Long next() {
		if (index < questionIds.size() - 1) {
			index++;
		}
		return questionIds.get(index);
	}

	public Long previous() {
		index = index == 0 ? index : index - 1;
		return questionIds.get(index);
	}

	public Long getCurrent() {
		return questionIds.get(index);
	}

	public void setCurrent(Long current) {
		throw new UnsupportedOperationException();
	}

	public void reset() {
		index = 0;
	}

	public int numberOfQuestions() {
		return numberOfQuestions;
	}
}
