package eu.parcifal.intra.http;

import java.util.Collection;
import java.util.StringJoiner;

/**
 * HTTP messages consist of requests from client to server and responses from
 * server to client.
 * 
 * @author Michaël van de Weerd
 * @see https://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#4.1
 */
public abstract class HTTPMessage {
	/**
	 * The format of the string representation of an HTTP message.
	 */
	private final static String STRING_FORMAT = "%1$s\r\n%2$s\r\n\r\n%3$s";

	/**
	 * The start-line of the current HTTP message.
	 */
	protected HTTPStartLine startLine;

	/**
	 * The headers of the current HTTP message.
	 */
	protected Collection<HTTPMessageHeader> messageHeaders;

	/**
	 * The body of the current HTTP message.
	 */
	protected HTTPMessageBody messageBody;

	protected HTTPMessage(HTTPStartLine startLine, Collection<HTTPMessageHeader> messageHeaders,
			HTTPMessageBody messageBody) {
		this.startLine = startLine;
		this.messageHeaders = messageHeaders;
		this.messageBody = messageBody;
	}

	public Collection<HTTPMessageHeader> getMessageHeaders() {
		return this.messageHeaders;
	}

	public HTTPMessageBody getMessageBody() {
		return this.messageBody;
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner("\r\n");

		for (HTTPMessageHeader messageHeader : this.messageHeaders) {
			joiner.add(messageHeader.toString());
		}

		return String.format(STRING_FORMAT, this.startLine, joiner, this.messageBody);
	}

}
