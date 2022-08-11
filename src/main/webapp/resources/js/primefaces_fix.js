function updateToggles(toggler) {
	$(PF(toggler).jqId).find("li").find('> .ui-chkbox > .ui-chkbox-box').each(
		function() {
			var chkbox = $(this);
			if (chkbox.hasClass('ui-state-active')) {
				PF(toggler).check(chkbox);
			} else {
				PF(toggler).uncheck(chkbox);
			}
		}
	);
}