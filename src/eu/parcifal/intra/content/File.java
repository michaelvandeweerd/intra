package eu.parcifal.intra.content;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.parcifal.intra.http.HTTPMessageBody;
import eu.parcifal.intra.http.HTTPMessageHeader;

public class File extends Context {

	private final static String FILE_ROOT = "./corpus/";

	@Override
	protected Collection<HTTPMessageHeader> messageHeaders() {
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

				if (matcher2.group(2).equals(subtype)) {
					accept = matcher2.group();
					break;
				} else if (quality <= acceptQuality) {
					acceptQuality = quality;
					accept = matcher2.group(1);
				}
			}

			messageHeaders.add(new HTTPMessageHeader("Content-Type", accept));
			messageHeaders.add(new HTTPMessageHeader("Access-Control-Allow-Origin", "*"));

			return messageHeaders;
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	protected HTTPMessageBody messageBody() {
		java.io.File file = new java.io.File(FILE_ROOT + this.request.requestLine().requestURI().path());

		if (file.exists()) {
			FileInputStream stream = null;

			try {
				byte[] content = new byte[(int) file.length()];

				stream = new FileInputStream(file);

				stream.read(content);

				return new HTTPMessageBody(content);
			} catch (IOException e) {
				throw new RuntimeException();
			} finally {
				try {
					stream.close();
				} catch (IOException e) {
					throw new RuntimeException();
				}
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

}
