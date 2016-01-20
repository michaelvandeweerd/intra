package eu.parcifal.web;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import eu.parcifal.extra.logic.Router;
import eu.parcifal.extra.print.Console;
import eu.parcifal.extra.print.output.Warning;
import eu.parcifal.extra.throwing.RouteNotFoundException;

public class Handler implements HttpHandler {

	private final static String MESSAGE_HANDLE_EXCHANGE = "handling HTTP request with URI %1s%2s";

	private Router router;

	public Handler(Router router) {
		this.router = router;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		Console.debug(MESSAGE_HANDLE_EXCHANGE, exchange.getRequestHeaders().getFirst("Host"), exchange.getRequestURI());

		try {
			router.route(exchange.getRequestHeaders().getFirst("Host"), exchange);
		} catch (RouteNotFoundException rnfe) {
			Console.warn(Warning.Level.HIGH, rnfe.getMessage());
		}
	}

}
