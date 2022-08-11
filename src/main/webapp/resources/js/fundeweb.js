// VARIABLES QUE ESTABLECE SI SE UTILIZAN O NO LAS FUNCIONALIDADES:

var userAgent = navigator.userAgent.toLowerCase(),

bloqueoFundeweb,

// Bloquear aplicacion hasta que no se haya cargado la página
bloqueoBotonesLinks = true,

// Mostrar mensaje de aviso en caso de que haya sido modificado el formulario
avisoCambioForm = true,

// Variable que controla el bloque del teclado cuando aparece la ventana de
// bloqueo
bloqueoTeclado = false,

// Varible que controla el normal funcionamiento del boton BACK del navegador
// true: se bloquea el boton BACK del navegador
// false: comportamiento por defecto del navegador
bloqueoBotonBackNavegador = true,

// Indica si el formulario actual ha cambiado o no
haCambiado = false,

// Variable que contiene la pagina a la que se quiere ir
pagina = '#',

// Variable para indicar que ya se hizo click en la opcion de menu (es necesaria
// ya que salta dos veces)
menuClicked = false,

// Variable para indicar que se hizo click en la opcion de menu con
// target="_blank"
menuClickedTargetBlank = false;

/*
 * ////////////////// MEJORA DE JQUERY PARA DETEXTAR CORRECTAMENTE LOS
 * NAVEGADORES - PARA jQUERY 1.3.2 ///////
 */

$.browser = {
	version : (userAgent
			.match(/.+(?:firefox|chrome|version|msie|it|ra|ie|me)[\/: ]([\d.]+)/) || [])[1],
	chrome : /chrome/.test(userAgent),
	safari : /webkit/.test(userAgent) && !/chrome/.test(userAgent),
	opera : /opera/.test(userAgent),
	msie : /msie/.test(userAgent) && !/opera/.test(userAgent),
	mozilla : (/mozilla/.test(userAgent) && !/(compatible|webkit)/
			.test(userAgent))
			|| /firefox/.test(userAgent)
};

/*
 * ////////////////// FIN MEJORA DE JQUERY PARA DETEXTAR CORRECTAMENTE LOS
 * NAVEGADORES - PARA jQUERY 1.3.2 ///////
 */

/*
 * /////// SOLUCION PROBLEMA DEL ONCHANGE EN RADIO Y CHECKBOXES EN IE
 * ////////////////
 */

// IE espera hasta que se produzca otro evento para enviar el evento 'change' de
// los radios and checkboxes
// Esta funcion enlaza (bind) un disparador (trigger) para el evento 'click',
// que lanza en change
// Para que funcione en IE como en el resto de navegadores
function bindInternetExplorerEvents() {
	$("input[type='checkbox'], input[type='radio']").click(function() {
		$(this).trigger('change');
		$(this).trigger('focus');
	});
}

/* /////// FIN SOLUCION PROBLEMA ONCHANGE RADIO EN IE //////////////// */

/* /////// SOLUCION PROBLEMA COMBOS QUE SE CORTAN EN IE7 - IE8 //////////////// */
function internetExplorerSelectCutSolution() {
	if ($.browser.msie
			&& ($.browser.version.substring(0, 2) == "7." || $.browser.version
					.substring(0, 2) == "8.")) {

		$("select").each(function() {
			$(this).data("origWidth", $(this).outerWidth()); // IE 8 can haz
																// padding
		}).mousedown(function() {
			$(this).css("width", "auto");
		}).bind("blur change", function() {
			$(this).css("width", $(this).data("origWidth"));
		});
	}
}
/*
 * /////// FIN - SOLUCION PROBLEMA DEL ONCHANGE EN RADIO Y CHECKBOXES EN IE
 * ////////////////
 */

/* /////// OTRAS FUNCIONES //////////////// */

function establecerFocoElemento(elemento) {
	$("[id$='" + elemento + "']").focus();
}

function establecerFocoElementoIdCliente(elemento) {
	$("[id='" + elemento + "']").focus();
}

/* /////// FIN SOLUCION PROBLEMA ONCHANGE RADIO EN IE //////////////// */

function inicio_ajax() {
	bloquearPantalla();
}

function fin_ajax() {
	desbloquearPantalla();
	religarJQuery();
}

function bloquearPantalla() {
	bloqueoTeclado = true;
	$(bloqueoFundeweb).css("height", $(document).height());

	if ($.browser.msie) {
		// Para que se vea bien la pantalla de bloqueo en el login
		$(bloqueoFundeweb).css("width", $(document).width() + 1000);
		$(bloqueoFundeweb).css("left", -1000);
	}

	$(bloqueoFundeweb).fadeIn(100);
}

function desbloquearPantalla() {
	$(bloqueoFundeweb).fadeOut(100);
	bloqueoTeclado = false;
}
// ////////// FIN PANEL DE BLOQUEO //////////////////////

