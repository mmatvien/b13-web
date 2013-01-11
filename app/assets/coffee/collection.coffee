$ ->
  $('#size-filter').val("M")
  $('#size-filter').change ->
    size = $('#size-filter option:selected').text()
    qString = window.location.search
    if(qString != "")
      if(qString.indexOf("size=") > 0)
        qString = qString.substr(0, qString.indexOf("size=")) + "size=" + size
      else
        qString += "&size=" + size
    else
      qString += "?size=" + size
    window.location.href = qString

window.Pager = class Pager
  goToPage: (num) ->
    qString = window.location.search
    cut = window.location.pathname.split("/")
    delete cut[cut.length - 1]
    base = cut.reduce (x, y) -> x + "/" + y
    window.location.replace(base + "/" + num + qString)



