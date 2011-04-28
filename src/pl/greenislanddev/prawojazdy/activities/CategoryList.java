package pl.greenislanddev.prawojazdy.activities;


import pl.greenislanddev.prawojazdy.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class CategoryList extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String[] categories = getResources().getStringArray(R.array.categories);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.categorylist_item, categories));
	}

}
