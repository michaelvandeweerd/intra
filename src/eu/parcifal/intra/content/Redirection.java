package eu.parcifal.intra.content;

import java.util.ArrayList;
import java.util.Collection;

import eu.parcifal.intra.http.HTTPMessageHeader;
import eu.parcifal.intra.http.HTTPStatusLine;
import eu.parcifal.plus.net.URI;

public class Redirection extends Content {

	private URI location;

	private boolean temporary;

	public Redirection(URI location, boolean temporary) {
		this.location = location;
		this.temporary = temporary;
	}

	public Redirection(String location, boolean temporary) {
		this(URI.fromString(location), temporary);
	}

	@Override
	protected HTTPStatusLine statusLine() {
		if (this.temporary) {
			return HTTPStatusLine.STATUS_307_1_1;
		} else {
			return HTTPStatusLine.STATUS_301_1_1;
		}
	}

	@Override
	protected Collection<HTTPMessageHeader> messageHeaders() {
		Collection<HTTPMessageHeader> messageHeaders = new ArrayList<HTTPMessageHeader>();

		messageHeaders.add(new HTTPMessageHeader("Location", this.location.toString()));

		return messageHeaders;
	}

}
