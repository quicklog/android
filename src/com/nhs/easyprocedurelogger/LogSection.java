package com.nhs.easyprocedurelogger;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.UUID;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.nhs.quicklog.data.Item;
import com.nhs.quicklog.data.Items;

public class LogSection extends RoboFragment {

	@InjectView(R.id.add)
	Button add;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.log_section, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					log();
				} catch (URISyntaxException e) {
					Log.e("LogSection", e.getMessage());
				}
			}
		});
	}

	private void log() throws URISyntaxException {
		Post post = new Post();
		post.execute(this.getActivity(), this.getLog());
	}

	private String getLog() {
		Items items = new Items();
		Item item = new Item();
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("tag1");
		item.setComment("comment1");
		item.setRating(5);
		item.setTimestamp("timestamp1");
		item.setTags(tags);
		item.setId(UUID.randomUUID());
		items.add(item);
		return new Gson().toJson(items);
	}

}
