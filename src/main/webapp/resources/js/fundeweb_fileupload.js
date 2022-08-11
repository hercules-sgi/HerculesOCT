/*
 * Librer�a de funciones Javascript
 */

/**
 * Funcion que indica si una cadena es vacia, es decir, es nula o tiene longitud 0
 * 
 * @param cadena --
 *                La cadena a comprobar
 * @return -- true si la cadena es vacia, false si no es vacia
 */
function esCadenaVacia(cadena) {
    if ((cadena === null) || (cadena.length === 0)) {
	return true;
    }
    return false;
}

/**
 * Funcion que indica si un array es vacio, es decir, es nulo o tiene longitud 0
 * 
 * @param arrays --
 *                el array a comprobar
 * @return -- true si el array es vacio, false si no es vacio
 */
function esArrayVacio(arrays) {
    if ((arrays === null) || (arrays.size() === 0)) {
	return true;
    }
    return false;
}

/**
 * Funcion que obtiene la parte final del identificador del fichero
 * 
 * @param fichero --
 *                fichero a procesar
 * @return -- Cadena que contiene la parte final del identificador del fichero
 */
function obtenerIdFichero(fichero) {
    if (fichero !== null) {
	var ficheroId = $.trim($(fichero).attr('id'));
	
	if (!esCadenaVacia(ficheroId)) {
	    return ficheroId.substring(ficheroId.lastIndexOf(":") + 1);
	}
    }
    return null;
}

/**
 * Funcion que indica si hay extensiones definidas en el array extensiones
 * 
 * @param extensiones --
 *                el array que contiene las extensiones
 * @return boolean -- true si hay extensiones, false si no hay extensiones
 */
function hayExtensionesDefinidas(extensiones) {
    if (!esArrayVacio(extensiones)) {
	return true;
    }
    return false;
}

/**
 * Funcion que comprueba si un fichero es obligatorio (required=true)
 * 
 * @param fichero --
 *                fichero a comprobar la obligatoriedad
 * @return -- true si el fichero es obligatorio, false si no es obligatorio
 */
 function checkRequiredFile(fichero) {
     if ($(fichero).parent().prev('label').children('span:first').hasClass('required')) {
	 // El fichero es obligatorio
	 return true;	
     }
     // El fichero no es obligatorio
     return false;
 }
 
/**
 * Funcion que indica si no hay un fichero seleccionado en el componente type=file de HTML cuando es obligatoria su
 * seleccion, (required=true)
 * 
 * @param fichero --
 *                el componente FILE html
 * @return boolean -- true si no hay fichero seleccionado, false si hay fichero seleccionado o si no la hay si no es
 *         obligatorio
 */
function checkEmptyRequiredFile(fichero) {
    var docName = $.trim($(fichero).val());
    
    if (checkRequiredFile(fichero)) {
	// El fichero es obligatorio
	if (esCadenaVacia(docName)) {
	    // No hemos seleccionado el fichero
	    establecerEstiloError(fichero, file_required_message);
	    // $(fichero).focus();
	    return true;
	}
	else {
	    // Hemos seleccionado un fichero
	    quitarEstiloError(fichero);
	    // return false;
	}
    }
    // El fichero no es obligatorio
    return false;
}

/**
 * Funcion que establece el estilo de error para los componentes de la plantilla definida en editFileUpload.xhtml
 * 
 * @param fichero --
 *                fichero cuyos componentes asociados cambian de estilo
 * @param mensaje --
 *                el texto del mensaje de error
 */
function establecerEstiloError(fichero, mensaje) {
    var label = $(fichero).parent().prev('label'),
    value = $(fichero).parent(),
    error = $(fichero).parent().next('span');

    $(label).addClass('errors');
    $(value).addClass('errors');
    // $(error).html('<IMG class=errors src="/plantillaFundeweb/resources/img/error.gif"><SPAN class=errors>' + mensaje +
    // '</SPAN> ');
    if ($(error).length === 0) {
	// Si no existe el SPAN para mostrar el mensaje de error lo creamos
	$(value).parent().append('<SPAN class=error><IMG alt="Error" class="errors" src="/plantillaFundeweb/resources/img/error.gif"><SPAN class="errors"> ' + mensaje + ' </SPAN></SPAN>');
    }
    else {
	// Si existe el SPAN para mostrar el mensaje de error lo rellenamos
	$(error).html('<IMG alt="Error" class="errors" src="/plantillaFundeweb/resources/img/error.gif"><SPAN class="errors"> ' + mensaje + ' </SPAN> ');
    }
}

/**
 * Funcion que establece elimina el estilo de error para los componentes de la plantilla definida en
 * editFileUpload.xhtml
 * 
 * @param fichero --
 *                fichero cuyos componentes asociados cambian de estilo
 */
