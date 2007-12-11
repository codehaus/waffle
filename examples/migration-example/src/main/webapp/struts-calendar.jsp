<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>    
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<div>
    <html:form action="/calendar.action">
        <html:hidden property="action" value=""/>
        <html:hidden property="actionTarget" value=""/>
        <td colspan="2" style="width:50%">
            <table id="tableRow" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <th>
                        <html:select property="language">
                            <html:optionsCollection property="languages" label="label"
                                                    value="value"/>
                        </html:select>
                    </th>
                </tr>
                <tr>
                    <th>
                        <html:select property="year">
                            <html:optionsCollection property="years" label="label" value="value"/>
                        </html:select>
                    </th>
                </tr>
            </table>
        </td>
        <td>
            <html:submit
                    onclick="document.calendarForm.action.value='query'"/>
        </td>
    </html:form>
    <logic:present name="calendarForm" property="months">
        <table id="tableContent" cellpadding="0" cellspacing="0" border="0">
            <thead>
                <tr>
                    <td>Months</td>
                </tr>
            </thead>
            <tbody>
                <logic:iterate id="month" name="calendarForm" property="months">
                    <tr>
                        <td align="center">
                            <bean:write name="month"
                                        property="displayName"/>
                        </td>
                        <td align="center">
                            <bean:write name="month"
                                        property="year"/>
                        </td>
                    </tr>
                </logic:iterate>
            </tbody>
        </table>


    </logic:present>
</div>
