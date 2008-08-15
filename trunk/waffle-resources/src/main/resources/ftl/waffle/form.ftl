<#ftl strip_whitespace=true>
<#--
 * A collection of FTL (Freemarker Template Language) functions and macros to use in HTML forms 
 *
 * @author Mauro Talevi
 -->

<#--
 * Determines if list contains the item based on the string representation of the items
 *
 * @param list the list to search for the item
 * @param item the item to search for in the list
 * @return true if item is found in the list, false otherwise
-->
<#function contains list item>
    <#list list as next>
    <#if next?string == item?string><#return true></#if>
    </#list>
    <#return false>
</#function>

<#--
 * Joins list values.  
 *
 * NOTE: The numeric values are converted using the c built-in to ensure that they are not formatted according to locales.
 *
 * @param values the values to join
 * @param separator the separator to join list with
 * @return A string with joined values
 -->
<#function join values separator>
    <#assign result = ''>
    <#list values as value><#if value?is_number><#assign result=result+value?c><#else><#assign result=result+value></#if><#if value_has_next><#assign result=result+separator></#if></#list>
    <#return result>
</#function>

<#--
 * Converts a sequence of elements to a sequence of values, ie obtained from the value fields
 *
 * @param elements the sequence of elements to convert
 * @param valueField the name of the value field in the input element (defaults to "value")
 * @return A sequence of values
 -->
<#function asValues elements valueField="value">
    <#assign result = []>
    <#list elements as element>
        <#assign value='${element["${valueField}"]!element}'>    
        <#assign result=result+[value]>
    </#list>
    <#return result>
</#function>

<#--
 * Converts a sequence of elements to a sequence of nameable values, ie hashes with name and value fields
 *
 * @param elements the sequence of elements to convert
 * @param valueField the name of the value field in the input element (defaults to "value")
 * @param nameField the name of the name field in the input element (defaults to "name") 
 * @return A sequence of nameables hashes
 -->
<#function asNameableValues elements valueField="value" nameField="name">
    <#assign result = []>
    <#list elements as element>
        <#assign value='${element["${valueField}"]!element}'>    
        <#assign name='${element["${nameField}"]!element}'>
        <#assign nameable = {"value":"${value}", "name":"${name}"}>
        <#assign result=result+[nameable]>
    </#list>
    <#return result>
</#function>

<#--
 * Shows values as CSV
 *
 * @param values the sequence of values
 -->
<#macro asCSV values>
    <#assign csv=join(values,",")>${csv}
</#macro>

<#--
 * Writes a checkbox input element with a given value
 *
 * @param field the name of the field to bind the element to 
 * @param value the value 
 * @param attributes any additional attributes for the element (defaults to "")
-->
<#macro checkbox field value attributes="">
    <input type="checkbox" id="${field}" name="${field}" value="${value}" ${attributes}/>    
</#macro>

<#--
 * Writes a file input element
 *
 * @param field the name of the field to bind the element to 
 * @param attributes any additional attributes for the element (defaults to "")
-->
<#macro file field attributes="">
    <input type="file" id="${field}" name="${field}" ${attributes}/>
</#macro>

<#--
 * Writes a hidden input element with a given value
 *
 * @param field the name of the field to bind the element to 
 * @param value the value
 * @param attributes any additional attributes for the element (defaults to "")
-->
<#macro hidden field value attributes="">
    <input type="hidden" id="${field}" name="${field}" value="${value}" ${attributes}/>
</#macro>

<#--
 * Writes a radio input element with a given value
 *
 * @param field the name of the field to bind the element to 
 * @param value the value 
 * @param attributes any additional attributes for the element (defaults to "")
-->
<#macro radio field value attributes="">
    <input type="radio" id="${field}" name="${field}" value="${value}" ${attributes}/>    
</#macro>

<#--
 * Writes a text input element with a given value
 *
 * @param field the name of the field to bind the element to 
 * @param value the value
 * @param attributes any additional attributes for the element (defaults to "")
-->
<#macro text field value attributes="">
    <input type="text" id="${field}" name="${field}" value="${value}" ${attributes}/>
</#macro>

<#--
 * Writes a text input element with a given values as CSV
 *
 * @param field the name of the field to bind the element to 
 * @param values the values
 * @param attributes any additional attributes for the element (defaults to "")
-->
<#macro textAsCSV field values attributes="">
    <#assign csv=join(values,",")>
    <input type="text" id="${field}" name="${field}" value="${csv}" ${attributes}/>
</#macro>

<#--
 * Writes a textarea element with a given value
 *
 * @param field the name of the field to bind the element to 
 * @param value the value
 * @param attributes any additional attributes for the element (defaults to "")
-->
<#macro textarea field value attributes="">
    <textarea id="${field}" name="${field}" ${attributes}>${value}</textarea>
</#macro>

<#--
 * Writes a select input element allowing a value to be chosen from a list of options.
 * If option value and name fields are not set, they'll default to the option value itself.
 *
 * @param field the name of the field to bind the element to 
 * @param options a sequence of available options
 * @param selectedValue the selected value (defaults to "")
 * @param attributes any additional attributes for the element (defaults to "")
-->
<#macro selectSingle field options selectedValue="" attributes="">
    <#assign selectedValues = [selectedValue]>
    <select id="${field}" name="${field}" ${attributes}>
        <#list options as option>
        <#assign value="${option.value!option}">
        <#assign name="${option.name!option}">
        <#assign selected = contains(selectedValues?default([]), value)>
        <option value="${value?html}" <#if selected>selected="true"</#if>>${name?html}</option>
        </#list>
    </select>
</#macro>

<#--
 * Writes a select input element allowing a multiple values to be chosen from a list of options.
 * If option value and name fields are not set, they'll default to the option value itself.
 *
 * @param field the name of the field to bind the element to 
 * @param options a sequence of available options
 * @param selectedValues the selected values (defaults to [])
 * @param attributes any additional attributes for the element (defaults to "")
-->
<#macro selectMultiple field options selectedValues attributes="">
    <select multiple="multiple" id="${field}" name="${field}" ${attributes}>
        <#list options as option>
        <#assign value="${option.value!option}">
        <#assign name="${option.name!option}">
        <#assign selected = contains(selectedValues?default([]), value)>
        <option value="${value?html}" <#if selected>selected="true"</#if>>${name?html}</option>
        </#list>
    </select>
</#macro>

<#--
 * Determines if values in a sequence are selected, adding the 'selected' attribute if so.
 *
 * @param currentValue the current value in a sequence
 * @param values the values to check 
-->
<#macro areSelected currentValue values>
    <#assign selected = contains(values?default([]), currentValue)>
    <#if selected>selected="true"</#if>>
</#macro>
