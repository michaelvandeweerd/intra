package eu.parcifal.web;

import com.sun.net.httpserver.HttpExchange;

import eu.parcifal.extra.logic.Executable;
import eu.parcifal.extra.logic.Router;
import eu.parcifal.extra.print.Console;
import eu.parcifal.extra.print.output.Warning;
import eu.parcifal.extra.throwing.RouteNotFoundException;

public abstract class Domain implements Executable {

	private static final String MESSAGE_ILLEGAL_ARGUMENT = "specified data is not an instance of an HTTP exchange";

	@Override
	public void execute(Object... args) {
		if (!(args[0] instanceof HttpExchange)) {
			throw new IllegalArgumentException(MESSAGE_ILLEGAL_ARGUMENT);
		} else {
			HttpExchange exchange = (HttpExchange) args[0];

			Router router = router();

			try {
				router.route(exchange.getRequestURI().toString(), exchange);
			} catch (RouteNotFoundException rnfe) {
				Console.warn(Warning.Level.HIGH, rnfe.getMessage());
			}
		}
	}

	abstract protected Router router();

}
