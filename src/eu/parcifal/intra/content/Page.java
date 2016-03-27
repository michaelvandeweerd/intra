package eu.parcifal.intra.content;

import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import eu.parcifal.intra.http.HTTPMessageBody;
import eu.parcifal.plus.print.Console;

public class Page extends File {
	
	public Page(String location) {
		super(location);
	}

	@Override
	protected HTTPMessageBody messageBody() {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

		// JavaScript strings cannot cover multiple lines
		String request = this.request.toString().replace("\r\n", "\\n");

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
			
			String plain = Page.read(this.location);
			
			StringBuffer buffer = new StringBuffer();
			
			Pattern pattern = Pattern.compile("<\\?js((?:(?!\\?>).)*)\\?>", Pattern.DOTALL);
			Matcher matcher = pattern.matcher(plain);
			
			Console.log(pattern.pattern().toString());
			
			while(matcher.find()) {
				engine.eval(matcher.group(1));
				matcher.appendReplacement(buffer, (String) engine.eval("intra.resetOutput()"));
			}
			
			String contentBody = matcher.appendTail(buffer).toString();
			
			return new HTTPMessageBody(contentBody);
		} catch (Exception exception) {
			Console.warn(exception);
			
			throw new RuntimeException();
		}		
	}

	public static String read(String location) {
		return new String(File.load(location));
	}

}
