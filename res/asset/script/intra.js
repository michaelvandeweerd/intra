(function(global) {	
	global.intra = Packages.eu.parcifal.intra;
	global.plus = Packages.eu.parcifal.plus;
	global.console = plus.print.Console;
	
	global.string = function(set, name) {
		try {
			var strings = JSON.parse(
					page.read(
						format("./content/string/%1.json", [set])
					)
				);
			
			var pattern = new RegExp(/([^;,]+)(?:;([^,]*))?(?:,|$)/g);
			var acceptLanguage = request.getMessageHeader("Accept-Language");
			var match;
			
			while(match = pattern.exec(pattern)) {
				for(var i = 0; i < strings[name].length; i++) {
					if(new RegExp(strings[name][i].language).test()) {
						return strings[name][i].value;
					}
				}
			}	
		} catch(exception) {
			return format("./content/string/%1.json#%2", [set, name]);
		}
	};
	
	global.format = function(string, arguments, unescape) {
		if(typeof arguments != "undefined") {
			for (var i = 0; i < arguments.length; i++) {
				string = string.replace(
						new RegExp("(^|[^\\\\])(%" + (i + 1) + "\\$?)([^0-9]|$)", "g"), 
						"$1" + arguments[i] + "$3"
					);
			}
		}

		if (typeof unescape != "undefined") {
			return unescape(string);
		} else {
			return string;
		}
	};
	
	global.unescape = function(string) {
		return string.replace(/\\(.)/g, "$1");
	};

	global.cookie = {
		/**
		 * Return either a set-cookie or a cookie HTTP-message-headers
		 * containing a name equal to the specified name. Set-cookie
		 * HTTP-message-headers are preferred over cookie
		 * HTTP-message-headers.
		 * 
		 * @param name
		 *            The name of the set-cookie or cookie
		 *            HTTP-message-header to be returned.
		 * @return Either a set-cookie or cookie HTTP-message-header
		 *         with a name equal to the specified name or undefined.
		 */
		get : function(name) {
			if(response.hasMessageHeader("Set-Cookie")) {
				var setCookies = response.getMessageHeader("Set-Cookie");
	
				for (var i = 0; i < setCookies; i++) {
					if (setCookies.name == name) {
						return setCookies[i];
					}
				}
			}

			if(request.hasMessageHeader("Cookie")) {
				var cookies = request.getMessageHeader("Cookie");
	
				for (var i = 0; i < cookies; i++) {
					return new RegExp("(?:; |^)" + name
							+ "=([^; ]*)(?:;|$)").exec(cookies)[1];
				}
			}
		},
		set : function(name, value, expires, maxAge, domain, path,
				secure, httponly, extension) {
			var cookie = name + "=" + value;

			if (!(expires === undefined || expires === null)) {
				cookie += ";Expires=" + expires;
			}

			if (!(maxAge === undefined || maxAge === null)) {
				cookie += ";Max-Age=" + maxAge;
			}

			if (!(domain === undefined || domain === null)) {
				cookie += ";Domain=" + domain;
			}

			if (!(path === undefined || path === null)) {
				cookie += ";Path=" + path;
			}

			if (secure) {
				cookie += ";Secure";
			}

			if (httponly) {
				cookie += ";HttpOnly";
			}

			if (!(extension === undefined || extension === null)) {
				for (var i = 0; i < extension.length; i++) {
					cookie += ";" + extension[i];
				}
			}

			response.addMessageHeader("Set-Cookie", cookie);
		}
	};
	
	var printed = "";
	
	global.print = function(string, arguments) {
		if(typeof arguments == "undefined") {
			printed += string;
		} else {
			printed += format(string, arguments);
		}
	};
	
	global.printLine = function(string, arguments) {
		print(string + "\n", arguments);
	};
	
	global.printString = function(set, name, arguments) {
		print(string(set, name), arguments);
	}
	
	global.printStringLine = function(set, name, arguments) {
		printLine(string(set, name), arguments);
	}
	
	global.printPage = function(location) {
		print(read(format("./content/%1.ejs", [ location ])));
	}
	
	global.flushPrinter = function() {
		var old = printed;
		
		printed = "";
		
		return old;
	};
	
	global.read = function(location) {
		return page.evaluate(page.read(location));
	}
	
	global.load = function(location) {
		return page.read(location);
	}
}) (this);