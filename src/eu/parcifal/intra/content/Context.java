package eu.parcifal.intra.content;

import java.util.ArrayList;
import java.util.Collection;

import eu.parcifal.intra.http.HTTPMessageBody;
import eu.parcifal.intra.http.HTTPMessageHeader;
import eu.parcifal.intra.http.HTTPRequest;
import eu.parcifal.intra.http.HTTPResponse;
import eu.parcifal.intra.http.HTTPStatusLine;
import eu.parcifal.plus.logic.Executable;

public abstract class Context implements Executable {

	protected HTTPRequest request;

	@Override
	public HTTPResponse execute(Object... args) {
		if (args.length < 1 || !(args[0] instanceof HTTPRequest)) {
			return new HTTPResponse(HTTPStatusLine.STATUS_500_1_1);
		} else {
			this.request = (HTTPRequest) args[0];

			return new HTTPResponse(this.getStatusLine(), this.getMessageHeaders(), this.getMessageBody());
		}
	}

	protected HTTPStatusLine getStatusLine() {
		return HTTPStatusLine.STATUS_200_1_1;
	}

	protected Collection<HTTPMessageHeader> getMessageHeaders() {
		return new ArrayList<HTTPMessageHeader>();
	}

	protected HTTPMessageBody getMessageBody() {
		return HTTPMessageBody.EMPTY;
	}

}
