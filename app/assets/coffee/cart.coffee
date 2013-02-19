$ ->
  $('#shippingOption').change ->
    selectedValue = $('#' + this.id + ' option:selected').val()
    if(selectedValue == "")
      $('#shippingPrice').text("")
    else
      $('#shippingPrice').text(selectedValue.split(":")[1])
      if(selectedValue.split(":")[0] == "priority" || selectedValue.split(":")[0] == "mediumBox" || selectedValue.split(":")[0] == "bigBox")
        $('#insuranceOption').css({visibility: 'visible'})
        $('#insure').prop('checked', false)
      else
        $('#insuranceOption').css({visibility: 'hidden'})
        $('#insurancePrice').text("")

  $('#insure').change ->
    checked = $('#insure').is(':checked')
    if(checked)
      $('#insurancePrice').text(parseFloat($('#insure').val()).toFixed(2))
    else
      $('#insurancePrice').text("")
      $('#insurancePrice').text("")

