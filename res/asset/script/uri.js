var URI = URI || (function() {
			var STRING_PATTERN = /^(?:([a-zA-Z][a-zA-Z0-9+\-.]*):)?(?:\/{2}(?:((?:[a-zA-z0-9\-._~]|(?:%[a-fA-F0-9]{2})|[!$&'()*+,;=]|:)*)@)?((?:[a-zA-Z0-9\-._~]|(?:%[a-fA-F0-9]{2})|[!$&'()*+,;=])*)(:[0-9]*)?)?(?:((?:\/(?:[a-zA-Z0-9\-._~]|(?:%[a-fA-F0-9]{2})|[!$&'()*+,;=]|:|@)*)*))?(?:\?((?:[a-zA-Z0-9\-._~]|(?:%[a-fA-F0-9]{2})|[!$&'()*+,;=]|:|@|\\|\?)*))?(?:#((?:[a-zA-Z0-9\-._~]|(?:%[a-fA-F0-9]{2})|[!$&'()*+,;=]|:|@|\\|\?)*))?/;

			var URI = function(scheme, userinfo, host, port, path, query, fragment) {
				this.scheme = scheme;
				this.userinfo = userinfo;
				this.host = host;
				this.port = port || -1;
				this.path = path;
				this.query = query;
				this.fragment = fragment;
			};

			URI.prototype = {
				authority : function() {
					var authority = "//";

					if (this.userinfo) {
						authority += this.userinfo + "@";
					}

					if (this.host) {
						authority += this.host;
					}

					if (this.port >= 0) {
						authority += this.port;
					}

					return authority;
				},
				/**
				 * Return the GET parameter defined in the query of the current
				 * URI. If the specified parameter has no value, return true.
				 * 
				 * @param name
				 *            The name of the GET parameter whose value must be
				 *            returned.
				 * @return The value of the specified GET parameter, or true if
				 *         no parameter is available.
				 */
				get : function(name) {
					var match = new RegExp("(?:^|\\?|&)" + name + "(?:=([^&#]*))?(?:&|#|$)")
							.exec(this.query);
					if (match) {
						if (typeof match[1] == "undefined") {
							return true;
						} else {
							return match[1];
						}
					}
				},
				toString : function() {
					var string = "";

					if (this.scheme) {
						string += this.scheme + ":";
					}

					if (this.authority()) {
						string += this.authority();
					}

					if (this.path) {
						string += this.path;
					}

					if (this.query) {
						string += "?" + this.query;
					}

					if (this.fragment) {
						string += "#" + this.fragment;
					}

					return string;
				}
			};

			URI.fromString = function(string) {
				var match = STRING_PATTERN.exec(string);

				var scheme = match[1];
				var userinfo = match[2];
				var host = match[3];
				var port = parseInt(match[4]);
				var path = match[5];
				var query = match[6];
				var fragment = match[7];

				return new URI(scheme, userinfo, host, port, path, query, fragment);
			}

			return URI;
		})();