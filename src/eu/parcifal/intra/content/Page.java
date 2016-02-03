package eu.parcifal.intra.content;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import eu.parcifal.intra.http.HTTPMessageBody;
import eu.parcifal.intra.http.HTTPMessageHeader;
import eu.parcifal.meta.Method;
import eu.parcifal.meta.MethodReplacer;
import eu.parcifal.plus.parsing.MarkdownParser;
import eu.parcifal.plus.parsing.Replacer;
import eu.parcifal.plus.parsing.TranscriptionFile;

public class Page extends Context {

	private static final String DEFAULT_CONTENT_TYPE = "text/html";

	private final static String PAGE_ROOT = "./pagina/";

	private String path;

	public Page(String path) {
		this.path = path;
	}

	@Override
	protected Collection<HTTPMessageHeader> messageHeaders() {
		Collection<HTTPMessageHeader> messageHeaders = new ArrayList<HTTPMessageHeader>();

		messageHeaders.add(new HTTPMessageHeader("Content-Type", DEFAULT_CONTENT_TYPE));

		return messageHeaders;
	}

	@Override
	protected HTTPMessageBody messageBody() {
		return new HTTPMessageBody(this.include(this.path));
	}

	private String include(String path) {
		File pagina = new File(PAGE_ROOT + path);

		if (pagina.exists()) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(pagina));

				StringBuilder builder = new StringBuilder();
				String line = null;

				try {
					while ((line = reader.readLine()) != null) {
						builder.append(line + "\r\n");
					}
				} finally {
					reader.close();
				}

				String content = builder.toString();

				Replacer replacer = new MethodReplacer(
					new Method("include", 1) {
						@Override
						public String execute(String... args) {
							return Page.this.include(args[0]);
						}
					}, new Method("string", 2) {
						@Override
						public String execute(String... args) {
							return Page.this.string(args[0], args[1]);
						}
					}, new Method("date", 1) {
						@Override
						public String execute(String... args) {
							return new SimpleDateFormat(args[0]).format(new Date());
						}
					}, new Method("lang") {
						@Override
						public String execute(String... args) {
							return Page.this.lang();
						}
					}, new Method("url") {
						@Override
						public String execute(String... args) {
							return Page.this.uri();
						}
					});

				return replacer.replace(content);
			} catch (IOException e) {
				throw new RuntimeException();
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	private String uri() {
		return "http://" + this.request.messageHeader("Host") + this.request.requestLine().requestURI().path();
	}

	private String lang() {
		return "en";
	}

	private String string(String source, String key) {
		try {
			TranscriptionFile file = new TranscriptionFile(PAGE_ROOT + source);
			Replacer replacer = new MarkdownParser();

			return replacer.replace(file.get(key, this.lang()).text());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return key;
	}

}
