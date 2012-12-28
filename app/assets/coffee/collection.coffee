$ ->
  $('#size-filter').val("M")
  $('#size-filter').change ->
    size = $('#size-filter option:selected').text()
    qString = window.location.search
    if(qString != "")
      if(qString.indexOf("size=") > 0)
        qString = qString.substr(0,qString.indexOf("size=")) + "size=" + size
      else
        qString += "&size=" + size
    else
      qString += "?size=" + size
    window.location.href = qString