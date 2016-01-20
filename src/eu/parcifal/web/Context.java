package eu.parcifal.web;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import eu.parcifal.extra.logic.Executable;
import eu.parcifal.extra.parsing.Parser;
import eu.parcifal.extra.print.Console;
import eu.parcifal.extra.print.output.Warning;

public abstract class Context implements Executable {

	private static final String WARNING_STRING_NOT_FOUND = "string \"%1s\" not found in \"%2s\"";

	private static final String WARNING_LANGUAGE_NOT_FOUND = "language \"%1s\" not found of string \"%2s\" in \"%3s\"";

	private static final String MESSAGE_ILLEGAL_ARGUMENT = "specified data is not an instance of an HTTP exchange";

	private static final Object WARNING_LOAD_PAGE = "specified page \"%1s\" could not be loaded";

	private static final int DEFAULT_RESPONSE_CODE = HttpURLConnection.HTTP_OK;

	private static final String DEFAULT_CONTENT_TYPE = "text/html";

	private static final String DEFAULT_LANGUAGE = "en";

	private HttpExchange exchange;

	@Override
	public void execute(Object... args) {
		if (!(args[0] instanceof HttpExchange)) {
			throw new IllegalArgumentException(MESSAGE_ILLEGAL_ARGUMENT);
		} else {
			this.exchange = (HttpExchange) args[0];

			String response = this.response();

			OutputStream out = null;

			try {
				Headers headers = this.exchange.getResponseHeaders();

				headers.add("Content-type", this.contentType());

				this.exchange.sendResponseHeaders(responseCode(), response.length());

				out = this.exchange.getResponseBody();

				out.write(response.getBytes());
			} catch (IOException ioe) {
				Console.warn(Warning.Level.HIGH, ioe.getMessage());
			} finally {
				this.exchange = null;

				try {
					out.close();
				} catch (IOException ioe) {
					Console.warn(Warning.Level.HIGH, ioe.getMessage());
				}
			}
		}
	}

	/**
	 * Return the response code of the current context.
	 * 
	 * @return The response code of the current context.
	 */
	protected int responseCode() {
		return DEFAULT_RESPONSE_CODE;
	}

	/**
	 * Return the content type of the current context.
	 * 
	 * @return The content type of the current context.
	 */
	protected String contentType() {
		return DEFAULT_CONTENT_TYPE;
	}

	protected String url() {
		return "http://" + this.exchange.getRequestHeaders().getFirst("Host")
				+ this.exchange.getRequestURI().toString();
	}

