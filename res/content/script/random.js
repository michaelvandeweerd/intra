// define the encoding of the response
var ENCODING = "UTF-8";

// define the default values for the parameters
var DEFAULT_LENGTH = 16;
var DEFAULT_CHARACTERS = "0123456789abcdefghijklmnopqrstuvfxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

// define the format of the result to be returned
var format = /^[^.]*.(.*)$/.exec(request.startLine.requestURI.path)[1];
var callback = request.startLine.requestURI.get("callback");

if(typeof format === 'undefined' || (format == "jsonp" && typeof callback == "undefined")) {
	// the request is invalid, return a status code accordingly
	response.statusLine = new HTTPStatusLine(HTTPVersion.HTTP_1_1, 400, "Bad Request");
} else {
	// define the parameters
	var length = parseInt(request.startLine.requestURI.get("length")) || DEFAULT_LENGTH;
	var characters = request.startLine.requestURI.get("characters") || DEFAULT_CHARACTERS;

	var value = "";

	// generate a random value
	for(var i = 0; i < length; i++) {
		value += characters.charAt((Math.random() * characters.length));
	}

	var contentType;
	var contentBody;

	switch(format) {
	case "txt":
		// return a plain-text string
		contentType = "text/plain";
		contentBody = value;
		break;
	case "xml":
		// return an XML string
		contentType = "text/xml";
		contentBody = intra.format(
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?><random format=\"%2\" length=\"%3\" characters=\"%4\">%1</random>",
			[value, format, length, characters]
		);
		break;
	case "json":
		// return a JSON string
		contentType = "application/json";
		contentBody = JSON.stringify({
			value: value,
			format: format,
			length: length,
			characters: characters
		});
		break;
	case "jsonp":
		// return a JavaScript object embedded in a JavaScript function
		contentType = "text/javascript";
		contentBody = intra.format(
				"%1(JSON.parse(%2));", 
				[callback, JSON.stringify({
					value : value, 
					format : format, 
					length : length, 
					characters : characters
				})]
		);
	}

	// define all message headers
	response.messageHeader("Content-Type", contentType);
	response.messageHeader("Content-Length", contentBody.length);

	// define the message body
	response.messageBody = new HTTPMessageBody(contentBody);
}
