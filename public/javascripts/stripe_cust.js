/*
 * Copyright (c) 2012.
 * All Right Reserved, http://www.maxiru.com/
 */


// this identifies your website in the createToken call below
Stripe.setPublishableKey('pk_test_7yraNGHTgbJEYmBoeCAFfS0L');

//Stripe.setPublishableKey('pk_live_m1O7SFjZzn4KEGRNK02zH8sB');



$(document).ready(function () {

    function addInputNames() {
        // Not ideal, but jQuery's validate plugin requires fields to have names
        // so we add them at the last possible minute, in case any javascript
        // exceptions have caused other parts of the script to fail.
        $(".card-number").attr("name", "card-number")
        $(".card-cvc").attr("name", "card-cvc")
        $(".card-expiry-year").attr("name", "card-expiry-year")
    }

    function removeInputNames() {
        $(".card-number").removeAttr("name")
        $(".card-cvc").removeAttr("name")
        $(".card-expiry-year").removeAttr("name")
    }

    function submitA(form) {

        // remove the input field names for security
        // we do this *before* anything else which might throw an exception
        removeInputNames(); // THIS IS IMPORTANT!

        // given a valid form, submit the payment details to stripe
        $('.submit-button').attr("disabled", "disabled");
        $('.submit-button').addClass("disabled")


        Stripe.createToken({
            number: $('.card-number').val(),
            cvc: $('.card-cvc').val(),
            exp_month: $('.card-expiry-month').val(),
            exp_year: $('.card-expiry-year').val()
        }, function (status, response) {
            if (response.error) {
                // re-enable the submit button
                $('.submit-button').removeAttr("disabled")
                $('.submit-button').removeClass("disabled")

                // show the error
                $(".payment-errors").html(response.error.message);

                // we add these names back in so we can revalidate properly
                addInputNames();
            } else {
                // token contains id, last4, and card type
                var token = response['id'];
                form = $("#payment-form");
                // insert the stripe token
             //   var input = $("<input name='stripeToken' value='" + token + "' style='display:none;' />");
                form.append("<input type='hidden' name='stripeToken' value='" + token + "'/>");
                // and submit
                form.get(0).submit();
            }
        });

        return false;
    }

    // add custom rules for credit card validating
    jQuery.validator.addMethod("cardNumber", Stripe.validateCardNumber, "Please enter a valid card number");
    jQuery.validator.addMethod("cardCVC", Stripe.validateCVC, "Please enter a valid security code");
    jQuery.validator.addMethod("cardExpiry", function () {
        return Stripe.validateExpiry($(".card-expiry-month").val(),
            $(".card-expiry-year").val())
    }, "Please enter a valid expiration");

    // We use the jQuery validate plugin to validate required params on submit
    $("#payment-form").validate({
        submitHandler: submitA,
        rules: {
            "card-cvc": {
                cardCVC: true,
                required: true
            },
            "card-number": {
                cardNumber: true,
                required: true
            },
            "card-expiry-year": "cardExpiry" // we don't validate month separately
        }
    });

    // adding the input field names is the last step, in case an earlier step errors
    addInputNames();
});