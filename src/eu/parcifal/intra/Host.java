package eu.parcifal.intra;

import eu.parcifal.intra.http.HTTPRequest;
import eu.parcifal.plus.logic.Executable;
import eu.parcifal.plus.logic.Route;
import eu.parcifal.plus.logic.Router;

public class Host implements Executable {

	private Router router;

	public Host(Router router) {
		this.router = router;
	}

	public Host(Route... routes) {
		this(new Router(routes));
	}

	@Override
	public Object execute(Object... args) {
		if (args.length == 0 || !(args[0] instanceof HTTPRequest)) {
			throw new IllegalArgumentException();
		} else {
			HTTPRequest request = (HTTPRequest) args[0];

			return this.router.route(request.requestLine().requestURI().path(), request);
		}
	}

}
