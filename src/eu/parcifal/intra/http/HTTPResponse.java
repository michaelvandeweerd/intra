package eu.parcifal.intra.http;

import java.util.ArrayList;
import java.util.Collection;

public class HTTPResponse extends HTTPMessage {

	public HTTPResponse(HTTPStatusLine statusLine, Collection<HTTPMessageHeader> messageHeaders,
			HTTPMessageBody messageBody) {
		super(statusLine, messageHeaders, messageBody);

		this.messageHeader(
				new HTTPMessageHeader("Content-Length", String.valueOf(this.messageBody.getContentBody().length())));
	}

	public HTTPResponse(HTTPStatusLine statusLine, Collection<HTTPMessageHeader> messageHeaders) {
		this(statusLine, messageHeaders, HTTPMessageBody.EMPTY);
	}

	public HTTPResponse(HTTPStatusLine statusLine) {
		this(statusLine, new ArrayList<HTTPMessageHeader>());
	}

	public void messageHeader(HTTPMessageHeader messageHeader) {
		if(this.messageHeaders.contains(messageHeader)) {
			this.messageHeaders.remove(messageHeader);
		}
		
		this.messageHeaders.add(messageHeader);
	}

	public void messageBody(String contentBody) {
		this.messageBody = new HTTPMessageBody(contentBody);
	}

}
