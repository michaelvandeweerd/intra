package eu.parcifal.intra.http;

import eu.parcifal.plus.print.Console;

public class HTTPMessageBody {
    /**
     * The content-body of the current HTTP-message-body. Final in order to
     * force a new HTTP-message-body to be set in the HTTP-message and thus
     * making sure that the Content-Length HTTP-message-header is updated.
     */
    private final byte[] contentBody;
    
    public HTTPMessageBody() {
        this.contentBody = new byte[0];
    }

    /**
     * Construct a new HTTP-message-body containing the specified byte array as
     * content-body. The content-body is final, thus a new HTTP-message-body
     * should be constructed if it needs to be changed.
     * 
     * @param contentBody
     *            The content-body of the new HTTP-message-body.
     */
    public HTTPMessageBody(byte[] contentBody) {
        this.contentBody = contentBody;
    }

    /**
     * Construct a new HTTP-message body containing the specified object as
     * content-body. The specified object will be stored as the byte array value
     * of the result of it's toString() method. The content-body is final, this
     * a new HTTP-message-body should be constructed if it needs to be changed.
     * 
     * @param contentBody
     *            The content-body of the new HTTP-message-body.
     */
    public HTTPMessageBody(Object contentBody) {
        Console.log(contentBody);
        this.contentBody = contentBody.toString().getBytes();
    }

    /**
     * Return the content-body of the current HTTP-message-body.
     * 
     * @return The content-body of the current HTTP-message-body.
     */
    public final byte[] getContentBody() {
        return this.contentBody;
    }

    /**
     * Return the amount of octets in the content-body of the current
     * HTTP-message-body. Equal to HTTPMessageBody.getContentBody().length.
     * 
     * @return The amount of octets in the content-body of the current
     *         HTTP-message-body.
     */
    public final int getContentLength() {
        return this.contentBody.length;
    }

    /**
     * Return the current HTTP-message-body as a byte array. Equal to
     * HTTPMessageBody.getContentBody(). Implemented to be consistent with the
     * other HTTP-message data classes.
     * 
     * @return The current HTTP-message-body as a byte array.
     */
    public final byte[] toBytes() {
        return this.contentBody;
    }

    /**
     * Return the current HTTP-message-body as a string. Equal to new
     * String(HTTPMessageBody.getContentBody());
     * 
     * @return The current HTTP-message-body as a string.
     */
    @Override
    public final String toString() {
        return new String(this.contentBody);
    }

}
