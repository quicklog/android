package com.nhs.easyprocedurelogger;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nhs.quicklog.data.Item;
import com.nhs.quicklog.data.Items;

public class QuickLog extends RoboActivity {

	@InjectView(R.id.procedure)
	TextView procedure;
	@InjectView(R.id.rating)
	RatingBar rating;
	@InjectView(R.id.comment)
	EditText comment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log);
		String procedureName = this.getIntent()
				.getCharSequenceExtra("procedure").toString();
		procedure.setText(procedureName);
	}

	public void addProcedure(View view) throws URISyntaxException {
		Post post = new Post();
		post.execute(this, this.getLog());
        Toast.makeText(getApplicationContext(), "procedure comment added", Toast.LENGTH_LONG).show();
		this.startProcedures();
	}

	public void startProcedures() {
		Intent intent = new Intent(this, Procedures.class);
		startActivity(intent);
	}

	private String getLog() {
		long time = new Date().getTime();
		Items items = new Items();
		Item item = new Item();
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("tag1");
		item.setComment(this.comment.getText().toString());
		item.setRating((int)this.rating.getRating());
		item.setTimestamp(String.valueOf(time));
		item.setTags(tags);
		item.setId(UUID.randomUUID());
		items.add(item);
		return new Gson().toJson(items);
	}
}
