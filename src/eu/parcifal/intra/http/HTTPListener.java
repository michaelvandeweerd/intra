package eu.parcifal.intra.http;

import eu.parcifal.plus.logic.Pool;
import eu.parcifal.plus.logic.Router;
import eu.parcifal.plus.net.Exchanger;
import eu.parcifal.plus.net.PortListener;

public class HTTPListener extends PortListener {

	private static final int DEFAULT_PORT = 80;

	private HTTPExchangerPool pool;

	public HTTPListener(int port, Router router) {
		super(port);

		this.pool = new HTTPExchangerPool(router);
	}

	public HTTPListener(Router router) {
		this(DEFAULT_PORT, router);
	}

	@Override
	protected Pool<Exchanger> pool() {
		return this.pool;
	}

}
