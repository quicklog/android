package com.nhs.quicklog.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProceduresDatabase {
	private static final String TAG = "ProceduresDatabase";

	private static final String DATABASE_NAME = "quicklog.db";

	private static final int DATABASE_VERSION = 2;

	public ProceduresDatabase() {
	}

	public static class ProcedureOpenHelper extends SQLiteOpenHelper {
		ProcedureOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + QuickLog.Procedures.TABLE_NAME + " ("
					+ QuickLog.Procedures._ID + " INTEGER PRIMARY KEY,"
					+ QuickLog.Procedures.COLUMN_NAME_NAME + " TEXT UNIQUE,"
					+ QuickLog.Procedures.COLUMN_NAME_CREATE_DATE + " INTEGER, "
					+ QuickLog.Procedures.COLUMN_NAME_HITS + " INTEGER NOT NULL DEFAULT 0);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");

			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
	}
}
