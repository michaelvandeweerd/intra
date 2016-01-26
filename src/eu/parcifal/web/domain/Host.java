package eu.parcifal.web.domain;

import eu.parcifal.extra.logic.Executable;
import eu.parcifal.extra.logic.Router;
import eu.parcifal.extra.net.http.HTTPRequest;
import eu.parcifal.extra.net.http.HTTPResponse;
import eu.parcifal.extra.throwing.RouteNotFoundException;

public class Host implements Executable {

	private Router router;

	public Host(Router router) {
		this.router = router;
	}

	@Override
	public Object execute(Object... args) {
		if (args.length == 0 || !(args[0] instanceof HTTPRequest)) {
			throw new IllegalArgumentException();
		} else {
			HTTPRequest request = (HTTPRequest) args[0];

			try {
				return this.router.route(request.uri().path(), request);
			} catch (RouteNotFoundException e) {
				return new HTTPResponse(HTTPResponse.STATUS_404);
			}
		}
	}

}
