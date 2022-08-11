function checkErrors() {
    $(".ui-state-error").each(function() {
        $(this).addClass("is-invalid");
    });
}