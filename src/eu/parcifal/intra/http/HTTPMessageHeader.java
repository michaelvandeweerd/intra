package eu.parcifal.intra.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTTPMessageHeader {
    /**
     * The field-name of the current HTTP-message-header.
     */
    private final byte[] fieldName;

    /**
     * The field-value of the current HTTP-message-header.
     */
    private byte[] fieldValue;

    /**
     * Construct a new HTTP-message-header containing the specified field-name
     * and field-value.
     * 
     * @param fieldName
     *            The field-name of the new HTTP-message-header.
     * @param fieldValue
     *            The field-value of the new HTTP-message-header.
     */
    public HTTPMessageHeader(byte[] fieldName, byte[] fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    /**
     * Construct a new HTTP-message-header containing the specified field-name
     * and field-value. Both fields will be stored as the byte array value of
     * the result of it's toString() method.
     * 
     * @param fieldName
     *            The field-name of the new HTTP-message-header.
     * @param fieldValue
     *            The field-value of the new HTTP-message-header.
     */
    public HTTPMessageHeader(Object fieldName, Object fieldValue) {
        this(fieldName.toString().getBytes(), fieldValue.toString().getBytes());
    }

    /**
     * Construct a new HTTP-message-header containing the specified field-name.
     * The field-value will be empty.
     * 
     * @param fieldName
     *            The field-name of the new HTTP-message-header.
     */
    public HTTPMessageHeader(byte[] fieldName) {
        this(fieldName, new byte[0]);
    }

    /**
     * Construct a new HTTP-message-header containing the specified field-name.
     * The field-name will be stored as the byte array value of the result of
     * it's toString() method. The field-value will be empty.
     * 
     * @param fieldName
     *            The field-name of the new HTTP-message-header.
     */
    public HTTPMessageHeader(Object fieldName) {
        this(fieldName.toString().getBytes());
    }

    /**
     * Return the field-name of the current HTTP-message-header.
     * 
     * @return The field-name of the current HTTP-message-header.
     */
    public byte[] getFieldName() {
        return this.fieldName;
    }

    /**
     * Return the field-value of the current HTTP-message-header.
     * 
     * @return The field-value of the current HTTP-message-header.
     */
    public byte[] getFieldValue() {
        return this.fieldValue;
    }

    /**
     * Set the field-value of the current HTTP-message-header.
     * 
     * @param fieldValue
     *            The new field-value of the current HTTP-message-header.
     */
    public void setFieldValue(byte[] fieldValue) {
        this.fieldValue = fieldValue;
    }

    /**
     * Set the field-value of the current HTTP-message-header. The field-value
     * will be stored as the byte array value of it's toString() method.
     * 
     * @param fieldValue
     *            The new field-value of the current HTTP-message-header.
     */
    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue.toString().getBytes();
    }

    /**
     * Return the current HTTP-message-header as a byte array in the
     * configuration as defined by RFC 2616:
     * 
     * field-name ":" [field-value]
     * 
     * @see https://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html
     * @return The current HTTP-message-header as a byte array.
     */
    public byte[] toBytes() {
        byte colon = ':';

        byte[] fieldName = this.getFieldName();
        byte[] fieldValue = this.getFieldValue();

        byte[] messageHeader = new byte[fieldName.length + 1 + fieldValue.length];

        messageHeader[fieldName.length] = colon;

        System.arraycopy(fieldName, 0, messageHeader, 0, fieldName.length);
        System.arraycopy(fieldValue, 0, messageHeader, fieldName.length + 1, fieldValue.length);

        return messageHeader;
    }

    /**
     * Return the current HTTP-message-header as a string. Equal to new
     * String(HTTPMessageHeader.toBytes()).
     * 
     * @return The current HTTP-message-header as a string.
     */
    @Override
    public String toString() {
        return new String(this.toBytes());
    }

    /**
     * Return true if the specified object is an instance of the
     * HTTPMessageHeader class and has a field-name equal to the field-name of
     * the current HTTP-message-header, otherwise return false.
     * 
     * @return True if the specified object equals the current
     *         HTTP-message-header, otherwise false.
     */
    @Override
    public boolean equals(Object other) {
        return other instanceof HTTPMessageHeader
                && ((HTTPMessageHeader) other).getFieldName().equals(this.getFieldName());
    }

    /**
     * Return the HTTP-message-header represented in the specified string.
     * 
     * @param raw
     *            The string representing the HTTP-message-header to return.
     * @return The HTTP-message-header represented in the specified string.
     */
    public static HTTPMessageHeader fromString(String raw) {
        Pattern pattern = Pattern.compile("^([^:]+):[\t ]*(.*)$");
        Matcher matcher = pattern.matcher(raw);

        if (matcher.find()) {
            return new HTTPMessageHeader(matcher.group(1), matcher.group(2));
        } else {
            throw new IllegalArgumentException(
                    String.format("no HTTP-message-header represented in the specified string\"%1$s\"", raw));
        }
    }

}
