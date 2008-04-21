<#import "/ftl/waffle/form.ftl" as w>
<#import "/ftl/waffle/validation.ftl" as v>
<div class="editContainer" xmlns="http://www.w3.org/1999/xhtml">

    <h3>Edit Person</h3>

    <@v.errorsAsUl errors "true" "false" "class='errors'"/>
    
    <div class="fieldRow">
        <label for="person.id">Id:</label>
        <@w.text "person.id" "${person.id}" "readonly='true' disabled='true'"/>
        <br style="clear:both"/>
    </div>
    <div class="fieldRow">
        <label for="person.firstName">First Name:</label>
        <@w.text "person.firstName" "${person.firstName}"/>
        <br style="clear:both"/>
    </div>
    <div class="fieldRow">
        <label for="person.lastName">Last Name:</label>
        <@w.text "person.lastName" "${person.lastName}"/>
        <br style="clear:both"/>
    </div>
    <div class="fieldRow">
        <label for="person.email">Email:</label>
        <@w.text "person.email" "${person.email}"/>
        <br style="clear:both"/>
    </div>
    <div class="fieldRow">
        <label for="person.dateOfBirth">Date Of Birth:</label>
        <#if person.dateOfBirth??>
            <@w.text "person.dateOfBirth" "${person.dateOfBirth?string('dd/MM/yyyy')}"/>
        <#else>
            <@w.text "person.dateOfBirth" ""/>
        </#if> 
        <br style="clear:both"/>
    </div>
    <div class="fieldRow">
        <label for="person.skills">Skills:</label>
        <@w.selectMultiple "person.skills" controller.getSkills() person.getSkills() "size='5'"/>
        <br style="clear:both"/>
    </div>
    <div class="fieldRow">
        <label for="person.levels">Levels:</label>
        <@w.textAsCSV "person.levels" person.getLevels()![] /> 
        <br style="clear:both"/>
    </div>
    <div class="fieldRow">
        <label for="person.grades">Grades:</label>
        <@w.textAsCSV "person.grades" person.getGrades()![] /> 
        <br style="clear:both"/>
    </div>

    <br/>
    <a href="javascript:fireActionMethod('save');">Save</a> |
    <a href="javascript:fireActionMethod('cancel');">Cancel</a>

    <#include "/fielderrors.ftl" parse="true">

</div>