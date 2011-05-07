package pl.greenislanddev.prawojazdy.activities;

import pl.greenislanddev.prawojazdy.R;
import pl.greenislanddev.prawojazdy.business.sequencer.ExamQuestionsSequencer;
import pl.greenislanddev.prawojazdy.business.sequencer.QuestionsSequencer;
import pl.greenislanddev.prawojazdy.exam.state.ExamState;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class DrivingLicense extends Activity {

	private static final int OPTION_EXERCISE_ID = Menu.FIRST;
	private static final int OPTION_EXAM_ID = OPTION_EXERCISE_ID + 1;
	private static final int OPTION_QUIT_ID = OPTION_EXAM_ID + 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, OPTION_EXERCISE_ID, 0, R.string.option_exercise).setIcon(R.drawable.train_icon);
		menu.add(0, OPTION_EXAM_ID, 0, R.string.option_exam).setIcon(R.drawable.exam_icon);
		menu.add(0, OPTION_QUIT_ID, 0, R.string.option_quit).setIcon(R.drawable.exit_icon);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent myIntent;
		QuestionsSequencer sequencer = null;

		ExamState state = new ExamState();
		state.setExamStartTime(System.currentTimeMillis());

		Resources res = getResources();
		int maxQuestions = res.getInteger(R.integer.max_questions);
		int maxExamQuestions = res.getInteger(R.integer.max_exam_questions);

		switch (item.getItemId()) {
		case OPTION_EXERCISE_ID:
			state.setExam(false);
			myIntent = new Intent(this, CategoryList.class);
			// sequencer = new TrainingQuestionsSequencer(maxQuestions);
			// myIntent.putExtra(QuestionsSequencer.ID, sequencer);
			// myIntent.putExtra(ExamState.ID, state);
			startActivityForResult(myIntent, 0);
			return true;

		case OPTION_EXAM_ID:
			state.setExam(true);
			myIntent = new Intent(this, QuestionViewer.class);
			sequencer = new ExamQuestionsSequencer(maxQuestions, maxExamQuestions);
			myIntent.putExtra(QuestionsSequencer.ID, sequencer);
			myIntent.putExtra(ExamState.ID, state);
			startActivityForResult(myIntent, 0);
			return true;
		case OPTION_QUIT_ID:
			finish();
			return false;
		}
		return super.onOptionsItemSelected(item);
	}
}