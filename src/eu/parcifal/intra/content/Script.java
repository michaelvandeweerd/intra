package eu.parcifal.intra.content;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import eu.parcifal.intra.http.HTTPRequest;
import eu.parcifal.intra.http.HTTPResponse;
import eu.parcifal.intra.http.HTTPStatusLine;

public class Script extends File {

    public Script(String location) {
        super(location);
    }

    @Override
    protected HTTPResponse getResponse(HTTPRequest request) {
        HTTPResponse response = new HTTPResponse(HTTPStatusLine.STATUS_200_1_1);

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

        engine.getBindings(ScriptContext.GLOBAL_SCOPE).put("request", request);
        engine.getBindings(ScriptContext.GLOBAL_SCOPE).put("response", response);
        
        try {
            engine.eval(new FileReader("./res/asset/script/intra.js"));
            engine.eval(new FileReader(this.location));
        } catch (FileNotFoundException exception) {
            throw new IllegalArgumentException(
                    String.format("file at location \"%1$s\" does not exist", this.location));
        } catch (ScriptException exception) {
            throw new RuntimeException(String.format("script at location \"%1$s\" is not valid, cause: %2$s", this.location, exception.toString()));
        }

        return response;
    }

}
