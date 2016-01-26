package eu.parcifal.web.domain;

import eu.parcifal.extra.logic.Route;
import eu.parcifal.extra.logic.Router;
import eu.parcifal.web.content.main.Index;

public class MainDomain extends Host {

	public MainDomain() {
		super(new Router(new Route(new Index(), ".*")));
	}

}
