package pl.greenislanddev.prawojazdy.business.sql.tables;

public class CurrentTestTable {

	public static final String NAME = "CURRENT_TEST";

	public static final String KEY_ID = "_id";
	public static final String KEY_QUESTION_ID = "question_id";
	public static final String KEY_ANSWER_1_ID = "answer_1";
	public static final String KEY_ANSWER_2_ID = "answer_2";
	public static final String KEY_ANSWER_3_ID = "answer_3";
	public static final String KEY_PROFILE_ID = "profile_id";
	
	public static final String TABLE_CREATE = "create table "
		+ NAME + " ("//
		+ KEY_ID + " integer primary key autoincrement, " //
		+ KEY_QUESTION_ID + " integer not null, " //
		+ KEY_ANSWER_1_ID + " boolean not null, " //
		+ KEY_ANSWER_2_ID + " boolean not null, " //
		+ KEY_ANSWER_3_ID + " boolean not null, " //
		+ KEY_PROFILE_ID + " integer);";
	
	public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + NAME;
}
