package eu.parcifal.intra.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import eu.parcifal.intra.http.HTTPMessageBody;
import eu.parcifal.intra.http.HTTPMessageHeader;
import eu.parcifal.intra.http.HTTPRequest;
import eu.parcifal.intra.http.HTTPResponse;
import eu.parcifal.intra.http.HTTPStatusLine;
import eu.parcifal.plus.logic.Executable;

public abstract class Content implements Executable {

	protected HTTPRequest request;

	@Override
	public HTTPResponse execute(Object... args) {
		if (args.length < 1 || !(args[0] instanceof HTTPRequest)) {
			throw new IllegalArgumentException();
		} else {
			this.request = (HTTPRequest) args[0];

			return new HTTPResponse(this.statusLine(), this.messageHeaders(), this.messageBody());
		}
	}

	protected HTTPStatusLine statusLine() {
		return HTTPStatusLine.STATUS_200_1_1;
	}

	protected Collection<HTTPMessageHeader> messageHeaders() {
		return new ArrayList<HTTPMessageHeader>();
	}

	protected HTTPMessageBody messageBody() {
		return HTTPMessageBody.EMPTY;
	}

	protected byte[] run(String script) {
		StringWriter writer = new StringWriter();

		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

		engine.getContext().setWriter(writer);
		engine.put("Request", this.request.toMap());

		try {
			engine.eval(script);
		} catch (ScriptException exception) {
			throw new IllegalArgumentException();
		}

		// remove newline appended by Nashorn's print(arg) function, should be
		// done better
		Pattern pattern = Pattern.compile("\\r?\\n");
		Matcher matcher = pattern.matcher(writer.toString());

		StringBuffer buffer = new StringBuffer();

		while (matcher.find()) {
			matcher.appendReplacement(buffer, "");
		}

		return matcher.appendTail(buffer).toString().getBytes();
	}

	protected byte[] load(String path) {
		File file = new File(path);

		if (file.exists() && file.isFile()) {
			FileInputStream stream = null;

			try {
				byte[] content = new byte[(int) file.length()];

				stream = new FileInputStream(file);

				stream.read(content);

				return content;
			} catch (IOException exception) {
				throw new RuntimeException();
			} finally {
				try {
					stream.close();
				} catch (IOException exception) {
					throw new RuntimeException();
				}
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

}
