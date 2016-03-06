package eu.parcifal.intra.http;

import java.util.HashMap;
import java.util.Map;

import eu.parcifal.plus.data.Mappable;

public class HTTPMessageBody implements Mappable {

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

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("contentBody", new String(this.contentBody));
		
		return map;
	}

}
