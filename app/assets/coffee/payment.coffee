$ ->
  if($('#message').text().length > 0)
    bootbox.confirm($('#message').text() + "продолжить оформление оставшихся товаров?", (result) ->
      if(result)
        qString = "cart"
        window.location.href = qString
      else
        window.location.href = "/"
    )
