package eu.parcifal.web.kort;

import eu.parcifal.web.Context;

public class URLContext extends Context {

	@Override
	protected String response() {
		return "default URL: " + this.path();
	}

}
