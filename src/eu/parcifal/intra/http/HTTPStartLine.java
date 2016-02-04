package eu.parcifal.intra.http;

public abstract class HTTPStartLine {

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

}
