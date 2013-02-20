$ ->
  $('.navZ').click ->
    $('.active').removeClass("active")
    $(this).addClass("active")
    x = $('.modal-body').offset().top
    y =  $("#"+this.id+"D").offset().top
    $('.modal-body').animate({scrollTop: y-x}, 1000);
