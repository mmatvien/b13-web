
# on ready
$ ->
  $('.orderRow').click ->
    detailRow = $('#items_' + this.id)
    if (detailRow.hasClass("orderItemsHidden"))
      detailRow.removeClass("orderItemsHidden")
      detailRow.addClass("orderItemsVisible")
    else
      detailRow.removeClass("orderItemsVisible")
      detailRow.addClass("orderItemsHidden")

  $('.btn').click ->
    id = this.id
    bootbox.confirm("Are you sure?", (result) ->
      if(result)
        linkId = id.replace("button", "")
        link = $('#link' + linkId)
        link[0].click()
    )
