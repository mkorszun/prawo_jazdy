package pl.greenislanddev.prawojazdy.exam.state;

import android.app.Activity;
import android.os.Bundle;

public class ExamStateExtractor {
	public static ExamState getState(Activity activity, Bundle current) {
		if (current != null) {
			return (ExamState) current.getSerializable(ExamState.KEY);
		} else {
			return (ExamState) activity.getIntent().getExtras().getSerializable(ExamState.KEY);
		}
	}
}
