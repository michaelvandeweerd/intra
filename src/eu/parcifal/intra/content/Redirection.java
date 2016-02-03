package eu.parcifal.intra.content;

import java.util.ArrayList;
import java.util.Collection;

import eu.parcifal.intra.http.HTTPMessageHeader;
import eu.parcifal.intra.http.HTTPStatusLine;
import eu.parcifal.plus.net.URI;

public class Redirection extends Context {

	private URI location;

	public Redirection(URI location) {
		this.location = location;
	}

	public Redirection(String location) {
		this(URI.fromString(location));
	}

	@Override
	protected HTTPStatusLine statusLine() {
		return HTTPStatusLine.STATUS_303_1_1;
	}

	@Override
	protected Collection<HTTPMessageHeader> messageHeaders() {
		Collection<HTTPMessageHeader> messageHeaders = new ArrayList<HTTPMessageHeader>();

		messageHeaders.add(new HTTPMessageHeader("Location", this.location.toString()));

		return messageHeaders;
	}

}
