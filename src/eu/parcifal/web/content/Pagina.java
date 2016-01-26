package eu.parcifal.web.content;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import eu.parcifal.extra.parsing.TranscriptionFile;
import eu.parcifal.extra.parsing.old.MarkdownParser;
import eu.parcifal.extra.parsing.old.Parser;
import eu.parcifal.meta.Method;
import eu.parcifal.meta.MethodParser;

public abstract class Pagina extends Content {

	private static final String ROOT = "./pagina";

	protected String load(String path) {
		File file = new File(ROOT + path);

		if (file.exists()) {
			try {
				String content = new String(Files.readAllBytes(file.toPath()), "UTF-8");

				Parser parser = new MethodParser(new Method("load") {
					@Override
					public Object execute(Object... args) {
						if (args.length < 1 || !(args[0] instanceof String)) {
							throw new IllegalArgumentException();
						} else {
							String file = (String) args[0];

							return Pagina.this.load(file);
						}
					}
				}, new Method("string") {
					@Override
					public Object execute(Object... args) {
						if (args.length < 2 || !(args[0] instanceof String && args[1] instanceof String)) {
							throw new IllegalArgumentException();
						} else {
							String source = (String) args[0];
							String identifier = (String) args[1];

							return Pagina.this.string(source, identifier);
						}
					}
				}, new Method("date") {
					@Override
					public Object execute(Object... args) {
						if (args.length < 1 || !(args[0] instanceof String)) {
							throw new IllegalArgumentException();
						} else {
							return new SimpleDateFormat((String) args[0]).format(new Date());
						}
					}
				});

				return parser.parse(content);
			} catch (IOException ioe) {
				throw new RuntimeException();
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	private String string(String source, String key) {
		try {
			TranscriptionFile file = new TranscriptionFile(ROOT + source);
			Parser parser = new MarkdownParser();

			return parser.parse(file.get(key, this.lang()).text());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return key;
	}

	private String lang() {
		return "en";
	}

}
