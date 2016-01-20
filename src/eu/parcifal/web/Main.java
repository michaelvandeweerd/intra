package eu.parcifal.web;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import eu.parcifal.extra.logic.Route;
import eu.parcifal.extra.logic.Router;
import eu.parcifal.extra.print.Console;
import eu.parcifal.web.kort.CustomURLContext;
import eu.parcifal.web.kort.IndexContext;
import eu.parcifal.web.kort.URLContext;

public class Main {

	private final static int PORT = 80;

	private static final String MESSAGE_RUN = "listening for HTTP requests at http://localhost:%1s";

	public static void main(String[] args) throws IOException {
		Console.printDebug(true);

		Domain main = new Domain() {
			@Override
			protected Router router() {
				return new Router(
						new Route(new eu.parcifal.web.main.IndexContext(), Route.PATH_EMPTY)
					);
			}
		};
		
		Domain file = new Domain() {
			@Override
			protected Router router() {
				return new Router(
						new Route(new eu.parcifal.web.file.FileContext(), Route.PATH_ALL)
					);
			}
		};

		Domain kort = new Domain() {
			@Override
			protected Router router() {
				return new Router(
						new Route(new IndexContext(), Route.PATH_EMPTY),
						new Route(new URLContext(), "^\\/+(?:u\\w{8}|c[\\w-]{8})(?:\\?.*)?$"),
						new Route(new CustomURLContext(), "^\\/+(?![uc])\\w{0,16}(?:\\?.*)?$")
					);
			}
		};

		Router router = new Router(
				new Route(main, "parcifal.eu", "parcifal.eu.localhost"),
				new Route(file, "file.parcifal.eu", "file.parcifal.eu.localhost"),
				new Route(kort, "parcif.al", "parcif.al.localhost")
			);

		HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

		httpServer.createContext("/", new Handler(router));
		httpServer.setExecutor(null);
		httpServer.start();

		Console.log(MESSAGE_RUN, PORT);
	}

}
