var HTTPMessage = HTTPMessage || (function() {
	var STRING_FORMAT = "%1\r\n%2\r\n%3";

	/**
	 * public HTTPMessage(HTTPStartLine, HTTPMessageHeader[],
	 * HTTPMessageBody)
	 * 
	 * Construct a new HTTP-message, containing an HTTP-start-line, zero
	 * or more HTTP-message-headers and a possibly empty
	 * HTTP-message-body.
	 * 
	 * @param startLine
	 *            The HTTP-start-line to be contained in the new
	 *            HTTP-message.
	 * @param messageHeader
	 *            An array of HTTP-message-headers to be contained in
	 *            the new HTTP-message.
	 * @param messageBody
	 *            The possibly empty HTTP-message-body to be contained
	 *            in the new HTTP-message.
	 */
	var HTTPMessage = function(startLine, messageHeaders, messageBody) {
		this.startLine = startLine;
		this.messageHeaders = messageHeaders || [];
		this.messageBody = messageBody || new HTTPMessageBody();
	};

	HTTPMessage.prototype = {
		messageHeader : function(name, value, replace) {
			replace = replace || true;
			
			if (typeof name == "undefined") {
				return this.messageHeaders;
			} else if (typeof value == "undefined") {
				var messageHeaders = [];

				for (var i = 0; i < this.messageHeaders.length; i++) {
					if (this.messageHeaders[i].name == name) {
						messageHeaders.push(this.messageHeaders[i]);
					}
				}

				return messagHeaders.length > 1 ? messageHeaders : messageHeaders[0];
			} else {
				if(replace) {
					for(var i = 0; i < this.messageHeaders.length; i++) {
						if(this.messageHeaders[i].name == name) {
							return this.messageHeaders[i] = new HTTPMessageHeader(name, value);
						}
					}
				} else {
					return this.messageHeaders.push(new HTTPMessageHeader(name, value));	
				}
			}
		},
		
		/**
		 * public String toString()
		 * 
		 * Return the current HTTP-message as a string.
		 */
		toString : function() {
			var messageHeaders = "";

			for (var i = 0; i < this.messageHeaders.length; i++) {
				messageHeaders += this.messageHeaders[i].toString();
			}
			
			return intra.format(STRING_FORMAT, [ this.startLine.toString(), messageHeaders, this.messageBody.toString() ]);
		}
	}

	return HTTPMessage;
})();

var HTTPRequest = HTTPRequest || (function() {
	var STRING_PATTERN = /(.*)\r?\n((?:.*\r?\n)*)\r?\n(.*)?/;

	/**
	 * public HTTPRequest(HTTPRequestLine, HTTPMessageHeader[], HTTPMessageBody)
	 * 
	 * Construct a new HTTP-message-request containing a HTTP-request-line, zero
	 * or more HTTP-message-headers and a possibly empty HTTP-message-body.
	 * 
	 * @param requestLine
	 *            The HTTP-request-line to be contained in the new HTTP-request.
	 * @param messageHeaders
	 *            An array of HTTP-message-headers to be contained in the new
	 *            HTTP-request.
	 * @param messageBody
	 *            The possibly empty HTTP-message-body to be contained in the
	 *            new HTTP-request.
	 */
	HTTPRequest = function(requestLine, messageHeaders, messageBody) {
		HTTPMessage.call(this, requestLine, messageHeaders, messageBody);
	};

	HTTPRequest.prototype = Object.create(HTTPMessage.prototype, {

	});

	/**
	 * public static HTTPRequest fromString(String)
	 * 
	 * Return the specified string as a HTTP-request.
	 * 
	 * @param string
	 *            The string representing a HTTP-request.
	 * @return The HTTP-request represented in the specified string.
	 */
	HTTPRequest.fromString = function(string) {
		var match = STRING_PATTERN.exec(string);

		var requestLine = HTTPRequestLine.fromString(match[1]);
		var messageHeaders = HTTPMessageHeader.fromString(match[2]);
		var messageBody = new HTTPMessageBody(match[3]);

		return new HTTPRequest(requestLine, messageHeaders, messageBody);
	};

	return HTTPRequest;
})();

