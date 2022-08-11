/**
 * Variable para cancelar el SUBMIT del formulario
 */
var cancelarSubmit = false,
	type_mail_text = null,
	comprobarPassword = true,
	inputUserLogin,
	inputPasswordLogin,
	selectTipoLogin;

function procesarCorreo(correo) {
	var arroba = null, dominio = null;

	if (!isEmpty(correo)) {
		arroba = correo.lastIndexOf("@");
		if (arroba == -1) {// No hay arroba
			correo += "@um.es";
		} else { // Hay arroba
			dominio = correo.substring(arroba + 1, correo.length);
			if (dominio == 'alu.um.es') {
				correo = correo.substring(0, arroba) + '@um.es';
			}
		}
	}
	return correo;
}

function inhabilitarCampos() {
	$(inputUserLogin).attr("disabled", true);
	$(inputUserLogin).addClass('deshabilitado');
	$(inputUserLogin).val('');
	$(inputPasswordLogin).attr("disabled", true);
	$(inputPasswordLogin).val('');
	$(inputPasswordLogin).addClass('deshabilitado');
}

function habilitarCampos() {
	$(inputUserLogin).removeAttr("disabled");
	$(inputUserLogin).val('');
	$(inputUserLogin).removeClass('deshabilitado');
	$(inputPasswordLogin).removeAttr("disabled");
	$(inputPasswordLogin).val('');
	$(inputPasswordLogin).removeClass('deshabilitado');
}

/* Comprueba si tiene el formato de un NIE: X y 7 cifras numéricas (XNNNNNNN) */
function letraNIF(nif) {
	var lockup = 'TRWAGMYFPDXBNJZSQVHLCKE';
	return lockup.charAt(nif % 23);
}

function isEmpty(inputStr) {
	if (inputStr === null || inputStr === '') {
		return true;
	}
	return false;
}

function isNIU(inputStr) {
	return verificaFormato(inputStr, /^[Uu]\d{7}[A-Za-z]$/);
}

//comprueba si tiene el formato de un NIF,NIU o NIE
function verificaFormato(inputStr, re) {
	if (isEmpty(inputStr) || (inputStr.length != 9) || !(re.test(inputStr))
			|| !validaLetra(inputStr)) {
		return false;
	}
	return true;
}

function validaLetra(nif) {
	//alert('validaLetra')
	return (nif.substr(8, 1).toUpperCase() == letraDNI(nif.toUpperCase()
			.substr(0, 8)));
}

function letraDNI(dni) {
	//alert(dni);
	var dni2 = dni.replace('U', '0').replace('X', '0').replace('M', '0')
			.replace('Y', '1').replace('Z', '2');
	var letrasNIF = "TRWAGMYFPDXBNJZSQVHLCKE";
	var r = dni2 % 23;
	return letrasNIF.substring(r, r + 1);
}

/* Comprueba si tiene el formato de un NIF: 8 cifras numéricas (NNNNNNNN) */
function isNIF(inputStr) {
	if (isEmpty(inputStr) || inputStr.length != 9) {
		return false;
	}
	// Comprobar formato NIF
	if (!/^\d{8}[A-Za-z]$/.test(inputStr)) {
		return false;
	}
	var letra_NIF = letraNIF(inputStr.substr(0, 8));
	if (inputStr.substr(8, 1).toUpperCase() != letra_NIF) {
		return false;
	}
	return true;
}

//comprueba si tiene el formato de un NIF: 8 cifras numéricas y la letra correspondiente al NIF
//o si es un NIE con su letra correspondiente
function isNIE(inputStr) {
	//alert('En esta 2');
	if (isEmpty(inputStr) || inputStr.length != 9) {
		return false;
	}
	//alert(inputStr);
	// Si es un NIE hay mirar la letra
	if (/^[MmXxYyZz]\d{7}[A-Za-z]$/.test(inputStr)) {
		//alert('correcto');
		return verificaFormato(inputStr, /^[MmXxYyZz]\d{7}[A-Za-z]$/);
		/*alert(inputStr.substr(1, 7)+'-'+inputStr.substr(8, 1))
		var letra_NIE = letraNIF(inputStr.substr(1, 7));
		if(inputStr.substr(8, 1).toUpperCase() != letra_NIE) {
		    return false;
		}*/
	}
	return true;
}

// comprueba si tiene el formato de un NIF: 8 cifras numéricas y la letra correspondiente al NIF
// o si es un NIE con su letra correspondiente
function isNIF_NIE(inputStr) {
	if (isEmpty(inputStr) || inputStr.length != 9) {
		return false;
	}
	var letra_NIE = null;
	letra_NIF = null;

	// Si es un NIE hay mirar la letra
	if (!/^\[Xx]d{7}[A-Za-z]$/.test(inputStr)) {
		// alert(inputStr.substr(1, 7)+'-'+inputStr.substr(8, 1))

		letra_NIE = letraNIF(inputStr.substr(1, 7));
		if (inputStr.substr(8, 1).toUpperCase() != letra_NIE) {
			return false;
		}
	} else {
		// Comprobar formato NIF
		if (!/^\d{8}[A-Za-z]$/.test(inputStr)) {
			return false;
		}
		letra_NIF = letraNIF(inputStr.substr(0, 8));
		if (inputStr.substr(8, 1).toUpperCase() != letra_NIF) {
			return false;
		}
	}
	return true;
}

