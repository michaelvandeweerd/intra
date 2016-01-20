onReady(initialisation);

loadLink("http://file.parcifal.eu.localhost/kort.css", "text/css", "stylesheet");

function initialisation() {
	var forms = Array.prototype.slice.call(document
			.getElementsByClassName("kort"));

	for (var i = 0; i < forms.length; i++) {
		forms[i]['submit'].onclick = onFormSubmit;
		forms[i]['is-custom'].onclick = onToggleCustomIdentifier;
		forms[i]['identifier'].onkeypress = onIdentifierKeyPress;
		forms[i]['identifier'].onblur = onIdentifierBlur;
		forms[i].classList.add("enabled");
	}
}

function onFormSubmit(event) {
	event.preventDefault();

	var submit = event.target;
	var form = getParentElementByTagName(submit, "form");

	var reference = form['reference'].value;
	var isCustom = form['is-custom'].checked;

	if (isCustom) {
		var identifier = form['identifier'].value;
		var source = "http://parcif.al/log?target=" + reference
				+ "&identifier=" + identifier;
	} else {
		var source = "http://parcif.al/log?target=" + reference;
	}

	alert(source);
}

function onToggleCustomIdentifier(event) {
	var checkbox = event.target;
	var form = getParentElementByTagName(checkbox, "form");
	var identifier = form['identifier'];

	if (checkbox.checked && !identifier.classList.contains("enabled")) {
		identifier.classList.add("enabled");
	} else if (!checkbox.checked && identifier.classList.contains("enabled")) {
		identifier.classList.remove("enabled");
	}
}

function onIdentifierKeyPress(event) {
	return event.charCode === 0
			|| /[a-z-]/.test(String.fromCharCode(event.charCode));
}

function onIdentifierBlur(event) {
	var identifier = event.target;

	if (identifier.value != "") {
		var dashes = 8 - identifier.value.length;

		for (var i = 0; i < dashes; i++) {
			identifier.value += "-";
		}
	}
}

function getParentElementByTagName(child, tagName) {
	if (child == document)
		return false;

	if (child.tagName == tagName.toUpperCase())
		return child;

	return getParentElementByTagName(child.parentNode, tagName);
}