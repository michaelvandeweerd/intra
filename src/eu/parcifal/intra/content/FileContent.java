package eu.parcifal.intra.content;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.parcifal.intra.http.HTTPMessageBody;
import eu.parcifal.intra.http.HTTPMessageHeader;

public class FileContent extends Context {

	private final static String FILE_ROOT = "./corpus/";

	@Override
	protected Collection<HTTPMessageHeader> getMessageHeaders() {
		Collection<HTTPMessageHeader> messageHeaders = new ArrayList<HTTPMessageHeader>();

		Pattern pattern = Pattern.compile("([^.]+)$");
		Matcher matcher = pattern.matcher(this.request.requestLine().requestURI().path());

		if (matcher.find()) {
			String subtype = matcher.group(1);

			Pattern pattern2 = Pattern.compile("([^\\/, ]+\\/([^;, ]+))(?: *; *q=([^;, ]+))?");
			Matcher matcher2 = pattern2.matcher(this.request.messageHeader("Accept").fieldValue());

			String accept = "*/*";
			double acceptQuality = 1;

			while (matcher2.find()) {
				double quality;

				try {
					quality = Double.valueOf(matcher2.group(3));
				} catch (NullPointerException | IndexOutOfBoundsException exception) {
					quality = 1;
				}
				
				if(matcher2.group(2).equals(subtype)) {
					accept = matcher2.group();
					break;
				} else if (quality <= acceptQuality) {
					acceptQuality = quality;
					accept = matcher2.group(1);
				}
			}

			messageHeaders.add(new HTTPMessageHeader("Content-Type", accept));

			return messageHeaders;
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	protected HTTPMessageBody getMessageBody() {
		File file = new File(FILE_ROOT + this.request.requestLine().requestURI().path());

		if (file.exists()) {
			try {
				return new HTTPMessageBody(new String(Files.readAllBytes(file.toPath()), "UTF-8"));
			} catch (IOException e) {
				throw new RuntimeException();
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

}
