package pl.greenislanddev.prawojazdy.business.sequencer;

import java.io.Serializable;

public interface QuestionsSequencer extends Serializable {

	String ID = "SEQUENCER_KEY";

	Long next();

	Long previous();

	Long getCurrent();

	void setCurrent(Long current);

	void reset();

	int numberOfQuestions();
}
