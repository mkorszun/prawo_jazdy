package pl.greenislanddev.prawojazdy.business.sequencer.sql;

import pl.greenislanddev.prawojazdy.business.sequencer.sql.tables.QuestionTable;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public abstract class DbDataCreator {
	
	long insertQuestion(SQLiteDatabase db, int questionId, int answerOptionsId,
			int correctAnswerId, int categoryId, int imageId) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(QuestionTable.KEY_QUESTION_ID, questionId);
		initialValues.put(QuestionTable.KEY_ANSWER_OPTIONS_ID, answerOptionsId);
		initialValues.put(QuestionTable.KEY_CORRECT_ANSWER_ID, correctAnswerId);
		initialValues.put(QuestionTable.KEY_CATEGORY_ID, categoryId);
		initialValues.put(QuestionTable.KEY_IMAGE_ID, imageId);
		return db.insert(QuestionTable.NAME, null, initialValues);
	}

}
