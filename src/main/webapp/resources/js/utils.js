/**
 * 
 */

function contains(itemLabel, filterValue) {

    return itemLabel.includes(filterValue) || specialCharacters(itemLabel).includes(filterValue);

}

function specialCharacters(input){

           var c=input.toLowerCase();
           c = c.replace(new RegExp("\\s", 'g')," ");
           c = c.replace(new RegExp("[àáâãäå]", 'g'),"a");
           c = c.replace(new RegExp("æ", 'g'),"ae");
           c = c.replace(new RegExp("ç", 'g'),"c");
           c = c.replace(new RegExp("[èéêë]", 'g'),"e");
           c = c.replace(new RegExp("[ìíîï]", 'g'),"i");                           
           c = c.replace(new RegExp("[òóôõö]", 'g'),"o");
           c = c.replace(new RegExp("œ", 'g'),"oe");
           c = c.replace(new RegExp("[ùúûü]", 'g'),"u");
           c = c.replace(new RegExp("[ýÿ]", 'g'),"y");
           return c;
}

var lastFocus=null;

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