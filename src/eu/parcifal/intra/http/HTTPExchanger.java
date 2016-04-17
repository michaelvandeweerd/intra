package eu.parcifal.intra.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import eu.parcifal.plus.MethodNotImplementedException;
import eu.parcifal.plus.logic.Router;
import eu.parcifal.plus.net.Exchanger;
import eu.parcifal.plus.print.Console;

/**
 * Responds to incoming HTTP request.
 * 
 * @author Michaël van de Weerd
 */
public class HTTPExchanger extends Exchanger {
    /**
     * The value of the server HTTP header in the response.
     */
    @SuppressWarnings("unused")
    private final static String SERVER_SIGNATURE = "INTRA";

    /**
     * The router used containing available hosts.
     */
    private Router hosts;

    /**
     * Construct a new HTTP exchange using the default encoding.
     * 
     * @param hosts
     *            The router containing available hosts.
     */
    public HTTPExchanger(Router hosts) {
        this.hosts = hosts;
    }

    @Override
    protected void exchange(InputStream input, OutputStream output) throws IOException {
        HTTPResponse response = null;

        try {
            HTTPRequestLine requestLine = HTTPRequestLine.fromString(new String(this.readLine(input)));

            HTTPRequest request = new HTTPRequest(requestLine);

            byte[] line = null;

            while ((line = this.readLine(input)).length > 2) {
                request.addMessageHeader(HTTPMessageHeader.fromString(new String(line)));
            }

            if (request.hasMessageHeader("Content-Length")) {
                int contentLength = Integer
                        .valueOf(new String(request.getMessageHeader("Content-Length").getFieldValue()));

                byte[] contentBody = new byte[contentLength];

                input.read(contentBody, 0, contentLength);

                request.setMessageBody(new HTTPMessageBody(contentBody));
            }

            switch (request.getRequestLine().getMethod()) {
            case "OPTIONS":
                response = this.options(request);
                break;
            case "GET":
                response = this.get(request);
                break;
            case "HEAD":
                response = this.head(request);
                break;
            case "POST":
                response = this.post(request);
                break;
            case "PUT":
                response = this.put(request);
                break;
            case "DELETE":
                response = this.delete(request);
                break;
            case "TRACE":
                response = this.trace(request);
                break;
            case "CONNECT":
                response = this.connect(request);
                break;
            }

            Console.debug(request);
        } catch (IllegalArgumentException exception) {
            Console.warn(exception);

            response = new HTTPResponse(HTTPStatusLine.STATUS_400_1_1);
            response.setMessageBody(HTTPStatusLine.STATUS_400_1_1);
        } catch (Exception exception) {
            Console.warn(exception);

            response = new HTTPResponse(HTTPStatusLine.STATUS_500_1_1);
            response.setMessageBody(HTTPStatusLine.STATUS_500_1_1);
        }

        // TODO set date
        // TODO set server signature
        
        Console.debug(response);

        output.write(response.toBytes());
    }

    /**
     * Implementation of the HTTP OPTIONS method.
     * 
     * @param httpRequest
     *            The incoming HTTP request.
     * @return The HTTP response.
     */
    private HTTPResponse options(HTTPRequest httpRequest) {
        throw new MethodNotImplementedException();
    }

    /**
     * Implementation of the HTTP GET method.
     * 
     * @param request
     *            The incoming HTTP request.
     * @return The HTTP response.
     */
    private HTTPResponse get(HTTPRequest request) {
        return (HTTPResponse) this.hosts.route(new String(request.getMessageHeader("Host").getFieldValue()), request);
    }

    /**
     * Implementation of the HTTP HEAD method.
     * 
     * @param httpRequest
     *            The incoming HTTP request.
     * @return The HTTP response.
     */
    private HTTPResponse head(HTTPRequest httpRequest) {
        // HTTPResponse httpResponse = (HTTPResponse)
        // this.hosts.route(httpRequest.getMessageHeader("Host").getFieldValue(),
        // httpRequest);

        // httpResponse.messageBody(HTTPMessageBody.EMPTY);

        // return httpResponse;

        return null;
    }

    /**
     * Implementation of the HTTP POST method.
     * 
     * @param httpRequest
     *            The incoming HTTP request.
     * @return The HTTP response.
     */
    private HTTPResponse post(HTTPRequest httpRequest) {
        // return (HTTPResponse)
        // this.hosts.route(httpRequest.getMessageHeader("Host").getFieldValue(),
        // httpRequest);

        return null;
    }

    /**
     * Implementation of the HTTP PUT method.
     * 
     * @param httpRequest
     *            The incoming HTTP request.
     * @return The HTTP response.
     */
    private HTTPResponse put(HTTPRequest httpRequest) {
        throw new MethodNotImplementedException();
    }

    /**
     * Implementation of the HTTP DELETE method.
     * 
     * @param httpRequest
     *            The incoming HTTP request.
     * @return The HTTP response.
     */
    private HTTPResponse delete(HTTPRequest httpRequest) {
        throw new MethodNotImplementedException();
    }

    /**
     * Implementation of the HTTP TRACE method.
     * 
     * @param httpRequest
     *            The incoming HTTP request.
     * @return The HTTP response.
     */
    private HTTPResponse trace(HTTPRequest httpRequest) {
        throw new MethodNotImplementedException();
    }

    /**
     * Implementation of the HTTP CONNECT method.
     * 
     * @param httpRequest
     *            The incoming HTTP request.
     * @return The HTTP response.
     */
    private HTTPResponse connect(HTTPRequest httpRequest) {
        throw new MethodNotImplementedException();
    }

    private byte[] readLine(InputStream stream) throws IOException {
        byte newLine = '\n';
        byte[] line = new byte[0];
        int next;

        try {
            while ((next = stream.read()) >= 0) {
                byte[] old = line;

                line = new byte[old.length + 1];
                line[old.length] = (byte) next;

                System.arraycopy(old, 0, line, 0, old.length);

                if (next == newLine) {
                    break;
                }
            }
        } catch (IOException exception) {
            Console.warn(exception);
        }

        return line;
    }

}
