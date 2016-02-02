package eu.parcifal.intra.http;

import java.net.Socket;

import eu.parcifal.plus.logic.Pool;
import eu.parcifal.plus.logic.Router;
import eu.parcifal.plus.net.Exchanger;

public class HTTPExchangerPool extends Pool<Exchanger> {

	private static Router ROUTER;

	public HTTPExchangerPool(Router router) {
		ROUTER = router;
	}

	@Override
	protected Exchanger instantiate(Object... args) {
		if (!(args[0] instanceof Socket)) {
			throw new IllegalArgumentException();
		} else {
			Exchanger responder = new HTTPExchanger(ROUTER);

			responder.observe(this);
			responder.initialise((Socket) args[0]);

			new Thread(responder).start();

			return responder;
		}
	}

}
