package com.nhs.easyprocedurelogger;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nhs.quicklog.data.Item;
import com.nhs.quicklog.data.Items;
import com.nhs.quicklog.data.QuickLog;

public class Comment extends RoboActivity {

	@InjectView(R.id.procedure)
	TextView procedure;
	@InjectView(R.id.rating)
	RatingBar rating;
	@InjectView(R.id.comment)
	EditText comment;
	@InjectView(R.id.attempts)
	EditText attempts;

	private String procedureName;
	private int procedureId;
	private int procedureHits;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log);
		this.procedureName = this.getIntent()
				.getCharSequenceExtra("procedure").toString();
		this.procedureId = this.getIntent().getIntExtra("id", 0);
		this.procedureHits = this.getIntent().getIntExtra("hits", 0);
		procedure.setText(this.procedureName);
	}

	public void addProcedure(View view) throws URISyntaxException {
		this.incrementProcedureHits();
		
		Post post = new Post();
		post.execute(this, this.getLog());
		
		Toast.makeText(getApplicationContext(), "procedure comment added",
				Toast.LENGTH_LONG).show();
		
		this.startProcedures();
	}

	private void incrementProcedureHits() {
		ContentValues values = new ContentValues();
		values.put(QuickLog.Procedures.COLUMN_NAME_NAME, this.procedureName);
		values.put(QuickLog.Procedures.COLUMN_NAME_HITS, this.procedureHits + 1);
		getContentResolver().update(Uri.parse(
				QuickLog.Procedures.CONTENT_ID_URI_BASE.toString() + this.procedureId),
				values, null, null);
	}

	public void stats(View view) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("http://quicklog.herokuapp.com/me/procedures/all"));
		startActivity(intent);
	}

	public void target(View view) {
	}

	public void minus(View view) {
		int value = this.getAttempts();

		--value;

		if (value <= 0) {
			return;
		}

		this.attempts.setText(String.format("%d", value));
	}

	public void plus(View view) {
		int value = this.getAttempts();
		++value;

		this.attempts.setText(String.format("%d", value));
	}

	public void startProcedures() {
		Intent intent = new Intent(this, Procedures.class);
		startActivity(intent);
	}

	private int getAttempts() {
		try {
			String value = this.attempts.getText().toString();
			return Integer.parseInt(value);
		} catch (Exception e) {
			return 0;
		}
	}

	private String getLog() {
		long time = new Date().getTime();
		Items items = new Items();
		Item item = new Item();
		item.setAttempts(Integer.parseInt(attempts.getText().toString()));
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("tag1");
		item.setComment(this.comment.getText().toString());
		item.setRating((int) this.rating.getRating());
		item.setTimestamp(String.valueOf(time));
		item.setTags(tags);
		item.setId(UUID.randomUUID());
		items.add(item);
		return new Gson().toJson(items);
	}
}
