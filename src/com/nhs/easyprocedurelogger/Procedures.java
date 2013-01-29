package com.nhs.easyprocedurelogger;

import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.google.inject.Inject;
import com.nhs.quicklog.data.ProceduresNames;

public class Procedures extends RoboActivity {
	@InjectView(R.id.search)
	SearchView searchView;
	@InjectView(R.id.list)
	ListView proceduresList;

	@Inject
	ProceduresNames proceduresNames;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.procedures);

		searchView.setSubmitButtonEnabled(true);
		this.FillItems(proceduresNames.get());
		
		this.searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				doMySearch(query);
				return true;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				doMySearch(newText);
				return true;
			}
		});
		
		proceduresList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> list, View view,
					int position, long id) {
				Object item = list.getItemAtPosition(position);
				addLog(item.toString());
			}
		});
	}

	private void doMySearch(String query) {
		
		this.FillItems(proceduresNames.getStartedWith(query));
	}

	private void FillItems(List<String> items) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, items);

		proceduresList.setAdapter(adapter);
	}

	public void addProcedure(View view) {
		String procedure = this.searchView.getQuery().toString();

		if (TextUtils.isEmpty(procedure)) {
			return;
		}

		proceduresNames.Add(procedure);
		this.addLog(procedure);
	}

	private void addLog(String procedure) {
		Intent intent = new Intent(this, QuickLog.class);
		intent.putExtra("procedure", procedure);
		startActivity(intent);
	}
}
