package eu.parcifal.web.domain;

import eu.parcifal.extra.logic.Route;
import eu.parcifal.extra.logic.Router;
import eu.parcifal.web.content.file.Index;

public class FileDomain extends Host {

	public FileDomain() {
		super(new Router(new Route(new Index(), Route.PATH_ALL)));
	}

}