// //////// PANEL BLOQUEO BOTONES Y LINKS ///////////////////
function bindLoadingBlockEvents() {
	$(".bloqueoFundeweb").click(function() {
		// si no esta deshabilitado, tiene href y no se abre en una ventana
		// nueva
		if (!$(this).is(":disabled")) {
			bloquearPantalla();
		}
	});
	// Al pulsar cualquier tecla si el panel esta bloqueado
	// no tendrá ningún efecto.
	$(document).keydown(function(evento) {
		if (bloqueoTeclado) {
			evento.preventDefault();
			evento.stopPropagation();
		}
	});
}
// //////// FIN PANEL BLOQUEO BOTONES Y LINKS ///////////////////

// ////////// PANEL AVISO HA CAMBIADO EL FORMULARIO //////////////////////

/*
 * Establace a true variable cambio de un formulario
 */
function setHaCambiado() {
	haCambiado = true;
}
/**
 * Establece eventos onchange para que quede registrado que ha habido un cambio
 * en el formulario
 * 
 * @return void
 */
function bindChangedInputEvents() {
	$("form.editFundeweb select").change(setHaCambiado);
	$("form.editFundeweb input").change(setHaCambiado);
	$("form.editFundeweb textarea").change(setHaCambiado);
	$("form.editFundeweb checkbox").change(setHaCambiado);
	$("form.editFundeweb radio").change(setHaCambiado);
}

/* //////////// FIN PANEL AVISO HA CAMBIADO EL FORMULARIO ////////////////////// */

/* /////// DESHABILITAR BOTON BACK NAVEGADOR //////////////// */
$(window).load(function() {
	// Deshabilitamos la funcionalidad de navegar hacia atras del navegador.
	if (bloqueoBotonBackNavegador && (window.history.forward(1) !== null)) {
		window.history.forward(1);
	}
});
/* /////// FIN DESHABILITAR BOTON BACK NAVEGADOR //////////////// */

/* /////////// FUNCION PARA CENTRAR DIVS ///////////////// */

$.fn.center = function(absolute) {
	return this.each(function() {
		var t = $(this);

		t.css({
			position : absolute ? 'absolute' : 'fixed',
			left : '45%',
			top : '45%'// ,
		// zIndex: '100'
		});

		if (absolute) {
			t.css({
				marginTop : parseInt(t.css('marginTop'), 10)
						+ $(window).scrollTop(),
				marginLeft : parseInt(t.css('marginLeft'), 10)
						+ $(window).scrollLeft()
			});
		}
	});
};

/* /////////// FIN FUNCION PARA CENTRAR DIVS /////////////////////////////////// */

/*
 * /////////// FUNCION PARA ABRIR INFORMES PDF EN OTRA PESTAÑA/VENTANA
 * ///////////
 */

function verInformePdfGenerado(elemento) {
	var boton = $("[id$='" + elemento + "']");
	// alert(elemento);
	if ($(boton)) {
		if ($(boton).is(':a')) {
			$(boton)[0].click();
		}
		// alert(elemento + "-click");
		$(boton).click();
	}
}

/* /////////// FIN FUNCION PARA CENTRAR DIVS ////////////////////////////////// */

/* //////// GESTION DE MENU Y DEL AVISO DE CAMBIO DE PAGINA /////// */
function abrirModalPanelAvisoCambioDePagina(elemento, evento) {
	pagina = $(elemento).attr('href');
	$("#confirmationLostChangePanel").attr('component').show();
	evento.stopPropagation();
	evento.preventDefault();
	fin_ajax();
}
/* ////// FIN GESTION DE MENU Y DEL AVISO DE CAMBIO DE PAGINA ///// */

/* /////// JQuery CON AJAX 4 JAVA //////////////// */
// al pasar un evento ajax4 se desligan todos lo eventos ligados con $
// que esten en los elementos repintados, por lo que hay que volverlos a ligar
function religarJQuery() {
	// SOLUCION PROBLEMA DEL ONCHANGE EN RADIO Y CHECKBOXES EN IE
	if ($.browser.msie) {
		bindInternetExplorerEvents();
		internetExplorerSelectCutSolution();
	}
	if (bloqueoBotonesLinks) {
		bindLoadingBlockEvents();
	}
	if (avisoCambioForm) {
		bindChangedInputEvents();
	}
}

/* ////// FIN JQuery CON AJAX 4 JAVA //////////////// */

$(document).ready(function() {
	bloqueoFundeweb = $('div[id$="panelBloqueoFundeweb"]');
	haCambiado = false;
	// Ligamos todos los eventos JQuery que son permanentes para las
	// aplicaciones
	religarJQuery();
});

function activaEdicionLinea() {
	$('.ui-datatable-data tr').first().find('span.ui-icon-pencil').each(
			function() {
				jQuery(this).click();
			});
}
