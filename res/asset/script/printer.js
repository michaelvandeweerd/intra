var Printer = Printer || (function() {	
	var Printer = function(targets) {
		this.targets = targets;
	};
	
	Printer.prototype = {
		log : function(content, arguments) {
			for(var i = 0; i < this.targets.length; i++) {
				this.targets[i](format(content, arguments));
			}
		}	
	};
	
	return Printer;
})();