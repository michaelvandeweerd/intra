package eu.parcifal.intra.http;

import java.util.HashMap;
import java.util.Map;

import eu.parcifal.plus.data.Mappable;

public abstract class HTTPStartLine implements Mappable {

	protected HTTPVersion version;

	protected HTTPStartLine(HTTPVersion version) {
		this.version = version;
	}

	public HTTPVersion getVersion() {
		return this.version;
	}
	
	public byte[] toBytes() {
		return this.toString().getBytes();
	}

	public Map<String, Object> toMap() {
		Map<String, Object> startLine = new HashMap<String, Object>();
		
		startLine.put("version", this.version.toMap());
		
		return startLine;
	}

}
