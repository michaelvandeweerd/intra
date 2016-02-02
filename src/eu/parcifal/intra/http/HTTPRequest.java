package eu.parcifal.intra.http;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTTPRequest extends HTTPMessage {

	public HTTPRequest(HTTPRequestLine requestLine, Collection<HTTPMessageHeader> messageHeaders,
			HTTPMessageBody messageBody) {
		super(requestLine, messageHeaders, messageBody);
	}

	public HTTPRequestLine requestLine() {
		return (HTTPRequestLine) this.startLine;
	}

	public static HTTPRequest fromString(String raw) {
		Pattern pattern = Pattern.compile("(.*)\r?\n((?:.*\r?\n)*)\r?\n(.*)?");
		Matcher matcher = pattern.matcher(raw);

		if (matcher.find()) {
			HTTPRequestLine requestLine = HTTPRequestLine.fromString(matcher.group(1));
			Collection<HTTPMessageHeader> messageHeaders = HTTPMessageHeader.fromString(matcher.group(2));
			HTTPMessageBody messageBody = new HTTPMessageBody(matcher.group(3));

			return new HTTPRequest(requestLine, messageHeaders, messageBody);
		} else {
			throw new RuntimeException();
		}
	}

	public HTTPMessageHeader messageHeader(String fieldName) {
		for (HTTPMessageHeader messageHeader : this.messageHeaders) {
			if (messageHeader.getFieldName().equals(fieldName)) {
				return messageHeader;
			}
		}

		throw new IllegalArgumentException();
	}

}
