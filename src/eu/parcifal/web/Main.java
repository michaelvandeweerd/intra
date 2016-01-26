package eu.parcifal.web;

import eu.parcifal.extra.logic.Route;
import eu.parcifal.extra.logic.Router;
import eu.parcifal.extra.net.http.HTTPListener;
import eu.parcifal.extra.print.Console;
import eu.parcifal.web.domain.FileDomain;
import eu.parcifal.web.domain.KortDomain;
import eu.parcifal.web.domain.MainDomain;

public class Main {

	public static void main(String... args) {
		Console.debug(true);

		Route file = new Route(new FileDomain(), "file.parcifal.eu", "file.parcifal.eu.localhost");
		Route kort = new Route(new KortDomain(), "parcif.al", "parcif.al.localhost");
		Route main = new Route(new MainDomain(), "parcifal.eu", "parcifal.eu.localhost");

		Router router = new Router(file, kort, main);

		HTTPListener listener = new HTTPListener(router);

		new Thread(listener).start();
	}

}
