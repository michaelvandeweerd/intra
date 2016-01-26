package eu.parcifal.web.content.kort;

import eu.parcifal.extra.net.http.HTTPRequest;
import eu.parcifal.extra.net.http.HTTPResponse;
import eu.parcifal.extra.print.Console;
import eu.parcifal.extra.print.output.Warning.Level;
import eu.parcifal.web.content.Pagina;

public class Index extends Pagina {

	@Override
	protected HTTPResponse response(HTTPRequest request) {
		HTTPResponse response;

		try {
			String body = this.load("./kort/index.pagina");

			response = new HTTPResponse(HTTPResponse.STATUS_200);

			response.body(body);

			return response;
		} catch (IllegalArgumentException iae) {
			Console.warn(Level.LOW, iae);

			response = new HTTPResponse(HTTPResponse.STATUS_404);
		} catch (RuntimeException re) {
			Console.warn(Level.HIGH, re);

			re.printStackTrace();

			response = new HTTPResponse(HTTPResponse.STATUS_500);
		}

		return response;
	}

}
