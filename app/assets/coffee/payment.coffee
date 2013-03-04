$ ->
  if($('#message').text().length > 0)
    bootbox.confirm($('#message').text() + "<br>продолжить оформление оставшихся товаров?<br>", (result) ->
      if(result)
        qString = "cart"
        window.location.href = qString
      else
        window.location.href = "/"
    )
