<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/include-internal.jsp" %>

<%@ page import="jetbrains.buildServer.approvalPlugin.util.BuildPromotionUtilKt" %>

<%--@elvariable id="buildPromotionEx" type="jetbrains.buildServer.serverSide.BuildPromotionEx"--%>
<%--@elvariable id="user" type="jetbrains.buildServer.users.SUser"--%>

<div>
    <div>
        <div title="${BuildPromotionUtilKt.describeApprovedBy(buildPromotionEx)}">
            Approvals: <u>
                ${BuildPromotionUtilKt.getApprovedBy(buildPromotionEx).size()}/${BuildPromotionUtilKt.getApprovalFeatureConfiguration(buildPromotionEx).requiredApprovalsCount}
            </u>
        </div>
    </div>
    <c:if test="${buildPromotionEx.queuedBuild != null}">
        <div>
            Waiting for approval (${BuildPromotionUtilKt.describeTimeoutDelta(buildPromotionEx)} left)
        </div>
    </c:if>
</div>

<c:if test="${buildPromotionEx.queuedBuild != null && BuildPromotionUtilKt.isApprovableByUser(buildPromotionEx, user) && not BuildPromotionUtilKt.isApprovedByUser(buildPromotionEx, user)}">
    <p>
        <button onclick="BS.ApprovalLogic.postApproveRequest()" style="height: 24px; width: 85px; border-radius: 3px; background-color: rgb(0,142,255); border-width: 0; font-size: 12px; color: rgb(255,255,255);">
            Approve...
        </button>
    </p>
</c:if>


<script type="text/javascript">
    BS.ApprovalLogic = {
        postApproveRequest: function() {
            var xhr = new XMLHttpRequest();
            xhr.open("POST", "/approveBuild.html?buildId=${buildPromotionEx.id}", true)
            xhr.onreadystatechange = function() {
                window.location.reload(false)
            }
            xhr.send()
        }
    }
</script>
