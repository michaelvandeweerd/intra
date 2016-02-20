package eu.parcifal.intra.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import eu.parcifal.intra.http.HTTPMessageBody;
import eu.parcifal.intra.http.HTTPMessageHeader;
import eu.parcifal.intra.http.HTTPRequest;
import eu.parcifal.intra.http.HTTPResponse;
import eu.parcifal.intra.http.HTTPStatusLine;
import eu.parcifal.plus.logic.Executable;
import eu.parcifal.plus.print.Console;

public abstract class Context implements Executable {

	protected HTTPRequest request;

	@Override
	public HTTPResponse execute(Object... args) {
		if (args.length < 1 || !(args[0] instanceof HTTPRequest)) {
			return new HTTPResponse(HTTPStatusLine.STATUS_500_1_1);
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

	protected byte[] load(String path) {
		Console.log(path);
		
		File file = new File(path);

		if (file.exists()) {
			FileInputStream stream = null;

			try {
				byte[] content = new byte[(int) file.length()];

				stream = new FileInputStream(file);

				stream.read(content);

				return content;
			} catch (IOException e) {
				throw new RuntimeException();
			} finally {
				try {
					stream.close();
				} catch (IOException e) {
					throw new RuntimeException();
				}
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

}
