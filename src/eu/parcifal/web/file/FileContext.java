package eu.parcifal.web.file;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.parcifal.web.Context;

public class FileContext extends Context {

	private final static String FILE_PATH = "bestand%1s";

	private final static String DEFAULT_CONTENT_TYPE = "unknown/unknown";

	@Override
	protected String response() {
		return this.load(String.format(FILE_PATH, this.path()));
	}

	@Override
	protected String contentType() {
		Pattern pattern = Pattern.compile(".*\\.(.*)$");
		Matcher matcher = pattern.matcher(this.path());

		if (matcher.find()) {
			return "text/" + matcher.group(1);
		} else {
			return DEFAULT_CONTENT_TYPE;
		}
	}

}
