package eu.parcifal.intra.http;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTTPResponse extends HTTPMessage {

	public HTTPResponse(HTTPStatusLine statusLine, Collection<HTTPMessageHeader> messageHeaders,
			HTTPMessageBody messageBody) {
		super(statusLine, messageHeaders, messageBody);

		this.messageHeader("Content-Length", String.valueOf(this.messageBody.contentBody().length));
	}

	public HTTPResponse(HTTPStatusLine statusLine, Collection<HTTPMessageHeader> messageHeaders) {
		this(statusLine, messageHeaders, HTTPMessageBody.EMPTY);
	}

	public HTTPResponse(HTTPStatusLine statusLine, HTTPMessageBody messageBody) {
		this(statusLine, new ArrayList<HTTPMessageHeader>(), messageBody);
	}

	public HTTPResponse(HTTPStatusLine statusLine) {
		this(statusLine, new ArrayList<HTTPMessageHeader>());
	}

	public HTTPStatusLine statusLine() {
		return (HTTPStatusLine) this.startLine;
	}

	public void statusLine(HTTPStatusLine statusLine) {
		this.startLine = statusLine;
	}

	public void messageHeader(HTTPMessageHeader messageHeader) {
		if (this.messageHeaders.contains(messageHeader)) {
			this.messageHeaders.remove(messageHeader);
		}

		this.messageHeaders.add(messageHeader);
	}

	public void messageHeader(String fieldName, String contentValue) {
		this.messageHeader(new HTTPMessageHeader(fieldName, contentValue));
	}

	public void messageBody(HTTPMessageBody messageBody) {
		this.messageBody = messageBody;
		this.messageHeader("Content-Length", String.valueOf(this.messageBody.contentBody().length));
	}

	/**
	 * Set the content body of the message body of the current HTTP response.
	 * Also updates the value of the content length header.
	 * 
	 * @param contentBody
	 *            The new content body of the message body.
	 */
	public void messageBody(String contentBody) {
		this.messageBody(new HTTPMessageBody(contentBody));
	}

	public static HTTPResponse fromString(String raw) {
		Pattern pattern = Pattern.compile("(.*)\r?\n((?:.*\r?\n)*)\r?\n(.*)?");
		Matcher matcher = pattern.matcher(raw);

		if (matcher.find()) {
			HTTPStatusLine statusLine = HTTPStatusLine.fromString(matcher.group(1));
			Collection<HTTPMessageHeader> messageHeaders = HTTPMessageHeader.fromString(matcher.group(2));
			HTTPMessageBody messageBody = new HTTPMessageBody(matcher.group(3));

			return new HTTPResponse(statusLine, messageHeaders, messageBody);
		} else {
			throw new RuntimeException();
		}
	}

}
