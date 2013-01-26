package com.nhs.easyprocedurelogger;

import java.net.URI;
import java.net.URISyntaxException;

import android.content.Context;

import com.b2msolutions.reyna.Header;
import com.b2msolutions.reyna.Message;
import com.b2msolutions.reyna.services.StoreService;

public class Post {

	public void execute(Context context, String content)
			throws URISyntaxException {
		Header[] headers = new Header[] {
				new Header("Content-Type", "application/json"),
				new Header("USERTOKEN", "AndroidDevice") };

		Message message = new Message(
				new URI("https://quicklog.herokuapp.com/api/1/me/items"), content, headers);

		StoreService.start(context, message);
	}
}
