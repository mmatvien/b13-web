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
#  $('.singlecheck').click ->
#    $(this).parent().siblings().children().removeClass("checked")
#    $(this).parent().siblings().children().find("span").removeClass("icon-ok")
#    $(this).toggleClass("checked")
#    $(this).find("span").toggleClass("icon-ok")
#    return false

  $('.categoryNavShe').click ->
    navigate("she",this.id)

  $('.categoryNavHe').click ->
    navigate("he",this.id)

  $('.categoryNavGirl').click ->
    navigate("girl",this.id)

  $('.categoryNavBoy').click ->
    navigate("boy",this.id)

  $('.categoryNavKid').click ->
    navigate("kid",this.id)

  $('.categoryNavOther').click ->
    navigate("other",this.id)

  $('.brandNav').click ->
    navigateBrand("other",this.id)


  navigateBrand = (section, brand) ->
    qString = window.location.search
    qString = "?brand=" + brand.replace(/&/g,"|")
    window.location.href = qString

  navigate = (section, id) ->
    cat = id.split(":").reverse()[1]+":"+id.split(":").reverse()[0]
    qString = window.location.search
    qString = "/collection/"+section+"/1?cat=" + cat.replace(/&/g,"|")
    window.location.href = qString
