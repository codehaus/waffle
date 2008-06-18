<#import "/ftl/waffle/form.ftl" as w>
<#assign nameables=w.asNameables(controller.getNameables()) />
<#list nameables as n>
${n.value}:${n.name}
</#list>
<#assign displayables=w.asNameables(controller.getDisplayables(), "id", "display")/>
<#list nameables as n>
${n.value}:${n.name}
</#list>
