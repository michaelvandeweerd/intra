package eu.parcifal.web.main;

import eu.parcifal.web.Context;

public class IndexContext extends Context {

	@Override
	protected String response() {
		return this.parse(this.load("pagina/main/index.pp"));
	}

}
