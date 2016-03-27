package eu.parcifal.intra.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.parcifal.plus.net.URI;

public class HTTPRequestLine extends HTTPStartLine {

	private final static String STRING_FORMAT = "%1$s %2$s %3$s\r\n";

	private String method;

	private URI requestURI;

	public HTTPRequestLine(String method, URI requestURI, HTTPVersion version) {
		super(version);

		this.method = method;
		this.requestURI = requestURI;
	}

	public String method() {
		return this.method;
	}

	public URI requestURI() {
		return this.requestURI;
	}

	public static HTTPRequestLine fromString(String raw) {
		Pattern pattern = Pattern.compile("^([^ ]+) ([^ ]+) ([^ ]+)$");
		Matcher matcher = pattern.matcher(raw);

		if (matcher.find()) {
			String method = matcher.group(1);
			URI requestURI = URI.fromString(matcher.group(2));
			HTTPVersion version = HTTPVersion.fromString(matcher.group(3));

			return new HTTPRequestLine(method, requestURI, version);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	@Override
	public String toString() {
		return String.format(STRING_FORMAT, this.method, this.requestURI, this.version);
	}

}
