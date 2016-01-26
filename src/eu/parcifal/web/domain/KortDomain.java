package eu.parcifal.web.domain;

import eu.parcifal.extra.logic.Route;
import eu.parcifal.extra.logic.Router;
import eu.parcifal.web.content.kort.Index;

public class KortDomain extends Host {

	public KortDomain() {
		super(new Router(new Route(new Index(), Route.PATH_ALL)));
	}

}
