package eu.parcifal.intra.content;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import eu.parcifal.intra.http.HTTPMessageBody;
import eu.parcifal.intra.http.HTTPMessageHeader;
import eu.parcifal.meta.Method;
import eu.parcifal.meta.MethodReplacer;
import eu.parcifal.plus.net.URI;
import eu.parcifal.plus.parsing.MarkdownReplacer;
import eu.parcifal.plus.parsing.Replacer;
import eu.parcifal.plus.parsing.TranscriptionFile;

public class Pagina extends Content {

	private static final String DEFAULT_CONTENT_TYPE = "text/html";

	private String path;

	public Pagina(String path) {
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
		return this.method(new String(this.load(path)));
	}

	private String uri() {
		return "http://" + this.request.messageHeader("Host") + this.request.requestLine().requestURI().path();
	}

	private String lang() {
		return "en";
	}

	private String navigation(URI location, String source, String key) {
		URI reference = localise(location);

		if (this.request.messageHeader("Host").fieldValue().equals(reference.host())
				&& this.request.requestLine().requestURI().path().equals(reference.path())) {
			return String.format("<a>%1$s</a>", this.string(source, key));
		} else {
			return String.format("<a href=\"%1$s\">$2$s</a>", reference, this.string(source, key));
		}
	}

	private URI localise(URI location) {
		location.host(location.host() + ".localhost");

		return location;
	}

	private String string(String source, String key) {
		try {
			TranscriptionFile file = new TranscriptionFile(source);

			return this.markdown(this.method(file.get(key, this.lang()).text()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return key;
	}

	private String markdown(String plain) {
		Replacer replacer = new MarkdownReplacer();

		return replacer.replace(plain);
	}

	private String method(String plain) {
		Replacer replacer = new MethodReplacer(new Method("include", 1) {
			@Override
			public String execute(String... args) {
				return Pagina.this.include(args[0]);
			}
		}, new Method("markdown", 1) {
			@Override
			public String execute(String... args) {
				return Pagina.this.markdown(new String(Pagina.this.load(args[0])));
			}
		}, new Method("method", 1) {
			@Override
			public String execute(String... args) {
				return Pagina.this.method(new String(Pagina.this.load(args[0])));
			}
		}, new Method("string", 2) {
			@Override
			public String execute(String... args) {
				return Pagina.this.string(args[0], args[1]);
			}
		}, new Method("date", 1) {
			@Override
			public String execute(String... args) {
				return new SimpleDateFormat(args[0]).format(new Date());
			}
		}, new Method("lang") {
			@Override
			public String execute(String... args) {
				return Pagina.this.lang();
			}
		}, new Method("url") {
			@Override
			public String execute(String... args) {
				return Pagina.this.uri();
			}
		}, new Method("localise", 1) {
			@Override
			public String execute(String... args) {
				return Pagina.this.localise(URI.fromString(args[0])).toString();
			}
		}, new Method("navigation", 3) {
			@Override
			public String execute(String... args) {
				return Pagina.this.navigation(URI.fromString(args[0]), args[1], args[2]);
			}
		});

		return replacer.replace(plain);
	}

}
