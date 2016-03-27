package eu.parcifal.intra;

import eu.parcifal.intra.http.HTTPMessageBody;
import eu.parcifal.intra.http.HTTPRequest;
import eu.parcifal.intra.http.HTTPResponse;
import eu.parcifal.intra.http.HTTPStatusLine;
import eu.parcifal.plus.MethodNotImplementedException;
import eu.parcifal.plus.logic.Executable;
import eu.parcifal.plus.logic.Route;
import eu.parcifal.plus.logic.RouteNotFoundException;
import eu.parcifal.plus.logic.Router;
import eu.parcifal.plus.print.Console;

public class Host implements Executable {

	private Router content;

	private Router status;

	public Host(Router content, Router status) {
		this.content = content;
		this.status = status;
	}

	public Host(Router content) {
		this(content, Router.EMPTY);
	}

	public Host(Route... routes) {
		this(new Router(routes));
	}

	@Override
	public HTTPResponse execute(Object... args) {
		if (args.length == 0 || !(args[0] instanceof HTTPRequest)) {
			throw new IllegalArgumentException();
		} else {
			HTTPRequest request = (HTTPRequest) args[0];

			HTTPResponse response = null;

			try {
				response = (HTTPResponse) this.content.route(request.requestLine().requestURI().path(), request);
			} catch (RouteNotFoundException | IllegalArgumentException exception) {
				response = new HTTPResponse(HTTPStatusLine.STATUS_404_1_1,
						new HTTPMessageBody(HTTPStatusLine.STATUS_404_1_1));
				
				Console.warn(exception);
			} catch (MethodNotImplementedException exception) {
				response = new HTTPResponse(HTTPStatusLine.STATUS_405_1_1,
						new HTTPMessageBody(HTTPStatusLine.STATUS_405_1_1));
				
				Console.warn(exception);
			} catch (RuntimeException exception) {
				response = new HTTPResponse(HTTPStatusLine.STATUS_500_1_1,
						new HTTPMessageBody(HTTPStatusLine.STATUS_500_1_1));
				
				Console.warn(exception);
			}

			try {
				HTTPStatusLine statusLine = response.statusLine();

				response = (HTTPResponse) this.status.route(String.valueOf(statusLine.statusCode()), request);
				response.statusLine(statusLine);
			} catch (RouteNotFoundException exception) {

			}

			return response;
		}
	}

}
