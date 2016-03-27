package eu.parcifal.intra.content;

import java.io.FileReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import eu.parcifal.intra.http.HTTPResponse;
import eu.parcifal.intra.http.HTTPStatusLine;
import eu.parcifal.plus.print.Console;

public class Script extends File {

	public Script(String location) {
		super(location);
	}

	@Override
	protected HTTPResponse response() {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		
		// JavaScript strings cannot cover multiple lines
		String request = this.request.toString().replace("\r\n", "\\n");
		String response = new HTTPResponse(HTTPStatusLine.STATUS_200_1_1).toString().replace("\r\n", "\\n");

		try {
			// evaluate all default scripts
			engine.eval(new FileReader("./res/asset/script/printer.js"));
			engine.eval(new FileReader("./res/asset/script/plus.js"));
			engine.eval(new FileReader("./res/asset/script/http.js"));
			engine.eval(new FileReader("./res/asset/script/uri.js"));
			
			// define global variables
			engine.eval("var console = new Printer([Packages.eu.parcifal.plus.print.Console.log]);");
			engine.eval("var intra = new Plus();");
			engine.eval(String.format("var request = HTTPRequest.fromString(\"%1$s\");", request));
			engine.eval(String.format("var response = HTTPResponse.fromString(\"%1$s\");", response));
			
			engine.eval(new FileReader(this.location));

			return HTTPResponse.fromString((String) engine.eval("response.toString();"));
		} catch (Exception exception) {
			Console.warn(exception);
			throw new RuntimeException();
		}
	}

}
