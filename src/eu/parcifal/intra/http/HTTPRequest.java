package eu.parcifal.intra.http;

import java.util.Collection;
import java.util.Map;
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

	public HTTPMessageHeader messageHeader(String fieldName) {
		for (HTTPMessageHeader messageHeader : this.messageHeaders) {
			if (messageHeader.fieldName().equals(fieldName)) {
				return messageHeader;
			}
		}

		throw new IllegalArgumentException();
	}

	public String post(String name) {
		if (this.messageHeader("Content-Type").fieldValue().equals("x-www-form-urlencoded")) {
			Pattern pattern = Pattern.compile(String.format("%1$s=([^&]*)", name));
			Matcher matcher = pattern.matcher(this.messageBody.toString());

			if (matcher.find()) {
				return matcher.group(1);
			} else {
				return null;
			}
		} else {
			throw new RuntimeException();
		}
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
	
	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		
		map.put("requestLine", this.requestLine().toMap());
		
		return map;
	}

}
