$ ->
  $('#subscribeForm').submit ->
    $.post(
      $(this).attr('action'), {
        email: $('#email-input').val()
      }, ->
        bootbox.alert("спасибо за подписку")
    )
    return false

  $('#questionForm').submit ->
    $.post(
      $(this).attr('action'), {
        email: $('#email-input-questions').val(),
        question: $('#questionF').val()
      }, ->
      $('#questionsModal').modal('hide')
      bootbox.alert("спасибо за вопрос")
    )
    return false