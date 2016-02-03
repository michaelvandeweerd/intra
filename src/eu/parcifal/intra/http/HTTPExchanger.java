package eu.parcifal.intra.http;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import eu.parcifal.plus.logic.RouteNotFoundException;
import eu.parcifal.plus.logic.Router;
import eu.parcifal.plus.net.Exchanger;
import eu.parcifal.plus.print.Console;

public class HTTPExchanger extends Exchanger {

	private final static String SERVER_NAME = "Parcifal";

	private final static String SERVER_VERSION = "1.0";

	private final static String SERVER_SIGNATURE = "%1$s/%2$s";

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
			case "GET":
				httpResponse = this.get(httpRequest);
				break;
			case "HEAD":
				httpResponse = this.head(httpRequest);
				break;
			case "POST":
				httpResponse = this.post(httpRequest);
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

		DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'");

		format.setTimeZone(TimeZone.getTimeZone("GMT"));

		httpResponse.messageHeader("Server", String.format(SERVER_SIGNATURE, SERVER_NAME, SERVER_VERSION));
		httpResponse.messageHeader("Date", format.format(new Date()));
		
		Console.log(httpResponse.toString());

		return httpResponse.toString().getBytes();
	}

	private HTTPResponse get(HTTPRequest httpRequest) {
		return (HTTPResponse) this.router.route(httpRequest.messageHeader("Host").fieldValue(), httpRequest);
	}

	private HTTPResponse head(HTTPRequest httpRequest) {
		HTTPResponse httpResponse = (HTTPResponse) this.router.route(httpRequest.messageHeader("Host").fieldValue(),
				httpRequest);

		httpResponse.messageBody("");

		return httpResponse;
	}

	private HTTPResponse post(HTTPRequest httpRequest) {
		Console.log("post");
		return null;
	}

}
