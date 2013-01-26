package com.nhs.easyprocedurelogger;

import roboguice.RoboGuice;
import android.app.Application;

public class QuickLogApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		RoboGuice.setBaseApplicationInjector(this, RoboGuice.DEFAULT_STAGE,
				RoboGuice.newDefaultRoboModule(this), new MyModule());
	}

}
