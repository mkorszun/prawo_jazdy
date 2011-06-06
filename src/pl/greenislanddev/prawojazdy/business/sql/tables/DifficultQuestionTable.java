package pl.greenislanddev.prawojazdy.business.sql.tables;

public class DifficultQuestionTable {

	public static final String NAME = "difficult_question";

	public static final String KEY_ID = "_id";
	public static final String KEY_QUESTION_ID = "question_id";
	public static final String KEY_PROFILE_ID = "profile_id";

	public static final String TABLE_CREATE = "create table if not exists " + NAME + " ("//
			+ KEY_ID + " integer primary key autoincrement, " //
			+ KEY_QUESTION_ID + " integer not null, " //
			+ KEY_PROFILE_ID + " integer);";

	public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + NAME;

}