function establecerMensajeError(mensaje, enfasis) {
	$('[id=error_login]').css("display", "block");
	$('[id=error_login]').append(
			'<ul class="message" id="messages"><li class="errormsg">'
					+ (enfasis === true ? '<strong> ATENCIÓN:</strong> ' : '')
					+ mensaje + ' </li></ul>');
}

function comprobacionIdentificador() {
	try {
		var er_otros = /^[^ ,<>%\'¿?]+$/,
			identificador = $(inputUserLogin).val();

		if (isEmpty(identificador)) {
			$(inputUserLogin).focus();
			establecerMensajeError('Debe introducir el identificador del usuario');
			return false;
		} else if (!er_otros.test(identificador)) {
			$(inputUserLogin).focus();
			establecerMensajeError('Carácter no válido en el documento; no puede introducir espacios en blanco, comas ni los caracteres < > % ¿ ?.');
			return false;
		} else if (identificador.substr(0, 1).toUpperCase() == 'U') { // Asumimos que es un NIU
			if (!isNIU(identificador)) {
				establecerMensajeError('El NIU introducido no es correcto.');
				$(inputUserLogin).focus();
				return false;
			}
			return true;
		} else if (identificador.substr(0, 1).toUpperCase() == 'X'
				|| identificador.substr(0, 1).toUpperCase() == 'Y'
				|| identificador.substr(0, 1).toUpperCase() == 'Z'
				|| identificador.substr(0, 1).toUpperCase() == 'M') { // Asumimos que es un NIE
			if (!isNIE(identificador)) {
				establecerMensajeError('El NIE introducido no es correcto.');
				$(inputUserLogin).focus();
				return false;
			}
			return true;
		} else if (!isNIF(identificador)) { // Asumimos que viene un NIF
			establecerMensajeError('El DNI/NIF introducido no es correcto, recuerde que debe introducir los 8 dígitos seguidos de la letra, Ej: 12345678A');
			$(inputUserLogin).focus();
			return false;
		}
		return true;
	} catch (e) {
		establecerMensajeError('Error en el script: ' + e.name + ". Message: "
				+ e.message);
		return false;
	}
}

function comprobacionPasaporte() {
	try {
		var identificador = $(inputUserLogin).val();

		if (isEmpty(identificador)
				|| identificador == 'Indique su identificador') {
			$(inputUserLogin).focus();
			establecerMensajeError('Debe introducir un pasaporte');
			return false;
		}
		return true;
	} catch (e) {
		establecerMensajeError('Error en el script: ' + e.name + ". Message: "
				+ e.message);
		return false;
	}
}

function comprobandoPassword() {
	try {
		if (isEmpty($(inputPasswordLogin).val())) {
			establecerMensajeError('Debe introducir su contraseña de acceso.',
					true);
			$(inputPasswordLogin).focus();
			return false;
		}
		return true;
	} catch (e) {
		establecerMensajeError('Error en el script: ' + e.name + ". Message: "
				+ e.message);
	}
	return false;
}

function comprobandoCorreo() {
	try {
		if (isEmpty($(inputUserLogin).val())) {
			establecerMensajeError('Debe introducir su correo de acceso.', true);
			$(inputUserLogin).focus();
			return false;
		} else {
			$(inputUserLogin).val(procesarCorreo($(inputUserLogin).val()));
			return true;
		}
	} catch (e) {
		establecerMensajeError('Error en el script: ' + e.name + ". Mensaje: "
				+ e.message);
	}
	return false;
}

function borrarMensajeError() {
	$('[id=error_login]').children().remove();
	$('[id=error_login]').css("display", "none");
	$('[id=messages]').css("display", "none");
}

function activarCorreo() {
	$(inputUserLogin).val(type_mail_text);
	$(inputUserLogin).removeClass('texto_blanco');
	$(inputUserLogin).focus();
	$(inputUserLogin).select();
}

function activarNIF() {
	$(inputUserLogin).val('Indique su Identificador');
	$(inputUserLogin).removeClass('texto_blanco');
	$(inputUserLogin).focus();
	$(inputUserLogin).select();
}

/**
 * Envia el PIN introducido por el usuario al APPLET SESTERTIUM para comprobar su validez
 */
function sendPinCard() {
	// alert('Entra en sendPin');
	inicio_ajax();
	borrarMensajeError();
	var resultado = false, pin_element = $('[id$=pin]'), pin = $(pin_element)
			.val(), cad = null;

	$(pin_element).val(''); // limpiamos la contraseña
	// alert('PIN: ' + pin);
	if (pin.length == 4) {
		cad = document.getElementById('AppletSestertium').entrar(pin, null);
		if (cad !== "") {
			establecerMensajeError(cad, true);
		}
		resultado = true;
	} else {
		establecerMensajeError('El PIN debe tener 4 digitos.', false);
		cancelarSubmit = true;
	}
	if (resultado === false) {
		fin_ajax();
		cancelarSubmit = true;
	}
	return resultado;
}

function autenticacionCertificado(retoXmlFuenteId, retoXmlFirmadoId,
		errorFirmaId) {
	inicio_ajax();
	var retofuente = $("'#" + retoXmlFuenteId + "'"), retofirmado = $("'#"
			+ retoXmlFirmadoId + "'"), error = $("'#" + errorFirmaId + "'");
	$(error).val("0");
	// alert("RetoXmlFuente: " + $(retofuente).val());
	$(retofirmado).val($(retofuente).val());

	// alert("RetoXmlFirmado - ID: " + $(retofirmado).attr('id') + "\n" + "RetoXmlFirmado - antes: " +
	// $(retofirmado).val());
	$(error).val(firmaAvanzadaAutenticacion($(retofirmado).attr('id')));
	// alert("RetoXmlFirmado - despues: " + $(retofirmado).val());

	if ($(error).val().length <= 3) { // Fallo en el proceso
		// alert("ErrorFirma: " + $(error).val());
		$(retofirmado).val(""); // Restauramos el reto firmado
	} else {
		$(error).val("0");
	}
}

function colocarFoco() {
	var elemento;
	switch ($(selectTipoLogin).val()) {
	case 'CARD':
		elemento = 'pin';
		break;
	case 'CERTIFICATE':
		elemento = 'certificateButton';
		break;
	case 'NIF':
	case 'RADIUS':
	case 'PASAPORTE':
		elemento = 'username';
		break;
	default: // SSO
		elemento = 'ssoButton';
		break;
	}
	// alert("colocarFoco: " + elemento);
	establecerFocoElemento(elemento);
}

function ligarEventosCampos() {
	// alert("ligarEventosCampos");

	inputUserLogin = $('input.user-login');
	inputPasswordLogin = $('input.password-login');
	selectTipoLogin = $('select.tipo-login');
	
	colocarFoco();

	if ($(selectTipoLogin).val() == 'RADIUS') {
		activarCorreo();
	}
	else if ($(selectTipoLogin).val() == 'NIF'
			|| $(selectTipoLogin).val() == 'PASAPORTE') {
		activarNIF();
	}

	$(inputUserLogin).click(
			function() {
				if (($(this).val() == 'Indique su e-mail')
						|| ($(this).val() == 'Indique su Identificador')) {
					$(this).val('');
				}
			});

	$(inputUserLogin).change(function() {
		if ($(selectTipoLogin).val() == 'RADIUS') {
			$(this).val(procesarCorreo($(this).val()));
		}
	});
}

/**
 * Activacion de JQuery: - Chequeamos en los eventos onblur y change de componente type=file de HTML - Tambien en el
 * evento submit del formulario
 * 
 */
$(document).ready(function() {
	//alert('Pillando jQuery');

	ligarEventosCampos();

	$('#autenticacionForm').submit(function(evento) {
		// si la función de submit devuelve false, no se envía el formulario

		// alert('SUBMIT');
		if (cancelarSubmit) {
			// alert('cancelando SUBMIT');
			fin_ajax();
			evento.stopPropagation();
			evento.preventDefault();
		} else {
			// Borramos si existe, los errores mostrados
			borrarMensajeError();

			var comprobacion = true, esApplet = false;
			if ($(selectTipoLogin).val() == 'RADIUS') {
				// comprobar que no sean vacios el correo y la contraseña
				comprobacion = comprobandoCorreo();
			} else if ($(selectTipoLogin).val() == 'NIF') {
				comprobacion = comprobacionIdentificador();
			} else if ($(selectTipoLogin).val() == 'PASAPORTE') {
				comprobacion = comprobacionPasaporte();
			} else if ($(selectTipoLogin).val() == 'CARD') {
				$('[id$=pin]').val(''); // limpiamos la contraseña
				comprobacion = true;
			} else {
				// Llamada al applet para acceso a la tarjeta.
				comprobacion = true;
				esApplet = true;
			}
			// alert('comprabacion: ' + comprobacion)
			if (comprobarPassword && (esApplet === false)) {
				comprobacion = (comprobacion && comprobandoPassword());
			}
			if (!comprobacion) {
				fin_ajax();
				evento.stopPropagation();
				evento.preventDefault();
			}
		}
		cancelarSubmit = false;
	});

});


