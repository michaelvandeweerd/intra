package eu.parcifal.intra.http;

import java.io.UnsupportedEncodingException;

import eu.parcifal.plus.logic.RouteNotFoundException;
import eu.parcifal.plus.logic.Router;
import eu.parcifal.plus.net.Exchanger;

public class HTTPExchanger extends Exchanger {

	public final static String DEFAULT_ENCODING = "ISO-8859-1";

	private Router router;

	private String encoding;

	public HTTPExchanger(Router router, String encoding) {
		this.router = router;
		this.encoding = encoding;
	}

	public HTTPExchanger(Router router) {
		this(router, DEFAULT_ENCODING);
	}

	@Override
	protected byte[] response(byte[] request) {
		HTTPResponse httpResponse;

		try {
			HTTPRequest httpRequest = HTTPRequest.fromString(new String(request, this.encoding));

			switch (httpRequest.requestLine().method()) {
			case "GET":
				httpResponse = (HTTPResponse) this.router.route(httpRequest.messageHeader("Host").fieldValue(),
						httpRequest);
				break;
			case "HEAD":
				httpResponse = (HTTPResponse) this.router.route(httpRequest.messageHeader("Host").fieldValue(),
						httpRequest);
				httpResponse.messageBody("");
				break;
			default:
				httpResponse = new HTTPResponse(HTTPStatusLine.STATUS_405_1_1);
			}
		} catch (UnsupportedEncodingException exception) {
			httpResponse = new HTTPResponse(HTTPStatusLine.STATUS_415_1_1);
		} catch (RouteNotFoundException | IllegalArgumentException exception) {
			exception.printStackTrace();
			httpResponse = new HTTPResponse(HTTPStatusLine.STATUS_404_1_1);
		} catch (RuntimeException exception) {
			httpResponse = new HTTPResponse(HTTPStatusLine.STATUS_500_1_1);
		}

		return httpResponse.toString().getBytes();
	}

}
