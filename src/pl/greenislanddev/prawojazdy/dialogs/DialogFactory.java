package pl.greenislanddev.prawojazdy.dialogs;

import android.app.Activity;

public class DialogFactory {

	public enum DialogType {
		EXAM_END_DIALOG, EXAM_EXIT_DIALOG, TRAINING_END_DIALOG
	}

	public static IDialog getDialog(DialogType type, Activity activity) {
		switch (type) {
		case EXAM_END_DIALOG:
			return new ExamEndDialog(activity);
		case EXAM_EXIT_DIALOG:
			return new ExamExitDialog(activity);
		case TRAINING_END_DIALOG:
			return new TrainingEndDialog(activity);
		default:
			throw new IllegalArgumentException();
		}
	}
}
