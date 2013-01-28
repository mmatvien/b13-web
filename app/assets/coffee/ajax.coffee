$ ->
  $('#subscribeForm').submit ->
    $.post(
      $(this).attr('action'), {
        email: $('#email-input').val()
      }, ->
        bootbox.alert("спасибо за подписку")
    )
    return false