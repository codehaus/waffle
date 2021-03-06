<#import "/ftl/waffle/form.ftl" as w>
<#import "/ftl/waffle/validation.ftl" as v>
<div class="editContainer" xmlns="http://www.w3.org/1999/xhtml">

    <@v.errorsAsUl errors "true" "false" "class='errors'"/>
    
    <fieldset>
        <label>Edit Person</label>
        <p class="fieldRow">
            <label for="person.id">Id:</label>
            <@w.text "person.id" "${person.id}" "readonly='true' disabled='true'"/>
        </p>
        <p class="fieldRow">
            <label for="person.firstName">First Name:</label>
            <@w.text "person.firstName" "${person.firstName}"/>
        </p>
        <p class="fieldRow">
            <label for="person.lastName">Last Name:</label>
            <@w.text "person.lastName" "${person.lastName}"/>
        </p>
        <p class="fieldRow">
            <label for="person.email">Email:</label>
            <@w.text "person.email" "${person.email}"/>
        </p>
        <p class="fieldRow">
            <label for="person.dateOfBirth">Date Of Birth:</label>
            <#if person.dateOfBirth??>
                <#assign defaultPattern=controller.dateProvider.defaultPattern>
                <@w.text "person.dateOfBirth" "${person.dateOfBirth?string(defaultPattern)}"/>
            <#else>
                <@w.text "person.dateOfBirth" ""/>
            </#if> 
        </p>
        <p class="fieldRow">
            <label for="person.birthDay">Birth Day:</label>
            <#if person.birthDay??>
                <#assign dayPattern=controller.dateProvider.dayPattern>
                <@w.text "person.birthDay" "${person.birthDay?string(dayPattern)}"/>
            <#else>
                <@w.text "person.birthDay" ""/>
            </#if> 
        </p>
        <p class="fieldRow">
            <label for="person.birthTime">Birth Time:</label>
            <#if person.birthTime??>
                <#assign timePattern=controller.dateProvider.timePattern>
                <@w.text "person.birthTime" "${person.birthTime?string(timePattern)}"/>
            <#else>
                <@w.text "person.birthTime" ""/>
            </#if> 
        </p>
        <p class="fieldRow">
            <label for="person.bestFriend">Best Friend:</label>
            <@w.selectSingle "person.bestFriend" w.asNameableValues(controller.getPeople(),"id","firstName") person.getBestFriend().getId() />
            <@w.hidden "person.bestFriend" ""/>
        </p>
        <p class="fieldRow">
            <label for="person.friends">Friends:</label>
            <@w.selectMultiple "person.friends" w.asNameableValues(controller.getPeople(),"id","firstName") w.asValues(person.getFriends(),"id") "size='5'"/>
            <@w.hidden "person.friends" ""/>
        </p>
        <p class="fieldRow">
            <label for="person.skills">Skills:</label>
            <@w.selectMultiple "person.skills" controller.getSkills() person.getSkills() "size='5'"/>
            <@w.hidden "person.skills" ""/>
        </p>
        <p class="fieldRow">
            <label for="person.levels">Levels:</label>
            <@w.textAsCSV "person.levels" person.getLevels()![] /> 
        </p>
        <p class="fieldRow">
            <label for="person.grades">Grades:</label>
            <@w.textAsCSV "person.grades" person.getGrades()![] /> 
        </p>
        <p class="fieldRow">
            <label for="person.type">Type:</label>
            <@w.selectSingle "person.type" w.asNameableValues(controller.getTypes(),"name()","name()") person.getType()/>
        </p>
        <p class="fieldRow">
            <label for="person.wizard">Is wizard:</label>
            <@w.text "person.wizard" "${person.wizard?string}"/>
        </p>
        <p class="fieldRow">
            <label for="person.magicNumber">Magic Number:</label>
            <@w.text "person.magicNumber" "${person.magicNumber}"/>
        </p>
        <p class="fieldRow">
            <label for="person.notes">Notes:</label>
            <@w.textarea "person.notes" "${person.notes}" />
        </p>
        <p class="fieldRow">
            <#assign numberLists = w.asProperties(person.getNumberLists()![]) />
            <label for="person.numberLists">Number Lists:</label>
            <@w.textarea "person.numberLists" "${numberLists}" />
        </p>
        <p class="fieldRow">
            <#assign stringLists = w.asProperties(person.getStringLists()![]) />
            <label for="person.stringLists">String Lists:</label>
            <@w.textarea "person.stringLists" "${stringLists}" />
        </p>
    </fieldset>
    
    <div class="controls">
        <a href="javascript:fireActionMethod('save');">Save</a> |
        <a href="javascript:fireActionMethod('cancel');">Cancel</a>
    </div>
    
    <#include "/fielderrors.ftl" parse="true">

</div>