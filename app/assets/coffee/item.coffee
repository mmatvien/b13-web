$ ->
  $('.custSel').each((x) ->
    sel = 'variations[' + x + '].value'
    cunstructOptions(sel)
  )

  $('.custSel').change ->
    cunstructOptions(this.id)
    selectedValue = $('#' + jqSelector(this.id) + ' option:selected').val()
    $('#' + selectedValue.replace(/\s/g, "_")).click()

  $('.product-photo-thumb').click ->
    if(!selectPictureOption(this.id))
      alert("option is missing")

  $(".custSel").validate({
    debug: true
  })


selectPictureOption = (pictureId) ->
  optionExists = false
  $('.custSel').each((nb) ->
    $('.custSel').each((n) ->
      selector = 'variations[' + n + '].value'
      optionArray = $('#' + jqSelector(selector) + ' option')
      optionArray.each(->
        if($(this).val().replace(/\s/g, "_") == pictureId)
          optionExists = true
          $(this).attr("selected", "selected")
          cunstructOptions(selector)
      )
    )
    if(!optionExists)
      selectorA = 'variations[' + nb + '].value'
      $('#' + jqSelector(selectorA)).val("---")
      cunstructOptions(selectorA)
  )



cunstructOptions = (currentSelection) ->
  selectedName = $("label[for='" + currentSelection + "']").text()
  selectedValue = $('#' + jqSelector(currentSelection) + ' option:selected').val()
  # go through all selectors
  $('.custSel').each((x) ->
    selector = jqSelector('variations[' + x + '].value')
    currentS = $('#' + selector)
    if (currentS.attr('id') != currentSelection)
      selectorName =  $("#" + jqSelector(currentS.attr('id')) + "_n").val()
      revalidateSelector(currentS, selectorName, selectedName, selectedValue)
  )

removeOption = (selectorId, optionValue) ->
  $("#" + jqSelector(selectorId) + " option[value='" + jqSelector(optionValue) + "']").remove()

addOption = (selectorId, optionValue, optionText, selected) ->
  $('#' + jqSelector(selectorId)).append($('<option>', { value: optionValue, text: optionText }))
  if(optionValue == selected)
    $('#' + jqSelector(selectorId)).val(selected)

revalidateSelector = (currentS, selectorName, selectedName, selectedValue) ->
  optionsForThisSelector = validateOtherSelections(selectorName, selectedName, selectedValue)
  selected = $('#' + jqSelector(currentS.attr('id'))).val()
  removeAllOptions(currentS.attr('id'))
  optionsForThisSelector.split(",").forEach((x) ->
    if (trim(x) != "")
      addOption(currentS.attr('id'), trim(x.split("^")[0]), trim(x.split("^")[1]), selected)
  )
  selectOnlyOption(currentS.attr('id'))


selectOnlyOption = (selectorId) ->
  optionArray = $('#' + jqSelector(selectorId) + ' option')
  if (optionArray.length == 2)
    optionArray.each((n) ->
      if(n == optionArray.length - 1)
        $(this).attr("selected", "selected")
        $('#' + jqSelector(selectorId)).val($(this).val())
        $('#' + $(this).val().replace(/\s/g, "_")).click()
    )


removeAllOptions = (selectorId) ->
  $('#' + jqSelector(selectorId) + ' option').each(->
    if ($(this).val() != "---")
      removeOption(selectorId, $(this).val())
  )


validateOtherSelections = (name, sname, svalue) ->
  dataX = $('#variationDataX').html()
  ava = dataX.split("~")
  addIteration = false
  iterationResult = ""
  ava.forEach((x) ->
    u = x.split(",")
    iterationValue = ""
    u.forEach((n) ->
      line = trim(n)
      if (line != "")
        temp = line.split(":")
        nameA = trim(temp[0])
        valueATemp = trim(temp[1])
        valueA = trim(valueATemp.split("^")[0])
        if (name == nameA)
          iterationValue = valueATemp
        if(nameA == sname && (valueA == svalue || svalue == "---"))
          addIteration = true
    )
    if (addIteration)
      if(!optionContains(iterationResult, iterationValue))
        iterationResult = iterationResult + "," + iterationValue
      addIteration = false
  )
  iterationResult

optionContains = (existing, newOption) ->
  res = false
  existing.split(",").forEach((x) ->
    if(x == newOption)
      res = true
  )
  res

getSelectorOptions = (selector) ->
  $('#' + jqSelector(selector) + ' option').each(->
    #       add $(this).val() to your list
  )

trim = (str) ->
  if(str != "")
    str.replace /^\s+|\s+$/g, ""
  else
    str

jqSelector = (str) ->
  str.replace(/([;&,\.\+\*\~':"\!\^#$%@\[\]\(\)=>\|])/g, '\\$1')

