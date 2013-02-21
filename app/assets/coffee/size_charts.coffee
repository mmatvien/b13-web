$ ->
  $('.navZ').click ->
    $('.active').removeClass("active")
    $(this).addClass("active")
    x = $('#sheD').offset().top
    y =  $("#"+this.id+"D").offset().top
    $('.modal-body').animate({scrollTop: y-x}, 1000);
