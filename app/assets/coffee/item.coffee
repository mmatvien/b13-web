$ ->
  $('.custSel').change ->
    cunstructOptions(this.id)


cunstructOptions = (currentSelection) ->
  selectedName = $('#' + jqSelector(currentSelection) + '_n').val()
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
  $("#" + jqSelector(selectorId) + " option[value='" + optionValue + "']").remove()

addOption = (selectorId, optionValue, optionText) ->
  $('#' + jqSelector(selectorId)).append($('<option>', { value: optionValue, text: optionText }))

revalidateSelector = (currentS, selectorName, selectedName, selectedValue) ->
  optionsForThisSelector = validateOtherSelections(selectorName, selectedName, selectedValue)
  removeAllOptions(currentS.attr('id'))
  optionsForThisSelector.split(",").forEach((x) ->
    if (trim(x) != "")

      addOption(currentS.attr('id'), trim(x.split("^")[0]), trim(x.split("^")[1]))
  )
  selectOnlyOption(currentS.attr('id'))


selectOnlyOption = (selectorId) ->
  optionArray = $('#' + jqSelector(selectorId) + ' option')
  if (optionArray.length == 2)
    optionArray.each((n) ->
      if(n == optionArray.length - 1)
        $(this).attr("selected", "selected")
        $('#' + jqSelector(selectorId)).val($(this).val())
    )


removeAllOptions = (selectorId) ->
  $('#' + jqSelector(selectorId) + ' option').each(->
    if ($(this).val() != "---")
      removeOption(selectorId, $(this).val())
  )

# getSelectorOptions(currentSelection)


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
