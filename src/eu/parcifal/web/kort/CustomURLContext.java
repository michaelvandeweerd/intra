package eu.parcifal.web.kort;

import eu.parcifal.web.Context;

public class CustomURLContext extends Context {

	@Override
	protected String response() {
		return "custom URL: " + this.path();
	}

}
