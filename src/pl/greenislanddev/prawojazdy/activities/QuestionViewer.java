package pl.greenislanddev.prawojazdy.activities;

import static pl.greenislanddev.prawojazdy.business.sequencer.sql.tables.QuestionTable.KEY_ANSWER_OPTIONS_ID;
import static pl.greenislanddev.prawojazdy.business.sequencer.sql.tables.QuestionTable.KEY_CORRECT_ANSWER_ID;
import static pl.greenislanddev.prawojazdy.business.sequencer.sql.tables.QuestionTable.KEY_IMAGE_ID;
import static pl.greenislanddev.prawojazdy.business.sequencer.sql.tables.QuestionTable.KEY_QUESTION_ID;

import java.util.Map;

import pl.greenislanddev.prawojazdy.R;
import pl.greenislanddev.prawojazdy.business.Question;
import pl.greenislanddev.prawojazdy.business.QuestionContentManager;
import pl.greenislanddev.prawojazdy.business.TestResult;
import pl.greenislanddev.prawojazdy.business.sequencer.QuestionsSequencer;
import pl.greenislanddev.prawojazdy.business.sequencer.SequencerExtractor;
import pl.greenislanddev.prawojazdy.business.sequencer.sql.DrivingLicenseDbAdapter;
import pl.greenislanddev.prawojazdy.dialogs.DialogExecutor;
import pl.greenislanddev.prawojazdy.dialogs.DialogFactory;
import pl.greenislanddev.prawojazdy.dialogs.DialogFactory.DialogType;
import pl.greenislanddev.prawojazdy.dialogs.IDialog;
import pl.greenislanddev.prawojazdy.dialogs.QuestionPickDialog;
import pl.greenislanddev.prawojazdy.dialogs.ResultDialog;
import pl.greenislanddev.prawojazdy.exam.state.ExamState;
import pl.greenislanddev.prawojazdy.exam.state.ExamStateExtractor;
import pl.greenislanddev.prawojazdy.exam.timer.ExamTimer;
import pl.greenislanddev.prawojazdy.utils.ResourcesUtils;
import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class QuestionViewer extends Activity {

	private static final int OPTION_SHOW_ID = Menu.FIRST;
	private static final int OPTION_GOTO_ID = OPTION_SHOW_ID + 1;

	private static final int QUESTION_PICK_DIALOG_ID = 0;

	private AdView admob;
	private ExamState state;
	private ExamTimer timer;
	private Question actualQuestion;
	private QuestionsSequencer questionsSequencer;
	private QuestionContentManager questionContent;
	private DrivingLicenseDbAdapter dbAdapter;

	@Override
	public void onCreate(Bundle currentState) {
		super.onCreate(currentState);
		setContentView(R.layout.question);
		state = ExamStateExtractor.getState(this, currentState);

		dbAdapter = new DrivingLicenseDbAdapter(this);
		dbAdapter.open();

		questionContent = new QuestionContentManager(this);
		questionContent.setPreviousListener(previousListener);
		questionContent.setNextListener(nextListener);
		questionContent.setTimerDisplay(state.getTimerDisplay());

		// Look up the AdView as a resource and load a request.
		admob = (AdView) findViewById(R.id.admobView);
		AdRequest adRequest = new AdRequest();
		admob.loadAd(adRequest);

		questionsSequencer = SequencerExtractor.getSequencer(this, currentState);
		state.setQuestionsNumber(questionsSequencer.numberOfQuestions());
		fetchQuestion(questionsSequencer.getCurrent());

		if (state.isExam() && !state.isFinished()) {
			timer = createTimer();
			timer.start();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbAdapter.close();
		admob.stopLoading();
		if (state.isExam() && !state.isFinished()) {
			if (timer != null) {
				timer.destroy();
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(ExamState.KEY, state);
		outState.putSerializable(QuestionsSequencer.ID, questionsSequencer);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			saveQuestion(actualQuestion);
			return showEndDialog();
		case KeyEvent.KEYCODE_MENU:
			return showMenu(keyCode, event);
		default:
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, OPTION_SHOW_ID, 0, R.string.show_answer).setIcon(R.drawable.check_icon);
		menu.add(0, OPTION_GOTO_ID, 0, R.string.goto_question).setIcon(R.drawable.goto_icon);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case OPTION_SHOW_ID:
			actualQuestion.setUserAnswers(questionContent.getAnswersState());
			questionContent.showColors(actualQuestion.validate(), false);
			return true;
		case OPTION_GOTO_ID:
			showDialog(QUESTION_PICK_DIALOG_ID);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void fetchQuestion(long rowId) {
		Cursor cursor = dbAdapter.fetchQuestion(rowId);
		int questionId = cursor.getInt(cursor.getColumnIndex(KEY_QUESTION_ID));
		int answersId = cursor.getInt(cursor.getColumnIndex(KEY_CORRECT_ANSWER_ID));
		int optionsId = cursor.getInt(cursor.getColumnIndex(KEY_ANSWER_OPTIONS_ID));
		int imageId = cursor.getInt(cursor.getColumnIndex(KEY_IMAGE_ID));
		cursor.close();

		state.initialize(questionId);
		boolean[] correctAnswers = ResourcesUtils.getBoolArray(getResources(), Question.MAX_ANSWERS, answersId);
		actualQuestion = nextQuestion(questionId);
		actualQuestion.setCorrectAnswers(correctAnswers);

		String text = ResourcesUtils.getString(getResources(), actualQuestion.getId());
		String[] answers = ResourcesUtils.getStringArray(getResources(), Question.MAX_ANSWERS, optionsId);

		showQuestion(actualQuestion, text, answers, imageId);
		state.setQuestionId(questionId);
	}

	private void showQuestion(Question question, String text, String[] answers, int imageId) {
		questionContent.setImageView(imageId);
		questionContent.showContent(actualQuestion, text, answers);
		questionContent.setPageNumber(state.toString());
		questionContent.setEndButton(state.isLastQuestion());
		if (state.isFinished()) {
			questionContent.showColors(question.validate(), true);
		}
	}

	private void saveQuestion(Question question) {
		if (!state.isFinished() && question != null) {
			Map<Integer, Question> answers = state.getAnswersCache();
			actualQuestion.setUserAnswers(questionContent.getAnswersState());
			answers.put(state.getQuestionId(), actualQuestion);
		}
	}

	private Question nextQuestion(int questionId) {
		Map<Integer, Question> answers = state.getAnswersCache();
		if (answers.containsKey(questionId)) {
			return answers.get(questionId);
		} else {
			return new Question(questionId);
		}
	}

	private void showExamSummary() {
		state.setFinished(true);
		state.setPageCounter(1);
		int totalQuestions = getResources().getInteger(R.integer.max_exam_questions);
		TestResult result = new TestResult(state.getAnswers(), totalQuestions);
		new ResultDialog(QuestionViewer.this, result.check()).show();
		questionsSequencer.reset();
		fetchQuestion(questionsSequencer.getCurrent());
	}

	private final OnClickListener previousListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			saveQuestion(actualQuestion);
			if (state.getPageCounter() > 1) {
				state.previousPageNumber();
				fetchQuestion(questionsSequencer.previous());
				questionContent.setEndButton(state.isLastQuestion());
			}
		}
	};

	private final OnClickListener nextListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			saveQuestion(actualQuestion);
			if (state.getPageCounter() < state.getQuestionsNumber()) {
				state.nextPageNumber();
				fetchQuestion(questionsSequencer.next());
			} else {
				showEndDialog();
			}
		}
	};

	private IDialog createDialog(DialogType type) {
		IDialog dialog = DialogFactory.getDialog(type, this);
		switch (type) {
		case EXAM_END_DIALOG:
			dialog.setExecutor(new DialogExecutor() {

				@Override
				public void executePositive() {
					timer.stop();
				}

				@Override
				public void executeNegative() {
					// TODO Auto-generated method stub
				}
			});
		case EXAM_EXIT_DIALOG:
			// Future use
		case TRAINING_END_DIALOG:
			// Future use
		}
		return dialog;
	}

	private boolean showEndDialog() {
		if (!state.isExam()) {
			createDialog(DialogType.TRAINING_END_DIALOG).show();
			return true;
		}

		if (!state.isFinished()) {
			createDialog(DialogType.EXAM_END_DIALOG).show();
			return true;
		} else {
			createDialog(DialogType.EXAM_EXIT_DIALOG).show();
			return true;
		}
	}

	private boolean showMenu(int keyCode, KeyEvent event) {
		if (!state.isExam()) {
			return super.onKeyDown(keyCode, event);
		} else {
			return true;
		}
	}

	private ExamTimer createTimer() {
		return new ExamTimer(state.getExamStartTime(), state.getElapsedTime(), questionContent.getTimer()) {
			@Override
			public void stop() {
				super.destroy();
				state.setTimerDisplay(questionContent.getTimerDisplay());
				showExamSummary();
			}
		};
	}

	private QuestionPickDialog.OnNumberSetListener mNumberSetListener = new QuestionPickDialog.OnNumberSetListener() {

		@Override
		public void onNumberSet(int questionNumber) {
			saveQuestion(actualQuestion);
			questionsSequencer.setCurrent(Long.valueOf(questionNumber));
			state.setPageCounter(questionNumber);
			fetchQuestion(questionsSequencer.getCurrent());
			questionContent.setEndButton(state.isLastQuestion());
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case QUESTION_PICK_DIALOG_ID:
			return new QuestionPickDialog(this, mNumberSetListener);
		}
		return null;
	}
}
