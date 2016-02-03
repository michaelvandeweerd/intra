package eu.parcifal.intra.http;

public class HTTPMessageBody {

	public static final HTTPMessageBody EMPTY = new HTTPMessageBody("");

	private String contentBody;

	public HTTPMessageBody(String contentBody) {
		this.contentBody = contentBody;
	}

	public String getContentBody() {
		return this.contentBody;
	}
	
	public int size() {
		return this.contentBody.getBytes().length;
	}

	@Override
	public String toString() {
		return this.contentBody;
	}

}
