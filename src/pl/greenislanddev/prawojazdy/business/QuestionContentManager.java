package pl.greenislanddev.prawojazdy.business;

import java.util.HashMap;
import java.util.Map;

import pl.greenislanddev.prawojazdy.R;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

public class QuestionContentManager {

	private static final Map<Integer, Integer> CATEGORIES = new HashMap<Integer, Integer>();
	
	static{
		CATEGORIES.put(R.string.category1, R.string.roman1);
		CATEGORIES.put(R.string.category2, R.string.roman2);
		CATEGORIES.put(R.string.category3, R.string.roman3);
		CATEGORIES.put(R.string.category4, R.string.roman4);
		CATEGORIES.put(R.string.category5, R.string.roman5);
		CATEGORIES.put(R.string.category6, R.string.roman6);
		CATEGORIES.put(R.string.category7, R.string.roman7);
		CATEGORIES.put(R.string.category8, R.string.roman8);
		CATEGORIES.put(R.string.category9, R.string.roman9);
		CATEGORIES.put(R.string.category10, R.string.roman10);
		CATEGORIES.put(R.string.category11, R.string.roman11);
		CATEGORIES.put(R.string.category12, R.string.roman12);
		CATEGORIES.put(R.string.category13, R.string.roman13);
		CATEGORIES.put(R.string.category14, R.string.roman14);
		CATEGORIES.put(R.string.category15, R.string.roman15);
		CATEGORIES.put(R.string.category16, R.string.roman16);
		CATEGORIES.put(R.string.category17, R.string.roman17);
		CATEGORIES.put(R.string.category18, R.string.roman18);
	}
	
	private CheckBox answer1;
	private CheckBox answer2;
	private CheckBox answer3;

	private TableRow row1;
	private TableRow row2;
	private TableRow row3;

	private ImageView imageView;
	private ImageView clockIcon;
	
	private TextView timer;
	private TextView questionText;
	private TextView pageNumberInfo;
	private TextView category;
	
	private ImageButton previous;
	private ImageButton next;
	private ImageButton check;

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
		previous = (ImageButton) activity.findViewById(R.id.previous);
		next = (ImageButton) activity.findViewById(R.id.next);
		timer = (TextView) activity.findViewById(R.id.timer);
		clockIcon = (ImageView)activity.findViewById(R.id.clockIcon);
		category = (TextView)activity.findViewById(R.id.categoryText);
		check = (ImageButton)activity.findViewById(R.id.check_button);
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
	
	public void setCheckListener(OnClickListener listener){
		check.setOnClickListener(listener);
	}

	public void showContent(Question question, String text, String[] answers) {
		resetColors();
		setQuestionText(text);
		setAnswersText(answers);
		setAnswersState(question.getUserAnswers());
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
	
	public void setClockIcon(boolean set){
		if(set){
			clockIcon.setImageResource(R.drawable.clock);
		}else{
			clockIcon.setImageResource(0);
		}
	}
	
	public void showCheckButton(boolean show){
		if(show){
			check.setVisibility(View.VISIBLE);
		}else{
			check.setVisibility(View.GONE);
		}
	}
	
	public void setCategory(Resources res, int resId){
		int value = CATEGORIES.get(Integer.valueOf(resId));
		category.setText(String.format("%s %s", res.getString(R.string.category_chapter),res.getString(value)));
	}
}
