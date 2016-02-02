package eu.parcifal.intra.http;

public class HTTPStatusLine extends HTTPStartLine {

	private final static String STRING_FORMAT = "%1$s %2$s %3$s";

	public final static HTTPStatusLine STATUS_100_1_1 = new HTTPStatusLine(100, "Continue", HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_101_1_1 = new HTTPStatusLine(100, "Switching Protocols",
			HTTPVersion.VERSION_1_1);

	public final static HTTPStatusLine STATUS_200_1_1 = new HTTPStatusLine(200, "OK", HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_201_1_1 = new HTTPStatusLine(201, "Created", HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_202_1_1 = new HTTPStatusLine(202, "Accepted", HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_203_1_1 = new HTTPStatusLine(203, "Non-Authoritative Information",
			HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_204_1_1 = new HTTPStatusLine(204, "No Content", HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_205_1_1 = new HTTPStatusLine(205, "Reset Content",
			HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_206_1_1 = new HTTPStatusLine(206, "Partial Content",
			HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_300_1_1 = new HTTPStatusLine(300, "Multiple Choices",
			HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_301_1_1 = new HTTPStatusLine(301, "Moved Permanently",
			HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_302_1_1 = new HTTPStatusLine(302, "Found", HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_303_1_1 = new HTTPStatusLine(303, "See Other", HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_304_1_1 = new HTTPStatusLine(304, "Not Modified",
			HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_305_1_1 = new HTTPStatusLine(305, "Use Proxy", HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_307_1_1 = new HTTPStatusLine(307, "Temporary Redirect",
			HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_400_1_1 = new HTTPStatusLine(400, "Bad Request", HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_401_1_1 = new HTTPStatusLine(401, "Unauthorized",
			HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_402_1_1 = new HTTPStatusLine(402, "Payment Required",
			HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_403_1_1 = new HTTPStatusLine(403, "Forbidden", HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_404_1_1 = new HTTPStatusLine(404, "Not Found", HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_405_1_1 = new HTTPStatusLine(405, "Method Not Allowed",
			HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_406_1_1 = new HTTPStatusLine(406, "Not Acceptable",
			HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_407_1_1 = new HTTPStatusLine(407, "Proxy Authentication Required",
			HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_408_1_1 = new HTTPStatusLine(408, "Request Timeout",
			HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_409_1_1 = new HTTPStatusLine(409, "Conflict", HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_410_1_1 = new HTTPStatusLine(410, "Gone", HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_411_1_1 = new HTTPStatusLine(411, "Length Required",
			HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_412_1_1 = new HTTPStatusLine(412, "Precondition Failed",
			HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_413_1_1 = new HTTPStatusLine(413, "Request Entity Too Large",
			HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_414_1_1 = new HTTPStatusLine(414, "Request-URI Too Long",
			HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_415_1_1 = new HTTPStatusLine(415, "Unsupported Media Type",
			HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_416_1_1 = new HTTPStatusLine(416, "Requested Range Not Satisfiable",
			HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_417_1_1 = new HTTPStatusLine(417, "Expectation Failed",
			HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_500_1_1 = new HTTPStatusLine(500, "Internal Server Error",
			HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_501_1_1 = new HTTPStatusLine(501, "Not Implemented",
			HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_502_1_1 = new HTTPStatusLine(502, "Bad Gateway", HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_503_1_1 = new HTTPStatusLine(503, "Service Unavailable",
			HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_504_1_1 = new HTTPStatusLine(504, "Gateway Timeout",
			HTTPVersion.VERSION_1_1);
	public final static HTTPStatusLine STATUS_505_1_1 = new HTTPStatusLine(505, "HTTP Version Not Supported",
			HTTPVersion.VERSION_1_1);

	private int statusCode;

	private String reasonPhrase;

	public HTTPStatusLine(int statusCode, String reasonPhrase, HTTPVersion version) {
		super(version);

		this.statusCode = statusCode;
		this.reasonPhrase = reasonPhrase;
	}

	public int getStatusCode() {
		return this.statusCode;
	}

	public String getReasonPhrase() {
		return this.reasonPhrase;
	}

	@Override
	public String toString() {
		return String.format(STRING_FORMAT, this.version, this.statusCode, this.reasonPhrase);
	}

}
