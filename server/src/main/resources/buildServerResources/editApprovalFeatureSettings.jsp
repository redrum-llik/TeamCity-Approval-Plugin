<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>

<jsp:useBean id="approvalsCountBean" class="jetbrains.buildServer.approvalPlugin.buildFeature.beans.ApprovalsCountBean"/>
<jsp:useBean id="timeoutBean" class="jetbrains.buildServer.approvalPlugin.buildFeature.beans.TimeoutBean"/>

<tr>
    <td colspan="2">
        <em>
            Allows to request approval before this build will be assigned to agent.<br/>
        </em>
    </td>
</tr>

<tr id="approvals_count">
    <th><label for="${approvalsCountBean.key}">${approvalsCountBean.label}</label></th>
    <td>
        <props:textProperty name="${approvalsCountBean.key}" className="mediumField" maxlength="2"/>
        <span class="smallNote">${approvalsCountBean.description}</span>
    </td>
</tr>

<tr class="advancedSetting" id="timeout">
    <th><label for="${timeoutBean.key}">${timeoutBean.label}</label></th>
    <td>
        <props:textProperty name="${timeoutBean.key}" className="mediumField"/>
        <span class="smallNote">${timeoutBean.description}</span>
    </td>
</tr>