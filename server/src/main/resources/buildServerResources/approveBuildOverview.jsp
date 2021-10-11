<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/include-internal.jsp" %>

<%--@elvariable id="buildId" type="java.lang.Long"--%>
<%--@elvariable id="isQueued" type="java.lang.Boolean"--%>
<%--@elvariable id="timeoutTimestampDescribed" type="java.lang.String"--%>
<%--@elvariable id="currentApprovalCount" type="java.lang.Integer"--%>
<%--@elvariable id="requiredApprovalCount" type="java.lang.Integer"--%>
<%--@elvariable id="currentlyApprovedBy" type="java.lang.String"--%>
<%--@elvariable id="isApprovedByCurrentUser" type="java.lang.Boolean"--%>
<%--@elvariable id="isApprovableByCurrentUser" type="java.lang.Boolean"--%>

<div class="BuildOverviewTab__oneLine--TY">
    <div class="BuildOverviewTab__queueInfo--3H">
        <c:choose>
            <c:when test="${currentApprovalCount > 0}">
                <div title="${currentlyApprovedBy}">
                    Approvals: <u>${currentApprovalCount}/${requiredApprovalCount}</u>
                </div>
            </c:when>
            <c:otherwise>
                <div>
                    Approvals: ${currentApprovalCount}/${requiredApprovalCount}
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    <c:if test="${isQueued}">
        <div>
            Waiting for approval (${timeoutTimestampDescribed} left)
        </div>
    </c:if>
</div>

<c:if test="${isApprovableByCurrentUser && not isApprovedByCurrentUser}">
    <p>
        <button onclick="BS.ApprovalLogic.postApproveRequest()" class="ring-button-button ring-button-group-button ring-button-set-button ring-button-toolbar-button ring-button-light ring-button-primary">
            Approve...
        </button>
    </p>
</c:if>


<script type="text/javascript">
    BS.ApprovalLogic = {
        postApproveRequest: function() {
            var xhr = new XMLHttpRequest();
            xhr.open("POST", "/approveBuild.html?buildId=${buildId}", true)
            xhr.onreadystatechange = function() {
                window.location.reload(false)
            }
            xhr.send()
        }
    }
</script>
