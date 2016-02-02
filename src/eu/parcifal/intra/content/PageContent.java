package eu.parcifal.intra.content;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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

public class PageContent extends Context {

	private final static String PAGE_ROOT = "./pagina/";

	private String path;

	public PageContent(String path) {
		this.path = path;
	}

	@Override
	protected Collection<HTTPMessageHeader> getMessageHeaders() {
		Collection<HTTPMessageHeader> messageHeaders = new ArrayList<HTTPMessageHeader>();

		messageHeaders.add(new HTTPMessageHeader("Content-Type", "text/html"));

		return messageHeaders;
	}

	@Override
	protected HTTPMessageBody getMessageBody() {
		return new HTTPMessageBody(this.include(this.path));
	}

	private String include(String path) {
		File pagina = new File(PAGE_ROOT + path);

		if (pagina.exists()) {
			try {
				String content = new String(Files.readAllBytes(pagina.toPath()), "UTF-8");

				Replacer replacer = new MethodReplacer(new Method("include", 1) {
					@Override
					public String execute(String... args) {
						return PageContent.this.include(args[0]);
					}
				}, new Method("string", 2) {
					@Override
					public String execute(String... args) {
						return PageContent.this.string(args[0], args[1]);
					}
				}, new Method("date", 1) {
					@Override
					public String execute(String... args) {
						return new SimpleDateFormat(args[0]).format(new Date());
					}
				}, new Method("lang") {
					@Override
					public String execute(String... args) {
						return PageContent.this.lang();
					}
				}, new Method("url") {
					@Override
					public String execute(String... args) {
						return PageContent.this.uri();
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
