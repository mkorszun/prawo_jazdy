package pl.greenislanddev.prawojazdy.business;

import java.util.List;

import pl.greenislanddev.prawojazdy.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CategoryItemAdapter extends ArrayAdapter<CategoryItem> {

	private List<CategoryItem> mItems;
	private Context mContext;

	public CategoryItemAdapter(Context context, int textViewResourceId, List<CategoryItem> items) {
		super(context, textViewResourceId, items);
		this.mContext = context;
		this.mItems = items;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.categorylist_item, null);
		}

		CategoryItem item = mItems.get(position);
		if (item != null) {
			TextView chapter = (TextView) view.findViewById(R.id.category_item_chapter);
			if (chapter != null) {
				chapter.setText(String.format("%s %s", mContext.getResources().getString(R.string.category_chapter),
						item.getChapter()));
			}

			TextView name = (TextView) view.findViewById(R.id.category_item_name);
			if (name != null) {
				name.setText(item.getCategoryName());
			}
		}

		return view;
	}

}
