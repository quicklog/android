package com.nhs.quicklog.data;

import java.util.ArrayList;

public class Items {

	public Items() {
		this.items = new ArrayList<Item>();
	}
	
	public ArrayList<Item> getItems() {
		return items;
	}

	public void add(Item item) {
		this.items.add(item);
	}
	
	private ArrayList<Item> items;

}
