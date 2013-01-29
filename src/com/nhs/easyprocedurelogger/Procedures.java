package com.nhs.easyprocedurelogger;

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

import com.google.inject.Inject;
import com.nhs.quicklog.data.ProceduresNames;

public class Procedures extends RoboActivity {
	@InjectView(R.id.search)
	SearchView searchView;
	@InjectView(R.id.procedures)
	ListView proceduresList;

	@Inject
	ProceduresNames proceduresNames;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.procedures);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, proceduresNames.get());

		proceduresList.setAdapter(adapter);
		
		this.searchView.setIconified(false);
		
		proceduresList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> list, View view, int position,
					long id) {
		         Object item = list.getItemAtPosition(position);
		         addLog(item.toString());
    		}
		});
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
