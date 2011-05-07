package pl.greenislanddev.prawojazdy.business.sql.tables;

public class QuestionTable {

	public static final String NAME = "question";

	public static final String KEY_ID = "_id";
	public static final String KEY_QUESTION_ID = "question_id";
	public static final String KEY_ANSWER_OPTIONS_ID = "answer_options_id";
	public static final String KEY_CORRECT_ANSWER_ID = "correct_answer_id";
	public static final String KEY_IMAGE_ID = "image_id";
	public static final String KEY_CATEGORY_ID = "category_id";
	
	public static final String TABLE_CREATE = "create table "
		+ NAME + " ("//
		+ KEY_ID + " integer primary key autoincrement, " //
		+ KEY_QUESTION_ID + " integer not null, " //
		+ KEY_ANSWER_OPTIONS_ID + " integer not null, " //
		+ KEY_CORRECT_ANSWER_ID + " integer not null, " //
		+ KEY_CATEGORY_ID + " category_id integer, " //
		+ KEY_IMAGE_ID + " integer);";
	
	public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + NAME;
}