var HTTPResponse = HTTPResponse || (function() {
	var STRING_PATTERN = /(.*)\r?\n((?:.*\r?\n)*)\r?\n(.*)?/;

	/**
	 * public HTTPResponse(HTTPStatusLine, HTTPMessageHeader[], HTTPMessageBody)
	 * 
	 * Construct a new HTTP-response containing an HTTP-status-line, zero or
	 * more HTTP-message-headers and a possibly empty HTTP-message-body.
	 * 
	 * @param statusLine
	 *            The HTTP-status-line to be contained in the new HTTP-response.
	 * @param messageHeaders
	 *            An array of HTTP-message-headers to be contained in the net
	 *            HTTP-response.
	 * @param messageBody
	 *            The possibly empty HTTP-message-body to be contained in the
	 *            new HTTP-request.
	 */
	var HTTPResponse = function(statusLine, messageHeaders, messageBody) {
		HTTPMessage.call(this, statusLine, messageHeaders, messageBody);
	};

	HTTPResponse.prototype = Object.create(HTTPMessage.prototype, {
		
	});

	/**
	 * public static HTTPResponse fromString(String)
	 */
	HTTPResponse.fromString = function(string) {
		var match = STRING_PATTERN.exec(string);

		var statusLine = HTTPStatusLine.fromString(match[1]);
		var messageHeaders = HTTPMessageHeader.fromString(match[2]);
		var messageBody = new HTTPMessageBody(match[3]);

		return new HTTPResponse(statusLine, messageHeaders, messageBody);
	};

	return HTTPResponse;
})();

var HTTPStartLine = HTTPStartLine || (function() {
	/**
	 * public HTTPStartLine(HTTPVersion)
	 * 
	 * Construct a new HTTP-start-line containing an HTTP-version.
	 * 
	 * @param version
	 *            The HTTP-version to be contained in the new HTTP-start-line.
	 */
	var HTTPStartLine = function(version) {
		this.version = version;
	};

	return HTTPStartLine;
})();

var HTTPRequestLine = HTTPRequestLine || (function() {
	var STRING_FORMAT = "%1 %2 %3";
	var STRING_PATTERN = /^([^ ]+) ([^ ]+) ([^ ]+)$/;

	/**
	 * public HTTPRequestLine(HTTPVersion, String, URI) extends
	 * HTTPStartLine
	 * 
	 * Construct a new HTTP-request-line containing an HTTP-version,
	 * method and request-URI.
	 * 
	 * @param method
	 *            The method to be contained in the new
	 *            HTTP-request-line.
	 * @param requestURI
	 *            The URI to be contained in the new HTTP-request-line.
	 * @param version
	 *            The HTTP-version to be contained in the new
	 *            HTTP-request-line.
	 */
	var HTTPRequestLine = function(method, requestURI, version) {
		HTTPStartLine.call(this, version);

		this.method = method;
		this.requestURI = requestURI;
	};
	
	HTTPRequest.prototype = {
		toString : function() {
			return intra.format(STRING_FORMAT, [ this.method, this.requestURI.toString(), this.version.toString() ]);
		}
	};

	HTTPRequestLine.fromString = function(string) {
		var match = STRING_PATTERN.exec(string);

		var method = match[1];
		var requestURI = URI.fromString(match[2]);
		var version = HTTPVersion.fromString(match[3]);

		return new HTTPRequestLine(method, requestURI, version);
	};

	return HTTPRequestLine;
})();

var HTTPStatusLine = HTTPStatusLine || (function() {
	var STRING_FORMAT = "%1 %2 %3";
	var STRING_PATTERN = /^([^ ]+) ([^ ]+) ([^ ]+)$/;

	/**
	 * public HTTPStatusLine(Integer, String, HTTPVersion) extends
	 * HTTPStartLine
	 * 
	 * Construct a new HTTP-status-line containing an status-code,
	 * reason-phrase an HTTP-version.
	 * 
	 * @param version
	 *            The HTTP-version to be contained in the new
	 *            HTTP-status-line.
	 * @param statusCode
	 *            The status-code to be contained in the new
	 *            HTTP-status-line.
	 * @param reasonPhrase
	 *            The reason-phrase to be contained in the new
	 *            HTTP-status-line.
	 */
	var HTTPStatusLine = function(version, statusCode, reasonPhrase) {
		HTTPStartLine.call(this, version);

		this.statusCode = statusCode;
		this.reasonPhrase = reasonPhrase;
	};

	HTTPStatusLine.prototype = {
		toString : function() {
			return intra.format(STRING_FORMAT, [ this.version, this.statusCode, this.reasonPhrase ]);
		}
	};

	HTTPStatusLine.fromString = function(string) {
		var match = STRING_PATTERN.exec(string);

		var version = HTTPVersion.fromString(match[1]);
		var statusCode = parseInt(match[2]);
		var reasonPhrase = match[3];

		return new HTTPStatusLine(version, statusCode, reasonPhrase);
	};

	return HTTPStatusLine;
})();

