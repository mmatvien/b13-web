
$ ->

  constructV()
  $('#itemForm').validate(
    onfocusout: false
    onkeyup: false
    onclick: false
    rules:
      "variations[0].value":
        variationsvalue: true
      "variations[1].value":
        variationsvalue: true
      "variations[2].value":
        variationsvalue: true
      "variations[3].value":
        variationsvalue: true
      "variations[4].value":
        variationsvalue: true
      "variations[5].value":
        variationsvalue: true
      "variations[6].value":
        variationsvalue: true
      "variations[7].value":
        variationsvalue: true
      "variations[8].value":
        variationsvalue: true
  )

  $('#cartform').validate(
    onfocusout: false
    onkeyup: false
    onclick: false
    rules:
      "shippingOption":
        variationsvalue: true
      "agree":
        terms: true
  )


constructV = () ->
  $.validator.addMethod 'variationsvalue',  (value, element) ->
    if(value == "---")
      return false
    else
      return true
  , "заполните"

  $.validator.addMethod 'terms',  (value, element) ->
    return $('#agree:checked')
  , "согласитесь с условиями заказа"
