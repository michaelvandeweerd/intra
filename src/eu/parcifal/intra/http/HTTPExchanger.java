package eu.parcifal.intra.http;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import eu.parcifal.plus.MethodNotImplementedException;
import eu.parcifal.plus.logic.RouteNotFoundException;
import eu.parcifal.plus.logic.Router;
import eu.parcifal.plus.net.Exchanger;

/**
 * Responds to incoming HTTP request.
 * 
 * @author Michaël van de Weerd
 */
public class HTTPExchanger extends Exchanger {
	/**
	 * The value of the server HTTP header in the response.
	 */
	private final static String SERVER_SIGNATURE = "parcifal-intra";

	/**
	 * The default encoding of the HTTP response.
	 */
	public final static String DEFAULT_ENCODING = "ISO-8859-1";

	/**
	 * The router used containing available hosts.
	 */
	private Router router;

	/**
	 * The encoding of the HTTP response.
	 */
	private String encoding;

	/**
	 * Construct a new HTTP exchange.
	 * 
	 * @param router
	 *            The router containing available hosts.
	 * @param encoding
	 *            The encoding of the HTTP response.
	 */
	public HTTPExchanger(Router router, String encoding) {
		this.router = router;
		this.encoding = encoding;
	}

	/**
	 * Construct a new HTTP exchange using the default encoding.
	 * 
	 * @param router
	 *            The router containing available hosts.
	 */
	public HTTPExchanger(Router router) {
		this(router, DEFAULT_ENCODING);
	}

	@Override
	protected byte[] response(byte[] request) {
		HTTPResponse httpResponse = null;

		try {
			HTTPRequest httpRequest = HTTPRequest.fromString(new String(request, this.encoding));

			switch (httpRequest.requestLine().method()) {
			case "OPTIONS":
				httpResponse = this.options(httpRequest);
				break;
			case "GET":
				httpResponse = this.get(httpRequest);
				break;
			case "HEAD":
				httpResponse = this.head(httpRequest);
				break;
			case "POST":
				httpResponse = this.post(httpRequest);
				break;
			case "PUT":
				httpResponse = this.put(httpRequest);
				break;
			case "DELETE":
				httpResponse = this.delete(httpRequest);
				break;
			case "TRACE":
				httpResponse = this.trace(httpRequest);
				break;
			case "CONNECT":
				httpResponse = this.connect(httpRequest);
				break;
			}
		} catch (RouteNotFoundException | IllegalArgumentException exception) {
			httpResponse = new HTTPResponse(HTTPStatusLine.STATUS_404_1_1);
		} catch (MethodNotImplementedException exception) {
			httpResponse = new HTTPResponse(HTTPStatusLine.STATUS_405_1_1);
		} catch (UnsupportedEncodingException exception) {
			httpResponse = new HTTPResponse(HTTPStatusLine.STATUS_415_1_1);
		} catch (RuntimeException exception) {
			httpResponse = new HTTPResponse(HTTPStatusLine.STATUS_500_1_1);
		}

		DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'");

		format.setTimeZone(TimeZone.getTimeZone("GMT"));

		httpResponse.messageHeader(HTTPMessageHeader.FIELD_NAME_SERVER, SERVER_SIGNATURE);
		httpResponse.messageHeader(HTTPMessageHeader.FIELD_NAME_DATE, format.format(new Date()));

		return httpResponse.toBytes();
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
	 * @param httpRequest
	 *            The incoming HTTP request.
	 * @return The HTTP response.
	 */
	private HTTPResponse get(HTTPRequest httpRequest) {
		return (HTTPResponse) this.router.route(httpRequest.messageHeader("Host").fieldValue(), httpRequest);
	}

	/**
	 * Implementation of the HTTP HEAD method.
	 * 
	 * @param httpRequest
	 *            The incoming HTTP request.
	 * @return The HTTP response.
	 */
	private HTTPResponse head(HTTPRequest httpRequest) {
		HTTPResponse httpResponse = (HTTPResponse) this.router.route(httpRequest.messageHeader("Host").fieldValue(),
				httpRequest);

		httpResponse.messageBody(HTTPMessageBody.EMPTY);

		return httpResponse;
	}

	/**
	 * Implementation of the HTTP POST method.
	 * 
	 * @param httpRequest
	 *            The incoming HTTP request.
	 * @return The HTTP response.
	 */
	private HTTPResponse post(HTTPRequest httpRequest) {
		return (HTTPResponse) this.router.route(httpRequest.messageHeader("Host").fieldValue(), httpRequest);
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

}
