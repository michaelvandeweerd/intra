package eu.parcifal.intra.content;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import eu.parcifal.intra.http.HTTPMessageBody;
import eu.parcifal.intra.http.HTTPRequest;
import eu.parcifal.plus.print.Console;

public class Page extends File {
    
    private ScriptEngine engine;

    public Page(String location) {
        super(location);
    }

    @Override
    protected HTTPMessageBody getMessageBody(HTTPRequest request) {
        this.engine = new ScriptEngineManager().getEngineByName("nashorn");

        this.engine.getBindings(ScriptContext.GLOBAL_SCOPE).put("request", request);
        this.engine.getBindings(ScriptContext.GLOBAL_SCOPE).put("page", this);
        
        try {
            this.engine.eval(new FileReader("./res/asset/script/intra.js"));
        } catch(ScriptException exception) {
            Console.warn(exception);
        } catch (FileNotFoundException exception) {
            Console.warn(exception);
        }

        String plain = this.read(this.location);

        String contentBody = this.evaluate(plain);


        return new HTTPMessageBody(contentBody);
    }
    
    public String evaluate(String plain) {
        Pattern pattern = Pattern.compile("<\\?e?js((?:(?!\\?>).)*)\\?>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(plain);

        StringBuffer buffer = new StringBuffer();
        
        while (matcher.find()) {            
            try {
                this.engine.eval(matcher.group(1));
                
                String result = (String) this.engine.eval("flushPrinter()");
                
                matcher.appendReplacement(buffer, this.evaluate(result));
            } catch (ScriptException exception) {
                Console.warn(exception);
                
                matcher.appendReplacement(buffer, String.format("<!-- %1$s -->", matcher.group(1)));
            }
        }
        
        return matcher.appendTail(buffer).toString();
    }

    public String read(String location) {
        return new String(super.load(location));
    }

}
