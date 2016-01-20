package eu.parcifal.web.kort;

import eu.parcifal.web.Context;

public class IndexContext extends Context {

	@Override
	protected String response() {
		return this.parse(this.load("pagina/kort/index.pp"));
	}

}
