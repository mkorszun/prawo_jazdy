package pl.greenislanddev.prawojazdy.dialogs;

import pl.greenislanddev.prawojazdy.R;
import pl.greenislanddev.prawojazdy.business.TestResult;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultDialog {

	private Context mContext;
	private TestResult mTestResult;

	private AlertDialog.Builder builder;
	private AlertDialog alertDialog;

	private ImageView mImage;
	private TextView mQuestionsCount;
	private TextView mCorrectAnswersCount;
	private TextView mMistakesCount;
	private TextView mExamInfo;

	public ResultDialog(Activity parent, TestResult result) {
		this.mContext = parent;
		this.mTestResult = result;

		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.exam_summary, (ViewGroup) parent.findViewById(R.id.summary_exam_layout));

		mImage = (ImageView) layout.findViewById(R.id.summary_header_image);
		mCorrectAnswersCount = (TextView) layout.findViewById(R.id.summary_correct_answers_count);
		mMistakesCount = (TextView) layout.findViewById(R.id.summary_mistakes_count);
		mQuestionsCount = (TextView) layout.findViewById(R.id.summary_questions_count);
		mExamInfo = (TextView) layout.findViewById(R.id.summary_exam_info);

		if (mTestResult.isExamPassed()) {
			if (mTestResult.getMistakes() == 0) {
				initComponents(R.drawable.congrats2, R.string.summary_pass_text);
			} else {
				initComponents(R.drawable.good2, R.string.summary_pass_text);
			}
		} else {
			initComponents(R.drawable.fail2, R.string.summary_fail_text);
		}

		builder = new AlertDialog.Builder(parent);
		builder.setView(layout).setCancelable(true);
		alertDialog = builder.create();
	}

	private void initComponents(final int imageRes, final int examInfoRes) {
		Resources res = mContext.getResources();
		mImage.setImageResource(imageRes);

		String correct = String.format(res.getString(R.string.summary_correct_answers_count), mTestResult.getCorrect());
		String mistakes = String.format(res.getString(R.string.summary_mistakes_count), mTestResult.getMistakes());
		String total = String.format(res.getString(R.string.summary_questions_count),
				res.getInteger(R.integer.max_exam_questions));

		mExamInfo.setText(examInfoRes);
		mCorrectAnswersCount.setText(correct);
		mMistakesCount.setText(mistakes);
		mQuestionsCount.setText(total);
	}

	public void show() {
		alertDialog.show();
	}
}
