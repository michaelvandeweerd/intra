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

public class HTTPExchanger extends Exchanger {

	private final static String SERVER_SIGNATURE = "phv";

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
		} catch (MethodNotImplementedException exception) {
			httpResponse = new HTTPResponse(HTTPStatusLine.STATUS_405_1_1);
		} catch (RouteNotFoundException | IllegalArgumentException exception) {
			exception.printStackTrace();
			httpResponse = new HTTPResponse(HTTPStatusLine.STATUS_404_1_1);
		} catch (UnsupportedEncodingException exception) {
			httpResponse = new HTTPResponse(HTTPStatusLine.STATUS_415_1_1);
		} catch (RuntimeException exception) {
			httpResponse = new HTTPResponse(HTTPStatusLine.STATUS_500_1_1);
		}

		DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'");

		format.setTimeZone(TimeZone.getTimeZone("GMT"));

		httpResponse.messageHeader(HTTPMessageHeader.FIELD_NAME_SERVER, SERVER_SIGNATURE);
		httpResponse.messageHeader(HTTPMessageHeader.FIELD_NAME_DATE, format.format(new Date()));

		return httpResponse.toString().getBytes();
	}

	private HTTPResponse options(HTTPRequest httpRequest) {
		throw new MethodNotImplementedException();
	}

	private HTTPResponse get(HTTPRequest httpRequest) {
		return (HTTPResponse) this.router.route(httpRequest.messageHeader("Host").fieldValue(), httpRequest);
	}

	private HTTPResponse head(HTTPRequest httpRequest) {
		HTTPResponse httpResponse = (HTTPResponse) this.router.route(httpRequest.messageHeader("Host").fieldValue(),
				httpRequest);

		httpResponse.messageBody(HTTPMessageBody.EMPTY);

		return httpResponse;
	}

	private HTTPResponse post(HTTPRequest httpRequest) {
		return (HTTPResponse) this.router.route(httpRequest.messageHeader("Host").fieldValue(), httpRequest);
	}

	private HTTPResponse put(HTTPRequest httpRequest) {
		throw new MethodNotImplementedException();
	}

	private HTTPResponse delete(HTTPRequest httpRequest) {
		throw new MethodNotImplementedException();
	}

	private HTTPResponse trace(HTTPRequest httpRequest) {
		throw new MethodNotImplementedException();
	}

	private HTTPResponse connect(HTTPRequest httpRequest) {
		throw new MethodNotImplementedException();
	}

}
