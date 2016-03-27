var Plus = Plus || (function(){
	var Plus = function() {
		
	};
	
	Plus.prototype = {
		require : function(string) {
			try {
				return eval(Packages.eu.parcifal.intra.content.Page.read(intra.format("./res/asset/script/%1.js")));
			} catch(exception) {
				return false;
			}
		},
		format : function(string, arguments, unescape) {
			if(typeof arguments != "undefined") {
				for (var i = 0; i < arguments.length; i++) {
					string = string.replace(
							new RegExp("(^|[^\\\\])(%" + (i + 1) + "\\$?)([^0-9]|$)", "g"), 
							"$1" + arguments[i] + "$3"
						);
				}
			}
	
			if (typeof unescape != "undefined") {
				return intra.unescape(string);
			} else {
				return string;
			}
		},
		unescape : function(string) {
			return string.replace(/\\(.)/g, "$1");
		},
		cookie : {
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
				var setCookies = response.getMessageHeaders("Set-Cookie");
	
				for (var i = 0; i < setCookies; i++) {
					if (setCookies.name == name) {
						return setCookies[i];
					}
				}
	
				var cookies = response.getMessageHeaders("Cookie");
	
				for (var i = 0; i < cookies; i++) {
					return new RegExp("(?:; |^)" + name
							+ "=([^; ]*)(?:;|$)").exec(cookies)[1];
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
	
				response.messageHeaders.push(new HTTPMessageHeader(
						"Set-Cookie", cookie));
			}
		},
		print : function(string, arguments) {
			if(typeof this.output == "undefined") {
				this.output = intra.format(string, arguments);
			} else {
				this.output += intra.format(string, arguments);
			}
		},
		resetOutput : function() {
			var output = this.output;
			
			this.output = "";
			
			return output;
		},
		string : function(name) {
			try {
				var strings = JSON.parse(
						Packages.eu.parcifal.intra.content.Page.read(
								intra.format("./res/string/%1.json", [name])
							)
					);
				
				var pattern = new RegExp(/([^;,]+)(?:;([^,]*))?(?:,|$)/g);
				var acceptLanguage = request.getHeader("Accept-Language");
				var match;
				
				while(match = pattern.exec(pattern)) {
					
				}	
			} catch(exception) {
				return intra.format("string \"%1\" not found", [name]);
			}
		}
	};
	
	return Plus;
})();