package eu.parcifal.intra.http;

public class HTTPResponse extends HTTPMessage {
    
    public HTTPResponse(HTTPStatusLine statusLine) {
        super(statusLine);
    }

	public HTTPStatusLine getStatusLine() {
		return (HTTPStatusLine) super.getStartLine();
	}

	public void setStatusLine(HTTPStatusLine statusLine) {
		super.setStartLine(statusLine);
	}

//	public static HTTPResponse fromString(String raw) {
//		Pattern pattern = Pattern.compile("(.*)\r?\n((?:.*\r?\n)*)\r?\n(.*)?");
//		Matcher matcher = pattern.matcher(raw);
//
//		if (matcher.find()) {
//			HTTPStatusLine statusLine = HTTPStatusLine.fromString(matcher.group(1));
//			Collection<HTTPMessageHeader> messageHeaders = HTTPMessageHeader.fromString(matcher.group(2));
//			HTTPMessageBody messageBody = new HTTPMessageBody(matcher.group(3));
//
//			return new HTTPResponse(statusLine, messageHeaders, messageBody);
//		} else {
//			throw new RuntimeException();
//		}
//	}

}
