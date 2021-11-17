<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="jetbrains.buildServer.approvalPlugin.util.BuildPromotionUtilKt" %>
<%@ page import="jetbrains.buildServer.approvalPlugin.util.BuildQueueUtilKt" %>

<jsp:useBean id="approvableBuildsData" scope="request" type="jetbrains.buildServer.approvalPlugin.controllers.ApprovableBuildsData"/>

<%--@elvariable id="user" type="jetbrains.buildServer.web.util.SessionUser>"--%>
<%--@elvariable id="builds" type="java.util.List<jetbrains.buildServer.serverSide.SQueuedBuild>"--%>

<div class="actionBar">
    <span class="nowrap">
        <label for="filterScopeId">Show approvals in:</label>
        <input name="filterScopeId" id="filterScopeId" value="<c:out value='${approvableBuildsData.filterScopeId}'/>" type="hidden"/>
        <div style="width: 330px; display: inline-block; vertical-align: top; margin-right: 10px;">
            <div id="buildTypeSelector" class="comboBox" style="width: 330px;"></div>
            <div id="includeScopeHierarchyDiv" style="display: none">
            <input type="checkbox" name="includeScopeHierarchy" id="includeScopeHierarchy" ${approvableBuildsData.includeScopeHierarchy ? "checked=checked" : ""} unchecked-value="false"/>
            <label for="includeScopeHierarchy">show builds in subprojects</label>
            </div>
        </div>

        <script>
        {
            let selected = null;
            <c:if test="${approvableBuildsData.filterScopeIdFilterApplied}">
            <c:forEach items="${approvableBuildsData.filterScopes}" var="filterScope">
            <c:if test="${approvableBuildsData.filterScopeId == filterScope.id}">
            const idRE = /^(buildType|project)_(.*)$/;
            const parsedId = '${filterScope.id}'.match(idRE);
            if (parsedId !== null) {
                var type = parsedId[1];
                if (type === 'buildType') {
                    type = 'bt';
                }
                var id = parsedId[2];

                selected = {
                    nodeType: type,
                    id: id,
                };
            }
            </c:if>
            </c:forEach>
            <c:if test="${approvableBuildsData.projectIdFilterApplied}">
            $("includeScopeHierarchyDiv").show();
            </c:if>
            </c:if>
            <c:if test="${!approvableBuildsData.filterScopeIdFilterApplied}">
            $("includeScopeHierarchy").checked = true;
            </c:if>
            ReactUI.renderConnected(document.getElementById('buildTypeSelector'), ReactUI.ProjectBuildTypeSelect, {
                allItemName: 'Everywhere',
                allItemSelectable: true,
                expandAll: true,
                includeRoot: true,
                selected,
                onSelect(item) {
                    let filterScopeId;
                    switch (item.nodeType) {
                        case 'project':
                            filterScopeId = 'project_' + item.id;
                            $("includeScopeHierarchyDiv").show();
                            break;
                        case 'bt':
                            filterScopeId = 'buildType_' + item.id;
                            $("includeScopeHierarchyDiv").hide();
                            break;
                        default:
                            filterScopeId = -1;
                            $("includeScopeHierarchyDiv").hide();
                    }
                    $j('#filterScopeId').val(filterScopeId);
                }
            });
        }
        </script>
    </span>
    <span class="actionBarRight">
        <label for="actionsPerPage">Results per page:</label>&nbsp;
        <select id="actionsPerPage" name="actionsPerPage" onchange="BS.AuditLogFilterForm.submit();">
            <admin:_actionsPerPageOption value="10" auditLogData="${approvableBuildsData}"/>
            <admin:_actionsPerPageOption value="20" auditLogData="${approvableBuildsData}"/>
            <admin:_actionsPerPageOption value="50" auditLogData="${approvableBuildsData}"/>
        </select>
        <input type="hidden" name="reset" id="reset"/>
    </span>
<div>

<c:choose>
    <c:when test="${builds.size() > 0}">
        <c:forEach items="${builds}" var="build">
            <div style="border-radius: 3px; border: 1px solid #dfe5bf;background-color: #f7f9fa; padding-left: 15px; padding-right: 15px; margin-top: 15px; font-size: 12px">
                <table>
                    <tbody>
                    <tr>
                        <td style="width: 100%">
                            <p>
                                <span>#${build.orderNumber} (${build.buildType.project.name} / ${build.buildType.name})</span>
                            </p>
                            <p>
                                <span>
                                    Triggered by ${build.triggeredBy.asString}
                                    (${BuildPromotionUtilKt.describeTimeoutDelta(BuildQueueUtilKt.getBuildPromotionEx(build))} left)
                                </span>
                                <span>
                                    <div title="${BuildPromotionUtilKt.describeApprovedBy(BuildQueueUtilKt.getBuildPromotionEx(build))}">
                                        Approvals: <u>
                                            ${BuildPromotionUtilKt.getApprovedBy(BuildQueueUtilKt.getBuildPromotionEx(build)).size()}/${BuildPromotionUtilKt.getApprovalFeatureConfiguration(BuildQueueUtilKt.getBuildPromotionEx(build)).requiredApprovalsCount}
                                        </u>
                                    </div>
                                </span>
                            </p>
                            <p>
                                <c:if test="${BuildPromotionUtilKt.isApprovableByUser(BuildQueueUtilKt.getBuildPromotionEx(build), user) && not BuildPromotionUtilKt.isApprovedByUser(BuildQueueUtilKt.getBuildPromotionEx(build), user)}">
                                    <button onclick="BS.ApprovalLogic.postApproveRequest(${build.itemId})" style="height: 24px; width: 85px; border-radius: 3px; background-color: rgb(0,142,255); border-width: 0; font-size: 12px; color: rgb(255,255,255);">
                                        Approve...
                                    </button>
                                </c:if>
                                <button style="height: 24px; width: 85px; border-radius: 3px; background-color: rgb(0,142,255); border-width: 0; font-size: 12px; color: rgb(255,255,255);">
                                    Cancel...
                                </button>
                            </p>
                        </td>
                        <td style="vertical-align: top; white-space: nowrap">
                            <p><span>${build.buildPromotion.containingChanges.size()} change(s)</span></p>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </c:forEach>
    </c:when>
    <c:otherwise>
        <div>
            There are no builds awaiting your approval
        </div>
    </c:otherwise>
</c:choose>

<c:set var="urlPattern"><c:url value="/admin/admin.html?item=audit&keepSession=1&page=[page]"/></c:set>
<bs:pager place="bottom" urlPattern="${urlPattern}" pager="${approvableBuildsData.pager}"/>

<script type="text/javascript">
    BS.ApprovalLogic = {
        postApproveRequest: function(id) {
            var xhr = new XMLHttpRequest();
            xhr.open("POST", "/approveBuild.html?buildId=" + id, true)
            xhr.onreadystatechange = function() {
                window.location.reload(false)
            }
            xhr.send()
        }
    }
</script>