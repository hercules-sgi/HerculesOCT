$(document).ready(function() {
    
  $('#menu-button').on('click', function() {
    $('.sidebar').toggleClass('sidebar-collapse');
    $('body').toggleClass('mini-sidebar');
  });

  $('.nav-list li:has("ul.collapse") > a[href="#"]').on('click', function() {
      var item = $(this).parent().children('ul.collapse');
      if (!item.hasClass('in')) {
        $(this).find(".fa:last-child").addClass('fa-angle-up');
        $(this).find(".fa:last-child").removeClass('fa-angle-down');
        // compruebo que no haya otra menu abierto
        if ($('.active-menuitem').length > 0) {
            if ($(this).parents('ul.collapse').length === 0) { 
                var submenu = $('.active-menuitem');
                submenu.find('a .fa:last-child').addClass('fa-angle-down');
                submenu.find('a .fa:last-child').removeClass('fa-angle-up');
                submenu.find('ul.collapse').slideUp();
                submenu.find('ul.collapse').removeClass('in');
                submenu.removeClass('active-menuitem');
            }
        }
        $(this).parent().addClass('active-menuitem');
        item.slideDown();
      }
      else {
        item.slideUp();
        $(this).parent().removeClass('active-menuitem');
        $(this).find(".fa:last-child").addClass('fa-angle-down');
        $(this).find(".fa:last-child").removeClass('fa-angle-up');
        
      }
      item.toggleClass('in');
  });

  
  $( "#menu-horizontal" ).click(function() {
      $('body').addClass('menu-horizontal');
      $('body').removeClass('mini-sidebar');
      $('.sidebar').removeClass('sidebar-collapse');
  });
  
  $( "#menu-vertical" ).click(function() {
      $('body').removeClass('menu-horizontal');
  });
  
});