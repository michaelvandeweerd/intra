package eu.parcifal.intra.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTTPVersion {

	private final static String STRING_FORMAT = "HTTP/%1$d.%2$d";

	public final static HTTPVersion VERSION_1_0 = new HTTPVersion(1, 0);
	public final static HTTPVersion VERSION_1_1 = new HTTPVersion(1, 1);

	private int major;
	private int minor;

	public HTTPVersion(int major, int minor) {
		this.major = major;
		this.minor = minor;
	}

	public int getMajor() {
		return this.major;
	}

	public int getMinor() {
		return this.minor;
	}

	public static HTTPVersion fromString(String raw) {
		Pattern pattern = Pattern.compile("^HTTP\\/([0-9])\\.([0-9])$");
		Matcher matcher = pattern.matcher(raw);

		if (matcher.find()) {
			int major = Integer.parseInt(matcher.group(1));
			int minor = Integer.parseInt(matcher.group(2));

			return new HTTPVersion(major, minor);
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public String toString() {
		return String.format(STRING_FORMAT, this.major, this.minor);
	}

}
