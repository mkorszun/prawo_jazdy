package pl.greenislanddev.prawojazdy.business;

import pl.greenislanddev.prawojazdy.R;
import android.app.Activity;
import android.graphics.Color;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

public class QuestionContentManager {

	private CheckBox answer1;
	private CheckBox answer2;
	private CheckBox answer3;

	private TableRow row1;
	private TableRow row2;
	private TableRow row3;

	private TextView questionText;
	private ImageView imageView;
	private TextView pageNumberInfo;
	private TextView timer;

	private Button previous;
	private Button next;

	private String textNext;
	private String textEnd;

	public QuestionContentManager(Activity activity) {
		row1 = (TableRow) activity.findViewById(R.id.answerRow1);
		row2 = (TableRow) activity.findViewById(R.id.answerRow2);
		row3 = (TableRow) activity.findViewById(R.id.answerRow3);
		answer1 = (CheckBox) activity.findViewById(R.id.answer1);
		answer2 = (CheckBox) activity.findViewById(R.id.answer2);
		answer3 = (CheckBox) activity.findViewById(R.id.answer3);
		questionText = (TextView) activity.findViewById(R.id.questionText);
		imageView = (ImageView) activity.findViewById(R.id.imageView);
		pageNumberInfo = (TextView) activity.findViewById(R.id.pageNumberText);
		previous = (Button) activity.findViewById(R.id.previous);
		next = (Button) activity.findViewById(R.id.next);
		textNext = activity.getResources().getString(R.string.next_button);
		textEnd = activity.getResources().getString(R.string.next_button_end);
		timer = (TextView) activity.findViewById(R.id.timer);
	}

	public void disable(boolean disable) {
		if (disable) {
			answer1.setEnabled(false);
			answer2.setEnabled(false);
			answer3.setEnabled(false);
		} else {
			answer1.setEnabled(true);
			answer2.setEnabled(true);
			answer3.setEnabled(true);
		}
	}

	public void setColors(boolean[] result) {
		answer1.setTextColor(Color.BLACK);
		answer2.setTextColor(Color.BLACK);
		answer3.setTextColor(Color.BLACK);

		if (result[0]) {
			row1.setBackgroundColor(Color.GREEN);
		} else {
			row1.setBackgroundColor(Color.RED);
		}
		if (result[1]) {
			row2.setBackgroundColor(Color.GREEN);
		} else {
			row2.setBackgroundColor(Color.RED);
		}
		if (result[2]) {
			row3.setBackgroundColor(Color.GREEN);
		} else {
			row3.setBackgroundColor(Color.RED);
		}
	}

	public void showColors(boolean[] result, boolean disable) {
		setColors(result);
		disable(disable);
	}

	public void resetColors() {
		answer1.setTextColor(Color.WHITE);
		answer2.setTextColor(Color.WHITE);
		answer3.setTextColor(Color.WHITE);
		row1.setBackgroundColor(Color.TRANSPARENT);
		row2.setBackgroundColor(Color.TRANSPARENT);
		row3.setBackgroundColor(Color.TRANSPARENT);
	}

	public void setAnswersState(boolean[] answers) {
		this.answer1.setChecked(answers[0]);
		this.answer2.setChecked(answers[1]);
		this.answer3.setChecked(answers[2]);
	}

	public boolean[] getAnswersState() {
		return new boolean[] { answer1.isChecked(), answer2.isChecked(), answer3.isChecked() };
	}

	public void setAnswersText(String[] answers) {
		this.answer1.setText(answers[0]);
		this.answer2.setText(answers[1]);
		this.answer3.setText(answers[2]);
	}

	public void setQuestionText(String text) {
		this.questionText.setText(text);
	}

	public void setImageView(int resId) {
		imageView.setBackgroundResource(resId);
	}

	public void setPageNumber(String pageNumberInfo) {
		this.pageNumberInfo.setText(pageNumberInfo);
	}

	public void setPreviousListener(OnClickListener listener) {
		previous.setOnClickListener(listener);
	}

	public void setNextListener(OnClickListener listener) {
		next.setOnClickListener(listener);
	}

	public void showContent(Question question, String text, String[] answers) {
		resetColors();
		setQuestionText(text);
		setAnswersText(answers);
		setAnswersState(question.getUserAnswers());
	}

	public void setEndButton(boolean end) {
		if (end) {
			next.setText(textEnd);
		} else {
			next.setText(textNext);
		}
	}

	public TextView getTimer() {
		return timer;
	}

	public String getTimerDisplay() {
		return timer.getText().toString();
	}

	public void setTimerDisplay(String display) {
		this.timer.setText(display);
	}
}
