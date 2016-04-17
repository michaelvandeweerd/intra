package eu.parcifal.intra.http;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HTTP messages consist of requests from client to server and responses from
 * server to client.
 * 
 * @author Michaël van de Weerd
 * @see https://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#4.1
 */
public abstract class HTTPMessage {

    /**
     * The start-line of the current HTTP message.
     */
    private HTTPStartLine startLine;

    /**
     * The headers of the current HTTP message.
     */
    private Collection<HTTPMessageHeader> messageHeaders = new ArrayList<HTTPMessageHeader>();

    /**
     * The body of the current HTTP message.
     */
    private HTTPMessageBody messageBody;

    protected HTTPMessage(HTTPStartLine startLine) {
        this.startLine = startLine;
        this.messageBody = new HTTPMessageBody();
        this.addMessageHeader("Content-Length", this.messageBody.getContentLength());
    }

    protected final HTTPStartLine getStartLine() {
        return this.startLine;
    }

    protected final void setStartLine(HTTPStartLine startLine) {
        this.startLine = startLine;
    }

    public final Collection<HTTPMessageHeader> getMessageHeaders() {
        // TODO return clone
        return this.messageHeaders;
    }

    public final void setMessageHeaders(Collection<HTTPMessageHeader> messageHeaders) {
        this.messageHeaders = messageHeaders;
    }

    public final HTTPMessageHeader getMessageHeader(byte[] fieldName) {
        for (HTTPMessageHeader messageHeader : this.messageHeaders) {
            if (Arrays.equals(messageHeader.getFieldName(), fieldName)) {
                return messageHeader;
            }
        }

        throw new IllegalArgumentException(String.format(
                "HTTP-message-header with specified field-name \"%1$s\" does not exist", new String(fieldName)));
    }

    public final HTTPMessageHeader getMessageHeader(Object fieldName) {
        return this.getMessageHeader(fieldName.toString().getBytes());
    }

    public final void addMessageHeader(HTTPMessageHeader messageHeader) {
        this.messageHeaders.add(messageHeader);
    }

    public final void addMessageHeader(byte[] fieldName, byte[] fieldValue) {
        this.addMessageHeader(new HTTPMessageHeader(fieldName, fieldValue));
    }

    public final void addMessageHeader(Object fieldName, Object fieldValue) {
        this.addMessageHeader(new HTTPMessageHeader(fieldName, fieldValue));
    }

    public final void removeMessageHeader(HTTPMessageHeader messageHeader) {
        this.messageHeaders.remove(messageHeader);
    }

    public final void removeMessageHeader(byte[] fieldName, byte[] fieldValue) {
        this.removeMessageHeader(new HTTPMessageHeader(fieldName, fieldValue));
    }

    public final void removeMessageHeader(Object fieldName, Object fieldValue) {
        this.removeMessageHeader(new HTTPMessageHeader(fieldName, fieldValue));
    }

    public final void setMessageHeader(byte[] fieldName, byte[] fieldValue) {
        if (this.hasMessageHeader(fieldName)) {
            this.getMessageHeader(fieldName).setFieldValue(fieldValue);
        } else {
            this.addMessageHeader(fieldName, fieldValue);
        }
    }

    public final void setMessageHeader(Object fieldName, Object fieldValue) {
        this.setMessageHeader(fieldName.toString().getBytes(), fieldValue.toString().getBytes());
    }

    public final boolean hasMessageHeader(byte[] fieldName) {
        try {
            this.getMessageHeader(fieldName);

            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    public final boolean hasMessageHeader(Object fieldName) {
        return this.hasMessageHeader(fieldName.toString().getBytes());
    }

    public final HTTPMessageBody getMessageBody() {
        return this.messageBody;
    }

    public final void setMessageBody(HTTPMessageBody messageBody) {
        this.messageBody = messageBody;
        if (this.hasMessageHeader("Content-Length")) {
            this.setMessageHeader("Content-Length", this.messageBody.getContentLength());
        } else {
            this.addMessageHeader("Content-Length", this.messageBody.getContentLength());
        }
    }

    public final void setMessageBody(Object contentBody) {
        this.setMessageBody(new HTTPMessageBody(contentBody));
    }

    public final void setMessageBody(byte[] contentBody) {
        this.setMessageBody(new HTTPMessageBody(contentBody));
    }

    public final String getPostValue(String name) {
        if (this.getMessageHeader("Content-Type").getFieldValue().equals("x-www-form-urlencoded")) {
            Pattern pattern = Pattern.compile(String.format("%1$s=([^&]*)", name));
            Matcher matcher = pattern.matcher(this.getMessageBody().toString());

            if (matcher.find()) {
                return matcher.group(1);
            } else {
                return null;
            }
        } else {
            throw new IllegalArgumentException(
                    String.format("post-value with specified name \"%1$s\" does not exist", name));
        }
    }

    public final byte[] toBytes() {
        byte[] startLine = this.startLine.toBytes();
        byte[] messageHeaders = new byte[0];
        byte[] newLine = "\r\n".getBytes();
        byte[] messageBody = this.messageBody.toBytes();

        for (HTTPMessageHeader messsageHeader : this.messageHeaders) {
            byte[] messageHeader = messsageHeader.toBytes();
            byte[] concatination = new byte[messageHeaders.length + messageHeader.length + newLine.length];

            System.arraycopy(messageHeaders, 0, concatination, 0, messageHeaders.length);
            System.arraycopy(messageHeader, 0, concatination, messageHeaders.length, messageHeader.length);
            System.arraycopy(newLine, 0, concatination, messageHeaders.length + messageHeader.length, newLine.length);

            messageHeaders = concatination;
        }

        byte[] message = new byte[startLine.length + messageHeaders.length + newLine.length + messageBody.length];

        System.arraycopy(startLine, 0, message, 0, startLine.length);
        System.arraycopy(messageHeaders, 0, message, startLine.length, messageHeaders.length);
        System.arraycopy(newLine, 0, message, startLine.length + messageHeaders.length, newLine.length);
        System.arraycopy(messageBody, 0, message, startLine.length + messageHeaders.length + newLine.length,
                messageBody.length);

        return message;
    }

    @Override
    public String toString() {
        return new String(this.toBytes());
    }

}
