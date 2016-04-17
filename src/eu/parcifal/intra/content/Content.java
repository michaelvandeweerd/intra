package eu.parcifal.intra.content;

import java.util.ArrayList;
import java.util.Collection;

import eu.parcifal.intra.http.HTTPMessageBody;
import eu.parcifal.intra.http.HTTPMessageHeader;
import eu.parcifal.intra.http.HTTPRequest;
import eu.parcifal.intra.http.HTTPResponse;
import eu.parcifal.intra.http.HTTPStatusLine;
import eu.parcifal.plus.logic.Executable;

public abstract class Content implements Executable {

    @Override
    public HTTPResponse execute(Object... args) {
        if (args.length < 1 || !(args[0] instanceof HTTPRequest)) {
            throw new IllegalArgumentException();
        } else {
            HTTPRequest request = (HTTPRequest) args[0];

            return this.getResponse(request);
        }
    }

    protected HTTPResponse getResponse(HTTPRequest request) {
        HTTPResponse response = new HTTPResponse(this.getStatusLine(request));

        response.setMessageHeaders(this.getMessageHeaders(request));
        response.setMessageBody(this.getMessageBody(request));

        return response;
    }

    protected HTTPStatusLine getStatusLine(HTTPRequest request) {
        return HTTPStatusLine.STATUS_200_1_1;
    }

    protected Collection<HTTPMessageHeader> getMessageHeaders(HTTPRequest request) {
        return new ArrayList<HTTPMessageHeader>();
    }

    protected HTTPMessageBody getMessageBody(HTTPRequest request) {
        return new HTTPMessageBody();
    }

}
