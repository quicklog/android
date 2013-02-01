package com.nhs.easyprocedurelogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;

import com.nhs.quicklog.data.QuickLog;

public class Procedures extends RoboActivity {

	private static final String TAG = "Procedures";

	@InjectView(R.id.search)
	SearchView searchView;
	@InjectView(R.id.list)
	ListView proceduresList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.procedures);
		searchView.setSubmitButtonEnabled(true);

		this.addStaticProcedures();
		this.FillItems();

		// this.searchView.setOnQueryTextListener(new OnQueryTextListener() {
		//
		// @Override
		// public boolean onQueryTextSubmit(String query) {
		// //doMySearch(query);
		// return true;
		// }
		//
		// @Override
		// public boolean onQueryTextChange(String newText) {
		// // doMySearch(newText);
		// return true;
		// }
		// });
		//
		
		proceduresList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> list, View view,
					int position, long id) {
				Cursor cursor = (Cursor) list.getAdapter().getItem(position);
				String procedure =  cursor.getString(QuickLog.Procedures.COLUMN_INDEX_TITLE);
				
				ContentValues values = new ContentValues();
				values.put(QuickLog.Procedures.COLUMN_NAME_NAME, cursor.getString(QuickLog.Procedures.COLUMN_INDEX_TITLE));
				values.put(QuickLog.Procedures.COLUMN_NAME_HITS, cursor.getInt(QuickLog.Procedures.COLUMN_INDEX_HITS) + 1);
				getContentResolver().update(Uri.parse(
						QuickLog.Procedures.CONTENT_ID_URI_BASE.toString() + cursor.getInt(0)),
						values, null, null);
				addComment(procedure);
			}
		});
	}

	private void doMySearch(String query) {

	}

	private void FillItems() {
		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(QuickLog.Procedures.CONTENT_URI,
				QuickLog.Procedures.PROJECTION, null, null,
				QuickLog.Procedures.DEFAULT_SORT_ORDER);

		String[] dataColumns = { QuickLog.Procedures.COLUMN_NAME_NAME };

		int[] viewIDs = { android.R.id.text1 };

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1, cursor, dataColumns,
				viewIDs);

		proceduresList.setAdapter(adapter);
	}

	public void addProcedure(View view) {
		String procedure = this.searchView.getQuery().toString();
		AddProcedureEntry(procedure, 1);
		this.addComment(procedure);
	}

	private void AddProcedureEntry(String procedure, int hits) {
		if (TextUtils.isEmpty(procedure)) {
			return;
		}

		ContentValues initialValues = new ContentValues();
		initialValues.put(QuickLog.Procedures.COLUMN_NAME_NAME, procedure);
		initialValues.put(QuickLog.Procedures.COLUMN_NAME_HITS, hits);
		getContentResolver().insert(QuickLog.Procedures.CONTENT_URI,
				initialValues);
	}

	private void addComment(String procedure) {
		Intent intent = new Intent(this, Comment.class);
		intent.putExtra("procedure", procedure);
		startActivity(intent);
	}

	private void addStaticProcedures() {
		new Thread(new Runnable() {
			public void run() {
				try {
					loadProcesures();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}).start();
	}

	private void loadProcesures() throws IOException {
		if (getCount() != 0) {
			return;
		}

		Log.d(TAG, "Loading Procesures...");
		final Resources resources = this.getResources();
		InputStream inputStream = resources.openRawResource(R.raw.initiallist);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				AddProcedureEntry(line, 0);
			}
		} finally {
			reader.close();
		}
		Log.d(TAG, "DONE loading procedures.");
	}

	private int getCount() {
		Cursor countCursor = getContentResolver().query(
				QuickLog.Procedures.CONTENT_URI,
				new String[] { "count(*) AS count" }, null, null, null);

		countCursor.moveToFirst();
		int count =  countCursor.getInt(0);
		countCursor.close();
		return count;
	}
}
