package eu.parcifal.web.kort;

import eu.parcifal.extra.logic.Route;
import eu.parcifal.extra.logic.Router;
import eu.parcifal.web.Domain;

public class KortDomain extends Domain {

	@Override
	protected Router router() {
		return new Router(
				new Route(new IndexContext(), Route.PATH_EMPTY),
				new Route(new URLContext(), "^\\/+(?:u\\w{8}|c[\\w-]{8})(?:\\?.*)?$"),
				new Route(new CustomURLContext(), "^\\/+(?![uc])\\w{0,16}(?:\\?.*)?$")
			);
	}

}
