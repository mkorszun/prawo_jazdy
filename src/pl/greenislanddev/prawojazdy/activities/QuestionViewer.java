package pl.greenislanddev.prawojazdy.activities;

import static pl.greenislanddev.prawojazdy.business.sql.tables.QuestionTable.KEY_ANSWER_OPTIONS_ID;
import static pl.greenislanddev.prawojazdy.business.sql.tables.QuestionTable.KEY_CORRECT_ANSWER_ID;
import static pl.greenislanddev.prawojazdy.business.sql.tables.QuestionTable.KEY_IMAGE_ID;
import static pl.greenislanddev.prawojazdy.business.sql.tables.QuestionTable.KEY_QUESTION_ID;

import java.util.Map;

import pl.greenislanddev.prawojazdy.R;
import pl.greenislanddev.prawojazdy.business.Question;
import pl.greenislanddev.prawojazdy.business.QuestionContentManager;
import pl.greenislanddev.prawojazdy.business.TestResult;
import pl.greenislanddev.prawojazdy.business.sequencer.QuestionsSequencer;
import pl.greenislanddev.prawojazdy.business.sequencer.SequencerExtractor;
import pl.greenislanddev.prawojazdy.business.sql.DrivingLicenseDbAdapter;
import pl.greenislanddev.prawojazdy.business.sql.tables.QuestionTable;
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
import android.content.Intent;
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
	
	private static final int OPTION_GOTO_ID = Menu.FIRST;
	private static final int OPTION_ABOUT_ID = OPTION_GOTO_ID + 1;
	private static final int OPTION_EXIT_ID = OPTION_ABOUT_ID + 1;

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
		questionContent.setCheckListener(checkListener);
		questionContent.setTimerDisplay(state.getTimerDisplay());
		questionContent.setClockIcon(state.isExam());

		// Look up the AdView as a resource and load a request.
		admob = (AdView) findViewById(R.id.admobView);
		admob.loadAd(new AdRequest());

		questionsSequencer = SequencerExtractor.getSequencer(this, currentState);
		state.setNumberOfQuestions(questionsSequencer.numberOfQuestions());
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
		outState.putSerializable(ExamState.ID, state);
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
		
		if(!state.isExam()){
			menu.add(0, OPTION_GOTO_ID, 0, R.string.goto_question).setIcon(R.drawable.goto_icon);
			menu.add(0, OPTION_ABOUT_ID, 0, R.string.about).setIcon(R.drawable.about_icon);
		}
		menu.add(0, OPTION_EXIT_ID, 0, R.string.option_quit).setIcon(R.drawable.exit_icon);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case OPTION_ABOUT_ID:
			//TODO
			return true;
		case OPTION_GOTO_ID:
			showDialog(QUESTION_PICK_DIALOG_ID);
			return true;
		case OPTION_EXIT_ID:
			Intent i = new Intent(this, DrivingLicense.class); 
			this.startActivity(i); 
			finish(); 
		}
		return super.onOptionsItemSelected(item);
	}

	public void fetchQuestion(long rowId) {
		Cursor cursor = dbAdapter.fetchQuestion(rowId);
		int questionId = cursor.getInt(cursor.getColumnIndex(KEY_QUESTION_ID));
		int answersId = cursor.getInt(cursor.getColumnIndex(KEY_CORRECT_ANSWER_ID));
		int optionsId = cursor.getInt(cursor.getColumnIndex(KEY_ANSWER_OPTIONS_ID));
		int imageId = cursor.getInt(cursor.getColumnIndex(KEY_IMAGE_ID));
		int categoryId = cursor.getInt(cursor.getColumnIndex(QuestionTable.KEY_CATEGORY_ID));
		cursor.close();

		state.initialize(questionId);
		boolean[] correctAnswers = ResourcesUtils.getBoolArray(getResources(), Question.MAX_ANSWERS, answersId);
		actualQuestion = nextQuestion(questionId);
		actualQuestion.setCorrectAnswers(correctAnswers);

		String text = ResourcesUtils.getString(getResources(), actualQuestion.getId());
		String[] answers = ResourcesUtils.getStringArray(getResources(), Question.MAX_ANSWERS, optionsId);

		showQuestion(actualQuestion, text, answers, imageId, categoryId);
		state.setQuestionId(questionId);
	}

	private void showQuestion(Question question, String text, String[] answers, int imageId, int categoryId) {
		questionContent.setImageView(imageId);
		questionContent.showContent(actualQuestion, text, answers);
		questionContent.setPageNumber(state.toString());
		questionContent.setCategory(getResources(), categoryId);
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
			}
		}
	};

	private final OnClickListener nextListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			saveQuestion(actualQuestion);
			if (state.getPageCounter() < state.getNumberOfQuestions()) {
				state.nextPageNumber();
				fetchQuestion(questionsSequencer.next());
			} else {
				showEndDialog();
			}
		}
	};
	
	private final OnClickListener checkListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			if(!state.isExam()){
				actualQuestion.setUserAnswers(questionContent.getAnswersState());
				questionContent.showColors(actualQuestion.validate(), false);			
			}else{
				if(!state.isFinished()){
					showEndDialog();
				}
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
					// Auto-generated method stub
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
		return super.onKeyDown(keyCode, event);
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
