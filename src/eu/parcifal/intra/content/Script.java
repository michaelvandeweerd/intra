package eu.parcifal.intra.content;

import java.util.ArrayList;
import java.util.Collection;

import eu.parcifal.intra.http.HTTPMessageBody;
import eu.parcifal.intra.http.HTTPMessageHeader;

public class Script extends Content {

	private String location;

	public Script(String location) {
		this.location = location;
	}

	protected Collection<HTTPMessageHeader> messageHeaders() {
		Collection<HTTPMessageHeader> messageHeaders = new ArrayList<HTTPMessageHeader>();
		
		messageHeaders.add(new HTTPMessageHeader("Content-type", "application/json"));
		
		return messageHeaders;
	}

	@Override
	protected HTTPMessageBody messageBody() {
		String script = new String(this.load(this.location));

		return new HTTPMessageBody(this.run(script));
	}

}