function quitarEstiloError(fichero) {
    var label = $(fichero).parent().prev('label'),
    value = $(fichero).parent(),
    error = $(fichero).parent().next('span');

    $(label).removeClass('errors');
    $(value).removeClass('errors');
    if ($(error).length > 0) {
	// Si existe el SPAN para mostrar el mensaje de error lo eliminamos
	// $(error).html('');
	$(error).remove();
    }
}

/**
 * Funcion que indica si la extension del fichero coincide con alguna de las definidas en el array de extensiones
 * 
 * @param extension_fichero --
 *                extension del fichero a validar
 * @param extensiones --
 *                array de extensiones
 * @return boolean -- true si la extension del fichero conincide con alguna de las definidas en el array o no hay
 *         extensiones definidas, false si la extension no es valida
 */
function checkExtensionFile(extension_fichero, extensiones) {
    if (hayExtensionesDefinidas(extensiones)) {
	// Hay extensiones definidas
	for (var i=0; i < extensiones.size(); i++) {
	    if (extension_fichero == extensiones[i]) {
		// extension del fichero valida
		return true;
	    }
	}
	// Hay extensiones, pero la extension no es valida
	return false;
    }
    // No hay extensiones definidas
    return true;
}

/**
 * Funcion que indica si el fichero es valido, comprobando la extension si el array de extensiones esta definido
 * 
 * @param fichero --
 *                componente fichero (el componente html de type=file)
 * @param label --
 *                componente label
 * @param value --
 *                componente value (el componente html de type=file)
 * @param error --
 *                componente que compone el mensaje de error
 * @return -- true si el fichero es valido o no hay restriccion de extensiones definidas, false si no es valido
 */
function checkAcceptFile(fichero) {
    var docName = $.trim($(fichero).val()),
    docExt = null;
    
    if (hayExtensionesDefinidas(file_extensions)) {
	if (!esCadenaVacia(docName)) { 
	    // Tenemos nombre de fichero
	    docExt = docName.substring(docName.lastIndexOf(".")+1).toLowerCase();
	    if (!esCadenaVacia(docExt)) {
		// El fichero tiene extension
		if (!checkExtensionFile(docExt, file_extensions)) {
		    // Extension no valida
		    establecerEstiloError(fichero, file_extension_message);
		    // $(fichero).focus();
		    return false;
		}
		else {
		    // Extension valida
		    quitarEstiloError(fichero);
		    return true;
		}
	    }
	    else {
		// El fichero no tiene extension
		return false;
	    }
	}
    }
    // Si no hay extensiones definidas o no se ha seleccionado el fichero
    return true;
}

/**
 * Activacion de JQuery:
 *  - Chequeamos en los eventos onblur y change de componente type=file de HTML - Tambien en el evento submit del
 * formulario
 * 
 */
$(document).ready(function() {
	
    var hayClickIE = false,
    clickTime = 0;

// Cuando cambiamos el fichero, vuelve a redibujar el componente
    $("input[type=file]").change(function() {
	// alert('onchange');
	if (!checkEmptyRequiredFile(this)) {
	    checkAcceptFile(this);
	}
    });

// Cuando cambiamos el fichero, vuelve a redibujar el componente
    $("input[type=file]").click(function(){
	// alert('onclick');
	
	if ($.browser.msie) {
	    hayClickIE = true;
	    clickTime = new Date();
	}		
    });

// Cuando se pierde el foco en IE redibujar el componente (IE permite EDITAR la URL)
    $("input[type=file]").blur(function(){
	// alert('onblur');
	if (!hayClickIE) { // No es IE
	    if (!checkEmptyRequiredFile(this)) {
		checkAcceptFile(this);
	    }
	}
	else { // Es IE
	    var onblurTime = new Date();
	    if ((onblurTime.valueOf() - clickTime.valueOf()) > 50) {
		// Si tardo mas de 50 ms, valido, asumo que no pincho en el boton examinar
		if (!checkEmptyRequiredFile(this)) {
		    checkAcceptFile(this);
		}			
	    }
	}
	hayClickIE = false;
	clickTime = 0;
    });

    $("#" + formulario).submit(function() {
	// alert('submit');
	var hacerSubmit = true,
	parcialSubmit = true;
	
	$("input[type=file]").each(function() {
	    parcialSubmit = true;
	    if (!checkEmptyRequiredFile(this)) {
		// Hay fichero obligatorio seleccionado
		parcialSubmit = checkAcceptFile(this);
	    }
	    else {
		// No hay fichero obligatorio seleccionado
		parcialSubmit = false;
	    }
	    hacerSubmit = hacerSubmit && parcialSubmit;
	});
	if (!hacerSubmit) { // Si no hacemos submit de formulario, desbloqueamos
	    fin_ajax();
	    evento.preventDefault();
	    evento.stopPropagation();
	}
    });
});
