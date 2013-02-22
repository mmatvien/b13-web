$ ->

  jQuery.validator.addMethod("cardNumber", testValidate(), "заполните правильно")

  $("#payment-form").validate({
    submitHandler: submitA,
    rules: {
      "card-cvc": {
      cardCVC: true,
      equired: true
      },
      "card-number": {
        cardNumber: true,
        required: true
      }
    }
  })

testValidate = () ->
  alert("validation")