<#ftl strip_whitespace=true>
<#--
 * A collection of FTL (Freemarker Template Language) validation functions and macros 
 *
 * @author Mauro Talevi
 -->

<#--
 * Shows errors as Div with CSS class "errors".
 * Each error is contained in div with class "error".
 *
 * @param errors the ErrorsContext
 -->
<#macro errorsAsDiv errors>
<#if errors.hasErrorMessages()>
    <div class="errors">
    <#list errors.allErrorMessages as error>
        <div class="error">${error.message}</div>
    </#list>
    </div>
</#if>
</#macro>

