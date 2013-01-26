package com.nhs.easyprocedurelogger;

import com.google.inject.Module;
import com.nhs.quicklog.data.ProceduresNames;

public class MyModule implements Module {

	public void configure(com.google.inject.Binder binder) {
		binder.bind(ProceduresNames.class).toInstance(new ProceduresNames());
	}
}
