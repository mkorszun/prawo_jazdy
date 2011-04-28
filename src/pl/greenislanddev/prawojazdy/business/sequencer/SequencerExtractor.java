package pl.greenislanddev.prawojazdy.business.sequencer;

import android.app.Activity;
import android.os.Bundle;

public class SequencerExtractor {
	
	public static QuestionsSequencer getSequencer(Activity activity, Bundle current) {
		if (current != null) {
			return (QuestionsSequencer) current
					.getSerializable(QuestionsSequencer.ID);
		} else {
			return (QuestionsSequencer) activity.getIntent().getExtras()
					.getSerializable(QuestionsSequencer.ID);
		}
	}
}
