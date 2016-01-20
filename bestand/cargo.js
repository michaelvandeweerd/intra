// the initialisation methods to be called when the document is ready
var initialisations = [];

// the scripts to be appended to the document's head when the document is ready
var scripts = [];

// the links to be appended to the document's head when the document is ready
var links = [];

// indicates whether or not the document has loaded all its resources
var ready = false;

var INDICATOR_CLASSNAME = "cargo-load";
var INDICATOR_CLASSNAME_IDLE = "idle";
var INDICATOR_CLASSNAME_ACTIVE = "active";

var INDICATOR_LABEL_INITIATION = "calling %1()";
var INDICATOR_LABEL_SCRIPT = "loading script %1";
var INDICATOR_LABEL_LINK = "loading resource %1";

// used to store JSON temporarily
var tempJSON = null;

// call all initialisation functions and load all scripts and links when the
// document is ready
window.onload = function() {
	ready = true;

	initialiseIndicators(initialisations.length + scripts.length + links.length);

	for (var i = 0; i < initialisations.length; i++) {
		updateIndicators(INDICATOR_LABEL_INITIATION.replace("%1",
				initialisations[i].name));

		initialisations[i]();
	}

	for (var i = 0; i < scripts.length; i++) {
		updateIndicators(INDICATOR_LABEL_SCRIPT.replace("%1", scripts[i].src));

		document.head.appendChild(scripts[i]);
	}

	for (var i = 0; i < links.length; i++) {
		updateIndicators(INDICATOR_LABEL_LINK.replace("%1", links[i].href));

		document.head.appendChild(links[i]);
	}

	finaliseIndicators();
};

/**
 * The specified function will be called when the document is ready. If the
 * document is already ready it will be called immediately.
 * 
 * @param initiate
 *            The function the be called when the document is ready.
 */
function onReady(initialisation) {
	if (ready) {
		initialisation();
	} else {
		initialisations.push(initialisation);
	}
}

/**
 * Load the script at the specified source when the document is ready.
 * 
 * @param src
 *            The source of the script to be loaded.
 * @param async
 *            Whether or not the script should run asynchronous.
 * @return The loaded script.
 */
function loadScript(src, async) {
	var script = document.createElement("script");

	script.src = src;

	if (async === undefined) {
		script.async = async;
	}

	if (ready) {
		document.head.appendChild(script);
	} else {
		scripts.push(script);
	}

	return script;
}

/**
 * Load the resource with the specified reference, type and relationship.
 * 
 * @param href
 *            The reference to the resource to be loaded.
 * @param type
 *            The type of the resource to be loaded.
 * @param rel
 *            The relationship of the resource to be loaded.
 * @return The loaded link.
 */
function loadLink(href, type, rel) {
	var link = document.createElement("link");

	link.href = href;
	link.type = type;
	link.rel = rel;

	if (ready) {
		document.head.appendChild(link);
	} else {
		links.push(link);
	}

	return link;
}

/**
 * Load a JSONP file from the specified source. When loaded the specified
 * callback function will be called with the JSON data as the first parameter.
 * 
 * @param src
 *            The source of the JSONP file.
 * @param callback
 *            The function to call when the JSONP file is loaded with the
 *            contained JSON data as the first parameter.
 */
function loadJSONP(src, callback) {
	loadScript(src + "&callback=" + callback.name, true);
}

/**
 * Initialise the indicators in the current document by setting the size and to
 * current step (0).
 * 
 * @param size
 *            The size of the indicator.
 */
function initialiseIndicators(size) {
	var indicators = document.getElementsByClassName(INDICATOR_CLASSNAME);

	for (var i = 0; i < indicators.length; i++) {
		indicators[i].dataset.cargoSize = size;
		indicators[i].dataset.cargoStep = 0;

		indicators[i].classList.remove(INDICATOR_CLASSNAME_IDLE);
		indicators[i].classList.add(INDICATOR_CLASSNAME_ACTIVE);
	}
}

/**
 * Update the indicator by incrementing the step and assigning it with the
 * specified label.
 * 
 * @param label
 *            The label of the next step.
 */
function updateIndicators(label) {
	var indicators = document.getElementsByClassName(INDICATOR_CLASSNAME);

	for (var i = 0; i < indicators.length; i++) {
		var step = parseInt(indicators[i].dataset.cargoStep);
		var size = parseInt(indicators[i].dataset.cargoSize);

		step++;

		indicators[i].dataset.cargoStep = step;
		indicators[i].dataset.cargoLabel = label;
		indicators[i].dataset.cargoPercentageDone = (step / size) * 100;
		indicators[i].dataset.cargoPercentageLeft = 100 - (step / size) * 100;
	}
}

/**
 * Finalise the indicators in the current document by removing all data.
 */
function finaliseIndicators() {
	var indicators = document.getElementsByClassName(INDICATOR_CLASSNAME);

	for (var i = 0; i < indicators.length; i++) {
		delete indicators[i].dataset.cargoSize;
		delete indicators[i].dataset.cargoStep;
		delete indicators[i].dataset.cargoLabel;
		delete indicators[i].dataset.cargoPercentageDone;
		delete indicators[i].dataset.cargoPercentageLeft;

		indicators[i].classList.remove(INDICATOR_CLASSNAME_ACTIVE);
		indicators[i].classList.add(INDICATOR_CLASSNAME_IDLE);
	}
}