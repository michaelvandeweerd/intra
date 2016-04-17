package eu.parcifal.intra.http;

public class HTTPRequest extends HTTPMessage {
    
    public HTTPRequest(HTTPRequestLine requestLine) {
        super(requestLine);
    }

    public HTTPRequestLine getRequestLine() {
        return (HTTPRequestLine) super.getStartLine();
    }

    public void setRequestLine(HTTPRequestLine requestLine) {
        super.setStartLine(requestLine);
    }

}
