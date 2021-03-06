$ ->
  updateTotal()

  $('.cart-quantity').change ->
    $("#shippingOption").rules("remove", "variationsvalue")
    $('#update-cart').click()

  $('#shippingOption').change ->
    selectedValue = $('#' + this.id + ' option:selected').val()
    if(selectedValue == "")
      $('#shippingPrice').text("")
      $('#insure').prop('checked', false)
      $('#insurancePrice').text("")
      updateTotal()
    else
      $('#shippingPrice').text(selectedValue.split(":")[1])
      if(selectedValue.split(":")[0] == "priority" || selectedValue.split(":")[0] == "mediumBox" || selectedValue.split(":")[0] == "bigBox")
        $('#insuranceOption').css({visibility: 'visible'})
        $('#insure').prop('checked', false)
        $('#insurancePrice').text("")
        updateTotal()
      else
        $('#insuranceOption').css({visibility: 'hidden'})
        $('#insurancePrice').text("")
        $('#insure').prop('checked', false)
        $('#insurancePrice').text("")
        updateTotal()

  $('#insure').change ->
    checked = $('#insure').is(':checked')
    if(checked)
      $('#insurancePrice').text(parseFloat($('#insure').val()).toFixed(2))
      updateTotal()
    else
      $('#insurancePrice').text("")
      $('#insurancePrice').text("")
      updateTotal()

  $('.agree').click ->
    if(this.checked)
      $('#checkout-button').removeClass("hidden")
      $('#checkout-button-disabled').addClass("hidden")
    else
      $('#checkout-button').addClass("hidden")
      $('#checkout-button-disabled').removeClass("hidden")

updateTotal = () ->
  subtotal = $('#subtotal').text()
  shipping =$('#shippingPrice').text()
  insurance = $('#insurancePrice').text()
  if (subtotal == "")
    subtotal = 0
  if (shipping == "")
    shipping = 0
  if (insurance == "")
    insurance = 0
  grandTotal = (parseFloat(subtotal) + parseFloat(shipping) + parseFloat(insurance)).toFixed(2)
  $('#subtotalPriceInput').val(subtotal)
  $('#insurancePriceInput').val(insurance)
  $('#shippingPriceInput').val(shipping)
  $('#grand-total').text(grandTotal)