var HTTPMessageHeader = HTTPMessageHeader || (function() {
	var STRING_FORMAT = "%1: %2\r\n";
	var STRING_PATTERN = /^([^:]+):[\t ]*(.*)$/gm;

	/**
	 * public HTTPMessageHeader(String, String)
	 * 
	 * Construct a new HTTP-message-header containing a name and value.
	 * 
	 * @param name
	 *            The name to be contained in the new HTTP-message-header.
	 * @param value
	 *            The value to be contained in the new HTTP-message-header.
	 */
	var HTTPMessageHeader = function(name, value) {
		this.name = name;
		this.value = value;
	};

	HTTPMessageHeader.fromString = function(string) {
		var match;
		var messageHeaders = [];

		while (match = new RegExp(STRING_PATTERN).exec(string)) {
			messageHeaders.push(new HTTPMessageHeader(match[1], match[2]));
		}

		return messageHeaders;
	};

	HTTPMessageHeader.prototype = {
		toString : function() {
			return intra.format(STRING_FORMAT, [ this.name, this.value ]);
		}
	};

	return HTTPMessageHeader;
})();

var HTTPMessageBody = HTTPMessageBody || (function() {
	/**
	 * public HTTPMessageBody(String)
	 * 
	 * Construct a new HTTP-message-body containing a content-body.
	 * 
	 * @param contentBody
	 *            The content-body to be contained in the new HTTP-message-body.
	 */
	var HTTPMessageBody = function(contentBody) {
		this.contentBody = contentBody;
	};

	HTTPMessageBody.prototype = {
		toString : function() {
			return this.contentBody;
		}
	};

	return HTTPMessageBody;
})();

var HTTPVersion = HTTPVersion || (function() {
	/**
	 * private static STRING_FORMAT
	 * 
	 * The default string format for an HTTP-version.
	 */
	var STRING_FORMAT = "HTTP/%1.%2";

	var STRING_PATTERN = /^HTTP\/([0-9])\.([0-9])/;

	/**
	 * public HTTPVersion(Integer, Integer)
	 * 
	 * Construct a new HTTP-version containing a major and minor value.
	 * 
	 * @param major
	 *            The major value to be contained in the new HTTP-version.
	 * @param minor
	 *            The minor value to be contained in the new HTTP-version.
	 */
	var HTTPVersion = function(major, minor) {
		this.major = major;
		this.minor = minor;
	};

	/**
	 * public static VERSION_1_0
	 * 
	 * The default definition for HTTP/1.0.
	 */
	HTTPVersion.VERSION_1_0 = new HTTPVersion(1, 0);

	/**
	 * public static VERSION_1_1
	 * 
	 * The default definition for HTTP/1.1.
	 */
	HTTPVersion.VERSION_1_1 = new HTTPVersion(1, 1);

	HTTPVersion.prototype = {
		/**
		 * Return the current HTTP-version as a string.
		 * 
		 * @return The current HTTP-version as a string.
		 */
		toString : function() {
			return intra.format(STRING_FORMAT, [ this.major, this.minor ]);
		}
	};

	/**
	 * Return a HTTP-version represented in the specified string.
	 * 
	 * @param string
	 *            The string representing the HTTP-version to be returned.
	 * @return The HTTP-version represented in the specified string.
	 */
	HTTPVersion.fromString = function(string) {
		var match = STRING_PATTERN.exec(string);

		var major = parseInt(match[1]);
		var minor = parseInt(match[2]);

		return new HTTPVersion(major, minor);
	}

	return HTTPVersion;
})();