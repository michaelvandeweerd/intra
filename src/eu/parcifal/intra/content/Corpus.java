package eu.parcifal.intra.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.parcifal.intra.http.HTTPMessageBody;
import eu.parcifal.intra.http.HTTPMessageHeader;
import eu.parcifal.plus.print.Console;

public class Corpus extends Context {

	private final static String CORPUS_ROOT = "./corpus/";

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
		Pattern pattern = Pattern.compile("[/+]([^+]*)");
		Matcher matcher = pattern.matcher(this.request.requestLine().requestURI().path());

		Console.log(this.request.requestLine().requestURI().path());
		
		byte[] contentBody = new byte[0];
		byte[] newLine = "\r\n\r\n".getBytes();

		while (matcher.find()) {
			byte[] file = this.load(CORPUS_ROOT + matcher.group(1));

			if (contentBody.length == 0) {
				contentBody = file;
			} else {
				byte[] content = new byte[contentBody.length + newLine.length + file.length];

				System.arraycopy(contentBody, 0, content, 0, contentBody.length);
				System.arraycopy(newLine, 0, content, contentBody.length, newLine.length);
				System.arraycopy(file, 0, content, contentBody.length + newLine.length, file.length);

				contentBody = content;
			}
		}

		return new HTTPMessageBody(contentBody);
	}

}
