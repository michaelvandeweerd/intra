package eu.parcifal.intra.http;

public class HTTPMessageBody {

	public static final HTTPMessageBody EMPTY = new HTTPMessageBody("");

	private String contentBody;

	public HTTPMessageBody(String contentBody) {
		this.contentBody = contentBody;
	}

	public String contentBody() {
		return this.contentBody;
	}

	@Override
	public String toString() {
		return this.contentBody;
	}

}
