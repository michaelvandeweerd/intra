package eu.parcifal.intra.http;

public class HTTPMessageBody {

	public static final HTTPMessageBody EMPTY = new HTTPMessageBody("");

	private byte[] contentBody;

	public HTTPMessageBody(byte[] contentBody) {
		this.contentBody = contentBody;
	}

	public HTTPMessageBody(Object contentBody) {
		this.contentBody = contentBody.toString().getBytes();
	}

	public byte[] contentBody() {
		return this.contentBody;
	}

	public byte[] toBytes() {
		return this.contentBody;
	}

	@Override
	public String toString() {
		return new String(this.contentBody);
	}

}
