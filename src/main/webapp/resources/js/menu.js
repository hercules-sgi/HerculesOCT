$(document).ready(function()
{
    
    $('#sidebar-nav .dropdown-toggle').click(function (e) {
        e.preventDefault();
        
        var $item = $(this).parent();

        if (!$item.hasClass('open')) {
            $item.parent().find('.open .submenu').slideUp('fast');
            $item.parent().find('.open').toggleClass('open');
        }
        
        $item.toggleClass('open');
        
        if ($item.hasClass('open')) {
            $item.children('.submenu').slideDown('fast');
        } 
        else {
            $item.children('.submenu').slideUp('fast');
        }
    });
    
    $('#colapsarMenu').click(function(event) {
        //$("#sidebar-nav" ).slideUp();
        $('#colapsarMenu').addClass('hidden');
        $("#sidebar-nav" ).removeClass('col-md-2');
        $("#sidebar-nav" ).removeClass('col-sm-2');
        $("#sidebar-nav" ).addClass('col-md-min');
        $("#sidebar-nav" ).addClass('col-sm-min');
        $("#sidebar-nav" ).addClass('collapsed');
        
        
        $("#main-content").removeClass('col-lg-offset-2');
        $("#main-content").removeClass('col-md-10');
        $("#main-content").removeClass('col-md-offset-2');
        $("#main-content").removeClass('col-sm-10');
        $("#main-content").removeClass('col-sm-offset-2');
        
        $("#main-content").addClass('col-md-max');
        $("#main-content").addClass('col-md-offset-min');
        $("#main-content").addClass('col-sm-max');
        $("#main-content").addClass('col-sm-offset-min');
        
        $('#expandirMenu').removeClass('hidden');
    });
    
    $('#expandirMenu').click(function(event) {
        //$("#sidebar-nav" ).slideToggle();
        $('#expandirMenu').addClass('hidden');
        $("#sidebar-nav" ).removeClass('col-md-min');
        $("#sidebar-nav" ).removeClass('col-sm-min');
        $("#sidebar-nav" ).addClass('col-md-2');
        $("#sidebar-nav" ).addClass('col-sm-2');
        $("#sidebar-nav" ).removeClass('collapsed');
        
        $("#main-content").removeClass('col-md-max');
        $("#main-content").removeClass('col-md-offset-min');
        $("#main-content").removeClass('col-sm-max');
        $("#main-content").removeClass('col-sm-offset-min');
        
        $("#main-content").addClass('col-md-10');
        $("#main-content").addClass('col-md-offset-2');
        $("#main-content").addClass('col-sm-10');
        $("#main-content").addClass('col-sm-offset-2');
        
        $('#colapsarMenu').removeClass('hidden');
    });
    
});