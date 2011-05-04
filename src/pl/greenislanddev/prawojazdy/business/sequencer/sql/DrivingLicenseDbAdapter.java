package pl.greenislanddev.prawojazdy.business.sequencer.sql;

import pl.greenislanddev.prawojazdy.business.sequencer.sql.tables.CurrentTestTable;
import pl.greenislanddev.prawojazdy.business.sequencer.sql.tables.QuestionTable;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DrivingLicenseDbAdapter {

	private static final String TAG = "DrivingLicenseDbAdapter";

	private static final String DATABASE_NAME = "driving_license";
	private static final int DATABASE_VERSION = 27;

	private final Context mCtx;

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(QuestionTable.TABLE_CREATE);
			db.execSQL(CurrentTestTable.TABLE_CREATE);
			initData(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
					+ ", which will destroy all old data");
			db.execSQL(QuestionTable.DROP_TABLE);
			db.execSQL(CurrentTestTable.DROP_TABLE);
			onCreate(db);
		}

		private void initData(SQLiteDatabase db) {
			new Category1DataCreator().initData(db);
			new Category2DataCreator().initData(db);
			new Category3DataCreator().initData(db);
			new Category4DataCreator().initData(db);
			new Category5DataCreator().initData(db);
			new Category6DataCreator().initData(db);
			new Category7DataCreator().initData(db);
			new Category8DataCreator().initData(db);
			new Category9DataCreator().initData(db);
		}

	}

	public DrivingLicenseDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public DrivingLicenseDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		if (mDbHelper != null) {
			mDbHelper.close();
			mDbHelper = null;
		}

		if (mDb != null) {
			mDb.close();
			mDb = null;
		}
	}

	public Cursor fetchQuestion(long rowId) throws SQLException {
		Cursor mCursor = //
		mDb.query(true, QuestionTable.NAME, new String[] { QuestionTable.KEY_ID, //
				QuestionTable.KEY_QUESTION_ID, //
				QuestionTable.KEY_ANSWER_OPTIONS_ID, //
				QuestionTable.KEY_CORRECT_ANSWER_ID, //
				QuestionTable.KEY_IMAGE_ID }, //
				QuestionTable.KEY_ID + " = " + rowId, //
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
}
