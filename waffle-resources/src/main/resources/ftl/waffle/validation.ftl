<#ftl strip_whitespace=true>
<#--
 * A collection of FTL (Freemarker Template Language) validation functions and macros 
 *
 * @author Mauro Talevi
 -->

<#--
 * Shows error messages as <ul>
 *
 * @param errors the ErrorsContext
 * @param showType the boolean flag to show or not the error type
 * @param showStack the boolean flag to show or not the error stack
 * @param attributes any additional attributes for the element (defaults to "")
 -->
<#macro errorsAsUl errors showType="false" showStack="true" attributes="">
<#if errors.hasErrorMessages()>
    <ul ${attributes}>
    <#list errors.allErrorMessages as error>    
        <li><#if showType=='true'>[${error.type}]&nbsp;</#if> ${error.message}
            <#if showStack=='true'><@listAsUl error.getStackMessages()![] attributes/></#if>
        </li>
    </#list>
    </ul>
</#if>
</#macro>

<#--
 * Shows a list of values as <ul>
 *
 * @param values the values
 * @param attributes any additional attributes for the element (defaults to "")
 -->
<#macro listAsUl values attributes="">
<ul ${attributes}>
<#list values as value>    
    <li>${value}</li>
</#list>
</ul>
</#macro>

