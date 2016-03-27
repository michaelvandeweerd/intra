package eu.parcifal.intra.content;

import java.util.ArrayList;
import java.util.Collection;

import eu.parcifal.intra.http.HTTPMessageBody;
import eu.parcifal.intra.http.HTTPMessageHeader;
import eu.parcifal.intra.http.HTTPRequest;
import eu.parcifal.intra.http.HTTPResponse;
import eu.parcifal.intra.http.HTTPStatusLine;
import eu.parcifal.plus.logic.Executable;

public abstract class Content implements Executable {

	protected HTTPRequest request;

	@Override
	public HTTPResponse execute(Object... args) {
		if (args.length < 1 || !(args[0] instanceof HTTPRequest)) {
			throw new IllegalArgumentException();
		} else {
			this.request = (HTTPRequest) args[0];

			return this.response();
		}
	}

	protected HTTPResponse response() {
		return new HTTPResponse(this.statusLine(), this.messageHeaders(), this.messageBody());
	}

	protected HTTPStatusLine statusLine() {
		return HTTPStatusLine.STATUS_200_1_1;
	}

	protected Collection<HTTPMessageHeader> messageHeaders() {
		return new ArrayList<HTTPMessageHeader>();
	}

	protected HTTPMessageBody messageBody() {
		return HTTPMessageBody.EMPTY;
	}

}
