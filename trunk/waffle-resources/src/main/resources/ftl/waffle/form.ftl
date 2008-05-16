<#ftl strip_whitespace=true>
<#--
 * A collection of FTL (Freemarker Template Language) functions and macros to use in HTML forms 
 *
 * @author Mauro Talevi
 -->

<#--
 * Determines if list contains the item.
 *
 * @param list the list to search for the item
 * @param item the item to search for in the list
 * @return true if item is found in the list, false otherwise
-->
<#function contains list item>
    <#list list as next>
    <#if next == item><#return true></#if>
    </#list>
    <#return false>
</#function>

<#--
 * Joins list values 
 *
 * @param values the values to join
 * @param separator the separator to join list with
 -->
<#function join values separator>
    <#assign result = ''>
    <#list values as value><#assign result=result+value><#if value_has_next><#assign result=result+separator></#if></#list>
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
 *
 * @param field the name of the field to bind the element to 
 * @param options a sequence of available options
 * @param selectedValue the selected value (defaults to "")
 * @param attributes any additional attributes for the element (defaults to "")
-->
<#macro selectSingle field options selectedValue="" attributes="">
    <select id="${field}" name="${field}" ${attributes}>
        <#list options as value>
        <option value="${value?html}"<@isSelected value selectedValue/>>${value?html}</option>
        </#list>
    </select>
</#macro>

<#--
 * Writes a select input element allowing a multiple values to be chosen from a list of options.
 *
 * @param field the name of the field to bind the element to 
 * @param options a sequence of available options
 * @param selectedValues the selected values (defaults to [""])
 * @param attributes any additional attributes for the element (defaults to "")
-->
<#macro selectMultiple field options selectedValues attributes="">
    <select multiple="multiple" id="${field}" name="${field}" ${attributes}>
        <#list options as value>
        <#assign selected = contains(selectedValues?default([""]), value)>
        <option value="${value?html}" <#if selected>selected="true"</#if>>${value?html}</option>
        </#list>
    </select>
</#macro>

<#--
 * Determines if a value in a sequence is selected, adding the 'selected' attribute if so.
 *
 * @param currentValue the current value in a sequence
 * @param value the value to check 
-->
<#macro isSelected currentValue value>
    <#if currentValue?is_number && currentValue == value?number>selected="true"</#if>
    <#if currentValue?is_string && currentValue == value>selected="true"</#if>
</#macro>

<#--
 * Determines if values in a sequence are selected, adding the 'selected' attribute if so.
 *
 * @param currentValue the current value in a sequence
 * @param values the values to check 
-->
<#macro areSelected currentValue values>
    <#assign selected = contains(values?default([""]), currentValue)>
    <#if selected>selected="true"</#if>>
</#macro>
