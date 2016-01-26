package eu.parcifal.web.content.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.parcifal.extra.net.http.HTTPRequest;
import eu.parcifal.extra.net.http.HTTPResponse;
import eu.parcifal.web.content.Content;

public class Index extends Content {

	private static final String ROOT = "./bestand";

	@Override
	protected HTTPResponse response(HTTPRequest request) {
		File file = new File(ROOT + request.uri());

		if (file.exists()) {
			try {
				String content = new String(Files.readAllBytes(file.toPath()), "UTF-8");

				HTTPResponse response = new HTTPResponse(HTTPResponse.STATUS_200);

				Pattern pattern = Pattern.compile("^([^,]*)");
				Matcher matcher = pattern.matcher(request.header("Accept").value());

				if (matcher.find()) {
					response.header("Content-Type", matcher.group(1));
				}

				response.body(content);

				return response;
			} catch (IOException e) {
				return new HTTPResponse(HTTPResponse.STATUS_500);
			}
		} else {
			return new HTTPResponse(HTTPResponse.STATUS_404);
		}
	}
}
