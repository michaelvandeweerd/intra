package eu.parcifal.intra.http;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import eu.parcifal.plus.data.Mappable;

/**
 * HTTP messages consist of requests from client to server and responses from
 * server to client.
 * 
 * @author Michaël van de Weerd
 * @see https://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#4.1
 */
public abstract class HTTPMessage implements Mappable {
	/**
	 * The format of the string representation of an HTTP message.
	 */
	private final static String STRING_FORMAT = "%1$s%2$s\r\n%3$s";

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

	public byte[] toBytes() {
		byte[] startLine = this.startLine.toBytes();
		byte[] messageHeaders = new byte[0];
		byte[] emptyLine = "\r\n".getBytes();
		byte[] messageBody = this.messageBody.toBytes();

		for (HTTPMessageHeader messsageHeader : this.messageHeaders) {
			byte[] messageHeader = messsageHeader.toBytes();
			byte[] concatination = new byte[messageHeaders.length + messageHeader.length];

			System.arraycopy(messageHeaders, 0, concatination, 0, messageHeaders.length);
			System.arraycopy(messageHeader, 0, concatination, messageHeaders.length, messageHeader.length);

			messageHeaders = concatination;
		}

		byte[] message = new byte[startLine.length + messageHeaders.length + emptyLine.length + messageBody.length];

		System.arraycopy(startLine, 0, message, 0, startLine.length);
		System.arraycopy(messageHeaders, 0, message, startLine.length, messageHeaders.length);
		System.arraycopy(emptyLine, 0, message, startLine.length + messageHeaders.length, emptyLine.length);
		System.arraycopy(messageBody, 0, message, startLine.length + messageHeaders.length + emptyLine.length,
				messageBody.length);

		return message;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> message = new HashMap<String, Object>();
		Map<String, Object> messageHeaders = new HashMap<String, Object>();

		for (HTTPMessageHeader messageHeader : this.messageHeaders) {
			messageHeaders.put(messageHeader.fieldName(), messageHeader.fieldValue());
		}

		message.put("startLine", this.startLine.toMap());
		message.put("messageHeaders", messageHeaders);
		message.put("messageBody", this.messageBody.toMap());

		return message;
	}

	@Override
	public String toString() {
		String messageHeaders = "";

		for (HTTPMessageHeader messageHeader : this.messageHeaders) {
			messageHeaders += messageHeader.toString();
		}

		return String.format(STRING_FORMAT, this.startLine, messageHeaders, this.messageBody);
	}

}
