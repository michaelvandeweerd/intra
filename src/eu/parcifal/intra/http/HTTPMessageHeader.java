package eu.parcifal.intra.http;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTTPMessageHeader {

	private final static String STRING_FORMAT = "%1$s: %2$s\r\n";

	public final static String FIELD_NAME_ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	public final static String FIELD_NAME_SERVER = "Server";
	public final static String FIELD_NAME_DATE = "Date";

	private String fieldName;

	private String fieldValue;

	public HTTPMessageHeader(String fieldName, String fieldValue) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public String fieldValue() {
		return this.fieldValue;
	}

	public static Collection<HTTPMessageHeader> fromString(String raw) {
		Collection<HTTPMessageHeader> messageHeaders = new ArrayList<HTTPMessageHeader>();

		Pattern pattern = Pattern.compile("^([^:]+):[\t ]*(.*)$", Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(raw);

		while (matcher.find()) {
			messageHeaders.add(new HTTPMessageHeader(matcher.group(1), matcher.group(2)));
		}

		return messageHeaders;
	}

	public byte[] toBytes() {
		return this.toString().getBytes();
	}

	@Override
	public String toString() {
		return String.format(STRING_FORMAT, this.fieldName, this.fieldValue);
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof HTTPMessageHeader && ((HTTPMessageHeader) other).fieldValue().equals(this.fieldValue);
	}

}
