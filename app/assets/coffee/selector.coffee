$ ->
  #  /* Multi select - allow multiple selections */
  #  /* Allow click without closing menu */
  #  /* Toggle checked state and icon */
  $('.multicheck').click ->
    $(this).toggleClass("checked")
    $(this).find("span").toggleClass("icon-ok")
    return false

  getParameterByName = (name) ->
    name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]")
    regexS = "[\\?&]" + name + "=([^&#]*)"
    regex = new RegExp(regexS)
    results = regex.exec(window.location.search)
    if(results == null)
      return ""
    else
      return decodeURIComponent(results[1].replace(/\+/g, " "))

  #/* Single Select - allow only one selection */
  #/* Toggle checked state and icon and also remove any other selections */
  #  $('.singlecheck').click ->
  #    $(this).parent().siblings().children().removeClass("checked")
  #    $(this).parent().siblings().children().find("span").removeClass("icon-ok")
  #    $(this).toggleClass("checked")
  #    $(this).find("span").toggleClass("icon-ok")
  #    return false
  urlString = window.location.href
  if(urlString.indexOf("boy") > 0)
    $('#dropBoy').addClass("active")
  if(urlString.indexOf("girl") > 0)
    $('#dropGirl').addClass("active")
  if(urlString.indexOf("/he") > 0)
    $('#dropHe').addClass("active")

  if(urlString.indexOf("she") > 0)
    $('#dropShe').addClass("active")



  if(urlString.indexOf("other") > 0)
    $('#dropOther').addClass("active")
  if(urlString.indexOf("kid") > 0)
    $('#dropKid').addClass("active")

  if(urlString.indexOf("brand=") > 0)
    $('#dropBrand').addClass("active")

  $('.categoryNavShe').click ->
    navigate("she", this.id)

  $('.categoryNavHe').click ->
    navigate("he", this.id)

  $('.categoryNavGirl').click ->
    navigate("girl", this.id)

  $('.categoryNavBoy').click ->
    navigate("boy", this.id)

  $('.categoryNavKid').click ->
    navigate("kid", this.id)

  $('.categoryNavOther').click ->
    navigate("other", this.id)

  $('.brandNav').click ->
    navigateBrand("other", this.id)


  navigateBrand = (section, brand) ->
    qString = "?brand=" + brand.replace(/&/g, "|")
    window.location.href = qString

  navigate = (section, id) ->
    cat = id.split(":").reverse()[1] + ":" + id.split(":").reverse()[0]
    qString = window.location.search
    qString = "/collection/" + section + "/1?cat=" + cat.replace(/&/g, "|")
    window.location.href = qString



  x = getParameterByName("cat")
  cs = x.replace(/\|/g,"_").replace(/\s/g,"").replace(/:/g,"_").replace(/'/g,"_").replace(/\(/g,"_").replace(/\)/g,"_")
  $('.'+cs).addClass("active")