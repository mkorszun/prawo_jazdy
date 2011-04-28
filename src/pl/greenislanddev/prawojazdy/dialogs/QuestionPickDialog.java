package pl.greenislanddev.prawojazdy.dialogs;

import pl.greenislanddev.prawojazdy.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class QuestionPickDialog extends AlertDialog implements OnClickListener {

	private static final String PROVIDED_TEXT = "provided_text";

	private int mQuestionNumber;
	private int mMaxQuestionNumber;
	private Context mContext;
	private OnNumberSetListener mCallback;

	private TextView mText;
	private EditText mNumberEdit;

	public interface OnNumberSetListener {
		void onNumberSet(int questionNumber);
	}

	public QuestionPickDialog(Context context, OnNumberSetListener callback) {
		super(context);
		this.mQuestionNumber = 1;
		this.mCallback = callback;
		this.mContext = context;
		this.mMaxQuestionNumber = mContext.getResources().getInteger(R.integer.max_questions);

		setButton(BUTTON_POSITIVE, context.getText(R.string.common_ok), this);
		setButton(BUTTON_NEGATIVE, context.getText(R.string.common_cancel), (OnClickListener) null);

		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.question_pick, null);
		setView(view);

		mText = (TextView) view.findViewById(R.id.question_pick_txt);
		mNumberEdit = (EditText) view.findViewById(R.id.question_pick_edit);

		String text = String.format(mContext.getResources().getString(R.string.question_pick_text), mMaxQuestionNumber);
		mText.setText(text);
		mNumberEdit.setText("");

	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (mCallback != null) {
			String providedText = mNumberEdit.getText().toString();

			try {
				Integer number = Integer.parseInt(providedText);

				if (number > 0 && number <= mMaxQuestionNumber) {
					mQuestionNumber = number;
					mCallback.onNumberSet(mQuestionNumber);
				}
			} catch (NumberFormatException e) {
				// ignore
			} finally {
				mNumberEdit.setText("");
			}
		}
	}

	@Override
	public Bundle onSaveInstanceState() {
		Bundle state = super.onSaveInstanceState();
		state.putString(PROVIDED_TEXT, mNumberEdit.getText().toString());
		return state;
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		String providedText = savedInstanceState.getString(PROVIDED_TEXT);
		mNumberEdit.setText(providedText);
	}

}
