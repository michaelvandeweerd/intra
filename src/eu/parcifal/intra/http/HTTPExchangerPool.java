package eu.parcifal.intra.http;

import eu.parcifal.plus.logic.Pool;
import eu.parcifal.plus.logic.Poolable;
import eu.parcifal.plus.logic.Router;
import eu.parcifal.plus.net.Exchanger;

public class HTTPExchangerPool extends Pool {

    private Router router;

    private HTTPExchangerPool(Router router) {
        super();

        this.router = router;
    }

    @Override
    protected Poolable instantiate(Object... args) {
        Exchanger exchanger = new HTTPExchanger(this.router);

        new Thread(exchanger).start();

        return exchanger;
    }

    public static HTTPExchangerPool instantiate(Router router) {
        HTTPExchangerPool pool = new HTTPExchangerPool(router);

        pool.initialise();

        return pool;
    }

}
