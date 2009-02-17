<#ftl strip_whitespace=true>
<#--
 * A collection of FTL (Freemarker Template Language) functions and macros for I18N
 *
 * @author Mauro Talevi
 -->

<#--
 * Shows a message for the given key from the messages resources
 *
 * @param key the message key
 * @param default the default message value if message resources or key are not found (defaults to "")
 -->
<#macro messageFor key default="">
    <#if messages?exists && messages.resources?exists><#assign message=messages.resources.getMessageWithDefault(key,default)/><#else><#assign message=default/></#if>${message}
</#macro>
