$ ->
  dataX = $('#variationDataX').html()
  ava = dataX.split("~")
  ava.forEach( (x) ->
    u = x.split(",")
    u.forEach( (n) ->
      line = n.replace /^\s+|\s+$/g, ""
      console.log(line)
    )

  )


  $('.optionSelector').change ->
    selected = $('.optionSelector:selected').text()
    console.log(selected)