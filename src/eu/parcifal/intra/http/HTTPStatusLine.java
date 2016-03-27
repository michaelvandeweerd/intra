package eu.parcifal.intra.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTTPStatusLine extends HTTPStartLine {

	private final static String STRING_FORMAT = "%1$s %2$s %3$s\r\n";

	public final static HTTPStatusLine STATUS_100_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 100, "Continue");
	public final static HTTPStatusLine STATUS_101_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 100,
			"Switching Protocols");

	public final static HTTPStatusLine STATUS_200_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 200, "OK");
	public final static HTTPStatusLine STATUS_201_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 201, "Created");
	public final static HTTPStatusLine STATUS_202_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 202, "Accepted");
	public final static HTTPStatusLine STATUS_203_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 203,
			"Non-Authoritative Information");
	public final static HTTPStatusLine STATUS_204_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 204, "No Content");
	public final static HTTPStatusLine STATUS_205_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 205,
			"Reset Content");
	public final static HTTPStatusLine STATUS_206_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 206,
			"Partial Content");
	public final static HTTPStatusLine STATUS_300_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 300,
			"Multiple Choices");
	public final static HTTPStatusLine STATUS_301_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 301,
			"Moved Permanently");
	public final static HTTPStatusLine STATUS_302_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 302, "Found");
	public final static HTTPStatusLine STATUS_303_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 303, "See Other");
	public final static HTTPStatusLine STATUS_304_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 304,
			"Not Modified");
	public final static HTTPStatusLine STATUS_305_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 305, "Use Proxy");
	public final static HTTPStatusLine STATUS_307_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 307,
			"Temporary Redirect");
	public final static HTTPStatusLine STATUS_400_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 400, "Bad Request");
	public final static HTTPStatusLine STATUS_401_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 401,
			"Unauthorized");
	public final static HTTPStatusLine STATUS_402_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 402,
			"Payment Required");
	public final static HTTPStatusLine STATUS_403_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 403, "Forbidden");
	public final static HTTPStatusLine STATUS_404_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 404, "Not Found");
	public final static HTTPStatusLine STATUS_405_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 405,
			"Method Not Allowed");
	public final static HTTPStatusLine STATUS_406_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 406,
			"Not Acceptable");
	public final static HTTPStatusLine STATUS_407_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 407,
			"Proxy Authentication Required");
	public final static HTTPStatusLine STATUS_408_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 408,
			"Request Timeout");
	public final static HTTPStatusLine STATUS_409_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 409, "Conflict");
	public final static HTTPStatusLine STATUS_410_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 410, "Gone");
	public final static HTTPStatusLine STATUS_411_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 411,
			"Length Required");
	public final static HTTPStatusLine STATUS_412_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 412,
			"Precondition Failed");
	public final static HTTPStatusLine STATUS_413_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 413,
			"Request Entity Too Large");
	public final static HTTPStatusLine STATUS_414_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 414,
			"Request-URI Too Long");
	public final static HTTPStatusLine STATUS_415_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 415,
			"Unsupported Media Type");
	public final static HTTPStatusLine STATUS_416_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 416,
			"Requested Range Not Satisfiable");
	public final static HTTPStatusLine STATUS_417_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 417,
			"Expectation Failed");
	public final static HTTPStatusLine STATUS_500_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 500,
			"Internal Server Error");
	public final static HTTPStatusLine STATUS_501_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 501,
			"Not Implemented");
	public final static HTTPStatusLine STATUS_502_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 502, "Bad Gateway");
	public final static HTTPStatusLine STATUS_503_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 503,
			"Service Unavailable");
	public final static HTTPStatusLine STATUS_504_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 504,
			"Gateway Timeout");
	public final static HTTPStatusLine STATUS_505_1_1 = new HTTPStatusLine(HTTPVersion.VERSION_1_1, 505,
			"HTTP Version Not Supported");

	private int statusCode;

	private String reasonPhrase;

	public HTTPStatusLine(HTTPVersion version, int statusCode, String reasonPhrase) {
		super(version);

		this.statusCode = statusCode;
		this.reasonPhrase = reasonPhrase;
	}

	public int statusCode() {
		return this.statusCode;
	}

	public String getReasonPhrase() {
		return this.reasonPhrase;
	}

	@Override
	public String toString() {
		return String.format(STRING_FORMAT, this.version, this.statusCode, this.reasonPhrase);
	}

	public static HTTPStatusLine fromString(String raw) {
		Pattern pattern = Pattern.compile("^([^ ]+) ([^ ]+) ([^ ]+)$");
		Matcher matcher = pattern.matcher(raw);

		if (matcher.find()) {
			HTTPVersion version = HTTPVersion.fromString(matcher.group(1));
			int statusCode = Integer.parseInt(matcher.group(2));
			String reasonPhrase = matcher.group(3);

			return new HTTPStatusLine(version, statusCode, reasonPhrase);
		} else {
			throw new IllegalArgumentException();
		}
	}

}
