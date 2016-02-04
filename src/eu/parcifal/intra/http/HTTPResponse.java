package eu.parcifal.intra.http;

import java.util.ArrayList;
import java.util.Collection;

public class HTTPResponse extends HTTPMessage {

	public HTTPResponse(HTTPStatusLine statusLine, Collection<HTTPMessageHeader> messageHeaders,
			HTTPMessageBody messageBody) {
		super(statusLine, messageHeaders, messageBody);

		this.messageHeader("Content-Length", String.valueOf(this.messageBody.contentBody().length));
	}

	public HTTPResponse(HTTPStatusLine statusLine, Collection<HTTPMessageHeader> messageHeaders) {
		this(statusLine, messageHeaders, HTTPMessageBody.EMPTY);
	}

	public HTTPResponse(HTTPStatusLine statusLine) {
		this(statusLine, new ArrayList<HTTPMessageHeader>());
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

}
