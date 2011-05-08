package pl.greenislanddev.prawojazdy.activities;

import java.util.ArrayList;
import java.util.List;

import pl.greenislanddev.prawojazdy.R;
import pl.greenislanddev.prawojazdy.business.CategoryItem;
import pl.greenislanddev.prawojazdy.business.CategoryItemAdapter;
import pl.greenislanddev.prawojazdy.business.sequencer.QuestionsSequencer;
import pl.greenislanddev.prawojazdy.business.sequencer.TrainingQuestionsSequencer;
import pl.greenislanddev.prawojazdy.business.sql.DrivingLicenseDbAdapter;
import pl.greenislanddev.prawojazdy.exam.state.ExamState;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class CategoryList extends ListActivity {

	private long[][] mMinMaxQuestionIds;
	private DrivingLicenseDbAdapter dbAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] mCategories = getResources().getStringArray(R.array.categories);
		String[] mCategoryChapters = getResources().getStringArray(R.array.category_chapters);

		List<CategoryItem> items = new ArrayList<CategoryItem>();
		for (int i = 0; i < mCategories.length; i++) {
			items.add(new CategoryItem(mCategories[i], mCategoryChapters[i]));
		}

		setListAdapter(new CategoryItemAdapter(this, R.layout.categorylist_item, items));

		dbAdapter = new DrivingLicenseDbAdapter(this);
		dbAdapter.open();
		mMinMaxQuestionIds = dbAdapter.minMaxCategoryQuestionIds();
	}

	@Override
	protected void onListItemClick(ListView lv, View v, int position, long id) {
		super.onListItemClick(lv, v, position, id);

		Long firstQuestion = mMinMaxQuestionIds[position][0];
		int lastQuestion = getResources().getInteger(R.integer.max_questions);

		ExamState state = new ExamState();
		state.setExam(false);
		state.setPageCounter(firstQuestion.intValue());

		Intent myIntent = new Intent(this, QuestionViewer.class);
		QuestionsSequencer sequencer = new TrainingQuestionsSequencer(firstQuestion, lastQuestion);
		myIntent.putExtra(QuestionsSequencer.ID, sequencer);
		myIntent.putExtra(ExamState.ID, state);
		startActivityForResult(myIntent, 0);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbAdapter.close();
	}
}
