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
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.nhs.quicklog.data.QuickLog;

public class Procedures extends RoboActivity {

	private static final String TAG = "Procedures";

	@InjectView(R.id.search)
	EditText searchView;
	@InjectView(R.id.list)
	ListView proceduresList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.procedures);
		
		this.addStaticProcedures();
		
		this.searchView.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				doMySearch(s.toString());
			}
		});
		
		proceduresList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> list, View view,
					int position, long id) {
				Cursor cursor = (Cursor) list.getAdapter().getItem(position);
				String procedure = cursor
						.getString(QuickLog.Procedures.COLUMN_INDEX_TITLE);

				addComment(procedure,
						cursor.getInt(QuickLog.Procedures.COLUMN_INDEX_ID),
						cursor.getInt(QuickLog.Procedures.COLUMN_INDEX_HITS));
			}
		});
	}

	private void doMySearch(String query) {
		ContentValues values = new ContentValues();
		values.put(QuickLog.Procedures.COLUMN_NAME_NAME, query);
		
		Cursor cursor = getContentResolver().query(
				Uri.parse(QuickLog.Procedures.CONTENT_ID_URI_BASE.toString()
						+ query), QuickLog.Procedures.PROJECTION, null, null,
				null);
		
		this.setListCursor(cursor);
	}

	private void FillItems() {
		CursorLoader cursorLoader = new CursorLoader(this, QuickLog.Procedures.CONTENT_URI,
				QuickLog.Procedures.PROJECTION, null, null,
				QuickLog.Procedures.DEFAULT_SORT_ORDER);
		
		this.setListCursor(cursorLoader.loadInBackground());
	}

	private void setListCursor(Cursor cursor) {
		String[] dataColumns = { QuickLog.Procedures.COLUMN_NAME_NAME };

		int[] viewIDs = { android.R.id.text1 };

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1, cursor, dataColumns,
				viewIDs, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		proceduresList.setAdapter(adapter);	
	}
	
	public void addProcedure(View view) {
		String procedure = this.searchView.getText().toString();
		if (TextUtils.isEmpty(procedure)) {
			return;
		}

		int id = AddProcedureEntry(procedure);
		this.addComment(procedure, id, 0);
	}

	private int AddProcedureEntry(String procedure) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(QuickLog.Procedures.COLUMN_NAME_NAME, procedure);
		initialValues.put(QuickLog.Procedures.COLUMN_NAME_HITS, 0);
		Uri uri = getContentResolver().insert(QuickLog.Procedures.CONTENT_URI,
				initialValues);

		return Integer.valueOf(uri.getPathSegments().get(
				QuickLog.Procedures.PROCEDURE_ID_PATH_POSITION));
	}

	private void addComment(String procedure, int id, int hits) {
		Intent intent = new Intent(this, Comment.class);
		intent.putExtra("procedure", procedure);
		intent.putExtra("id", id);
		intent.putExtra("hits", hits);
		startActivity(intent);
        this.finish();
	}

	private void addStaticProcedures() {
		proceduresList.post(new Runnable() {
			public void run() {
				try {
					loadProcesures();
					FillItems();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
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
				AddProcedureEntry(line);
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
		int count = countCursor.getInt(0);
		countCursor.close();
		return count;
	}
}