	/**
	 * Return the URI path of the current context.
	 * 
	 * @return The URI path of the current context.
	 */
	protected String path() {
		Pattern pattern = Pattern.compile("^(.*)(?:\\?.*)?$");
		Matcher matcher = pattern.matcher(this.exchange.getRequestURI().toString());

		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return "/";
		}
	}

	/**
	 * Return the language of the current context.
	 * 
	 * @return The language of the current context.
	 */
	protected String lang() {
		String lang = this.cookie("language");

		if (lang != "") {
			return lang;
		}

		lang = this.exchange.getRequestHeaders().getFirst("Accept-Language");

		Pattern pattern = Pattern.compile("^([^;,]{2}).*");
		Matcher matcher = pattern.matcher(lang);

		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return DEFAULT_LANGUAGE;
		}
	}

	protected String charset() {
		return Charset.defaultCharset().name();
	}

	/**
	 * Return the value of the first occurrence of the GET parameter with the
	 * specified name.
	 * 
	 * @param name
	 *            The name of the GET parameter.
	 * @return The value of the specified GET parameter.
	 */
	protected String get(String name) {
		String uri = this.path();
		Pattern pattern = Pattern.compile(String.format("[\\?&]%1s=(.*)", name), Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(uri);

		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return "";
		}
	}

	protected String cookie(String name) {
		String cookies = this.exchange.getRequestHeaders().getFirst("Cookie");

		if (cookies != null) {
			Pattern pattern = Pattern.compile(String.format("%1s=([^;=])", name), Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(cookies);

			if (matcher.find()) {
				return matcher.group(1);
			}
		}

		return "";
	}

	protected String load(String name, Charset encoding) {
		byte[] encoded = null;

		try {
			encoded = Files.readAllBytes(Paths.get(name));
		} catch (IOException ioe) {
			Console.warn(Warning.Level.HIGH, WARNING_LOAD_PAGE, name);
		}

		return new String(encoded, encoding);
	}

	protected String load(String name) {
		return load(name, Charset.defaultCharset());
	}

	protected String string(String source, String name, String lang) {
		String strings = this.load(source);

		try {
			Pattern pattern = Pattern.compile(String.format("^%1s \\{$\\s*^([^\\}]*)$\\s*^\\}$", name),
					Pattern.MULTILINE);
			Matcher matcher = pattern.matcher(strings);

			if (matcher.find()) {
				Pattern pattern2 = Pattern.compile(String.format(String.format("\\t%1s: \"(.*)\"", lang),
						Pattern.MULTILINE | Pattern.CASE_INSENSITIVE));
				Matcher matcher2 = pattern2.matcher(matcher.group(1));

				if (matcher2.find()) {
					return matcher2.group(1);
				} else {
					Console.warn(Warning.Level.LOW, WARNING_LANGUAGE_NOT_FOUND, lang, name, source);
				}
			} else {
				Console.warn(Warning.Level.LOW, WARNING_STRING_NOT_FOUND, name, source);
			}
		} catch (Exception e) {
			Console.log(e);
		}

		return name;
	}

	protected String string(String source, String name) {
		return this.string(source, name, this.lang());
	}

	protected String parse(String content) {
		Parser loadParser = new Parser("(?<!\\\\)%load\\((.*)\\)(?<!\\\\)%") {
			@Override
			public String replacement(String... groups) {
				return Context.this.load(groups[1]);
			}
		};

		Parser stringParser = new Parser("(?<!\\\\)%string\\((.*), (.*)\\)(?<!\\\\)%") {
			@Override
			public String replacement(String... groups) {
				return Context.this.string(groups[1], groups[2], Context.this.lang());
			}
		};

		Parser getParser = new Parser("(?<!\\\\)%get\\((.*)\\)(?<!\\\\)%") {
			@Override
			public String replacement(String... groups) {
				return Context.this.get(groups[1]);
			}
		};

		Parser langParser = new Parser("(?<!\\\\)%lang(?<!\\\\)%") {
			@Override
			public String replacement(String... groups) {
				return Context.this.lang();
			}
		};

		Parser charsetParser = new Parser("(?<!\\\\)%charset(?<!\\\\)%") {
			@Override
			public String replacement(String... groups) {
				return Context.this.charset();
			}
		};

		Parser anchorParser = new Parser("(?<!\\\\)\\[([^\\]]*)(?<!\\\\)\\](?<!\\\\)\\(([^\\)]*)(?<!\\\\)\\)") {
			@Override
			public String replacement(String... groups) {
				return String.format("<a href=\"%1s\">%2s</a>", groups[2], groups[1]);
			}
		};

		Parser boldParser = new Parser("(?<!\\\\)\\*\\*((?:(?<=\\\\)\\*\\*|[^\\*\\*])*)(?<!\\\\)\\*\\*") {
			@Override
			public String replacement(String... groups) {
				return String.format("<strong>%1s</strong>", groups[1]);
			}
		};

		Parser italicParser = new Parser("(?<!\\\\)\\*((?:(?<=\\\\)\\*|[^\\*])*)(?<!\\\\)\\*") {
			@Override
			public String replacement(String... groups) {
				return String.format("<em>%1s</em>", groups[1]);
			}
		};

		Parser urlParser = new Parser("(?<!\\\\)%url(?<!\\\\)%") {
			@Override
			public String replacement(String... groups) {
				return Context.this.url();
			}
		};

		Parser dateParser = new Parser("(?<!\\\\)%date\\((.*)\\)(?<!\\\\)%") {
			@Override
			public String replacement(String... groups) {
				DateFormat format = new SimpleDateFormat(groups[1]);
				Date date = new Date();

				return format.format(date);
			}
		};

		content = loadParser.replace(content);
		content = stringParser.replace(content);
		content = getParser.replace(content);
		content = langParser.replace(content);
		content = charsetParser.replace(content);
		content = urlParser.replace(content);
		content = dateParser.replace(content);
		content = anchorParser.replace(content);
		content = boldParser.replace(content);
		content = italicParser.replace(content);

		return content;
	}

	abstract protected String response();

}
