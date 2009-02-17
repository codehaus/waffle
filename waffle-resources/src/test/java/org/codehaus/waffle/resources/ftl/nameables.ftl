<#import "/ftl/waffle/form.ftl" as w>
<#assign nameables=w.asNameableValues(controller.getNameables()) />
<#list nameables as n>
${n.value}:${n.name}
</#list>
<#assign displayables=w.asNameableValues(controller.getDisplayables(), "id", "display")/>
<#list nameables as n>
${n.value}:${n.name}
</#list>
