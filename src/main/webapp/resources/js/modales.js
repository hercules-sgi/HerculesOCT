/**
 * 
 */

/* modales */
var lastFocus=null;
var modalFocus=null;

function goToLastFocus() {
	if (lastFocus) {
		lastFocus.focus();
	}
}
function saveLastFocus(event) {
	try {
		lastFocus = event.target;
		if (!$(lastFocus).is(':focusable')) {
			lastFocus = event.target.parentElement;
		}
	} catch ( error ) {
		console.error( error );
	}
}
function goToModalFocus() {
	if (modalFocus) {
		modalFocus.focus();
	}
}
function saveModalFocus(event) {
	try {
		modalFocus = event.target;
		if (!$(modalFocus).is(':focusable')) {
			modalFocus = event.target.parentElement;
		}
	} catch ( error ) {
		console.error( error );
	}
}

