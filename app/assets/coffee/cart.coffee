$ ->
  $('#shippingOption').change ->
    selectedValue = $('#' + this.id + ' option:selected').val()
    if(selectedValue == "")
      $('#shippingPrice').text("")
    else
      $('#shippingPrice').text(selectedValue.split(":")[1])
      if(selectedValue.split(":")[0] == "priority")
        $('#insuranceOption').css({visibility: 'visible'})
      else
        $('#insuranceOption').css({visibility: 'hidden'})

  $('#insuranceOption').change ->
    selectedValue = $('#' + this.id + ' option:selected').val()
    if(selectedValue == "")
      $('#insurancePrice').text("")
    else
      $('#insurancePrice').text(selectedValue)
      