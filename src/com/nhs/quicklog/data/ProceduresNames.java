package com.nhs.quicklog.data;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

import com.google.inject.Singleton;

@Singleton
public class ProceduresNames {

	private ArrayList<String> items;

	public ProceduresNames() {
		items = new ArrayList<String>();
		this.FillItems();
	}

	public void Add(String procedure) {
		if (this.items.contains(procedure)) {
			return;
		}

		this.items.add(0, procedure);
	}

	public List<String> get() {
		return this.items;
	}

	public List<String> getStartedWith(String pattern) {

		if (TextUtils.isEmpty(pattern)) {
			return this.items;
		}

		ArrayList<String> mactahedItems = new ArrayList<String>();
		for (String item : items) {
			if (item.toLowerCase().startsWith(pattern.toLowerCase())) {
				mactahedItems.add(item);
			}
		}

		return mactahedItems;
	}

	private void FillItems() {
		this.items.add("Cannula");
		this.items.add("Venepuncture");
		this.items.add("Blood Culture");
		this.items.add("Male Catheterisation");
		this.items.add("Female Catheterisation");
		this.items.add("Arterial Blood Gas");
		this.items.add("IV drug administration");
		this.items.add("Chest Drain");
		this.items.add("Lumbar puncture");
	}

}
