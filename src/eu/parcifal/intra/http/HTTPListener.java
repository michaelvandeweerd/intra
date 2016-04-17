package eu.parcifal.intra.http;

import eu.parcifal.plus.logic.Router;
import eu.parcifal.plus.net.PortListener;

public class HTTPListener extends PortListener {

    private static final int DEFAULT_PORT = 80;

    public HTTPListener(int port, Router router) {
        super(port, HTTPExchangerPool.instantiate(router));
    }

    public HTTPListener(Router router) {
        this(DEFAULT_PORT, router);
    }

}
