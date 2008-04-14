<#ftl strip_whitespace=true>
<#--
 * A collection of FTL (Freemarker Template Language) functions and macros for use in Waffle web applications
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
 * Shows values as CSV
 *
 * @param values the sequence of values
 -->
<#macro asCSV values>
    <#list values as value>${value}<#if value_has_next>,</#if></#list>
</#macro>

<#--
 * Show a select input element allowing a value to be chosen from a list of options.
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
 * Show a select input element allowing a multiple values to be chosen from a list of options.
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
