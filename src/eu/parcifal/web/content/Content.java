package eu.parcifal.web.content;

import eu.parcifal.extra.logic.Executable;
import eu.parcifal.extra.net.http.HTTPRequest;
import eu.parcifal.extra.net.http.HTTPResponse;

public abstract class Content implements Executable {

	@Override
	public final Object execute(Object... args) {
		if (args.length == 0 || !(args[0] instanceof HTTPRequest)) {
			throw new IllegalArgumentException();
		} else {
			return response((HTTPRequest) args[0]);
		}
	}

	protected abstract HTTPResponse response(HTTPRequest request);

}
