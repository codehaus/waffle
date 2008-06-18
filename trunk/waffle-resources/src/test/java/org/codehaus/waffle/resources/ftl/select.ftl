<#import "/ftl/waffle/form.ftl" as w>
<@w.selectSingle "values" controller.getValues() "one"/>
<@w.selectSingle "nameables" controller.getNameables() "two"/>
<@w.selectSingle "types" w.asNameables(controller.getTypes(), "name()", "name()") controller.getSelectedType() />
<@w.selectMultiple "values" controller.getValues() controller.getSelectedValues() />
<@w.selectMultiple "nameables" controller.getNameables() controller.getSelectedValues() />
<@w.selectMultiple "displayables" w.asNameables(controller.getDisplayables(),"id","display") controller.getSelectedValues() />