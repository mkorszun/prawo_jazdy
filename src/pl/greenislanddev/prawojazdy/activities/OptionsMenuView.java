package pl.greenislanddev.prawojazdy.activities;

import pl.greenislanddev.prawojazdy.R;
import pl.greenislanddev.prawojazdy.business.ImageAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class OptionsMenuView extends Activity {

	private static final int OPTION_TRAINING = 0;
	private static final int OPTION_EXAM = 1;
	private static final int OPTION_CATEGORIES = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options_menu);

		GridView gridview = (GridView) findViewById(R.id.options_menu);
		gridview.setAdapter(new ImageAdapter(this));

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				Class<?> c = null;

				switch (position) {
				case OPTION_TRAINING:
					c = QuestionViewer.class;
					break;
				case OPTION_EXAM:
					c = QuestionViewer.class;
					break;
				case OPTION_CATEGORIES:
					c = CategoryList.class;
					break;
				}

				Intent myIntent = new Intent(v.getContext(), c);
				startActivityForResult(myIntent, 0);
			}
		});

	}
}
