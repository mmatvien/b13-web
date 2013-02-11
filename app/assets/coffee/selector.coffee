$ ->
#  /* Multi select - allow multiple selections */
#  /* Allow click without closing menu */
#  /* Toggle checked state and icon */
  $('.multicheck').click ->
    $(this).toggleClass("checked")
    $(this).find("span").toggleClass("icon-ok")
    return false

#/* Single Select - allow only one selection */
#/* Toggle checked state and icon and also remove any other selections */
  $('.singlecheck').click ->
    $(this).parent().siblings().children().removeClass("checked")
    $(this).parent().siblings().children().find("span").removeClass("icon-ok")
    $(this).toggleClass("checked")
    $(this).find("span").toggleClass("icon-ok")
    return false

