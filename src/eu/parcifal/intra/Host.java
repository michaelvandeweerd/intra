package eu.parcifal.intra;

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
        this(content, new Router());
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

            HTTPResponse response;

            try {
                response = (HTTPResponse) this.content.route(request.getRequestLine().getRequestURI().getPath(), request);
            } catch (RouteNotFoundException | IllegalArgumentException exception) {
                response = new HTTPResponse(HTTPStatusLine.STATUS_404_1_1);
                response.setMessageBody(HTTPStatusLine.STATUS_404_1_1);

                Console.warn(exception);
            } catch (MethodNotImplementedException exception) {
                response = new HTTPResponse(HTTPStatusLine.STATUS_405_1_1);
                response.setMessageBody(HTTPStatusLine.STATUS_405_1_1);

                Console.warn(exception);
            } catch (RuntimeException exception) {
                response = new HTTPResponse(HTTPStatusLine.STATUS_500_1_1);
                response.setMessageBody(HTTPStatusLine.STATUS_500_1_1);

                Console.warn(exception);
            }

            HTTPStatusLine statusLine = response.getStatusLine();

            if (this.status.explore(String.valueOf(statusLine.statusCode()))) {
                response = (HTTPResponse) this.status.route(String.valueOf(statusLine.statusCode()), request);
                response.setStatusLine(statusLine);
            }

            return response;
        }
    }

}
