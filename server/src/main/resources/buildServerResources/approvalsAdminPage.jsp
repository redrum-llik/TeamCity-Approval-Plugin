<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--@elvariable id="builds" type="java.util.List<jetbrains.buildServer.serverSide.SQueuedBuild>"--%>

<c:choose>
    <c:when test="${builds.size() > 0}">
        <c:forEach items="${builds}" var="build">
            <div>
                ${build.itemId} (${build.buildType.name})<br>
            </div>
        </c:forEach>
    </c:when>
    <c:otherwise>
        <div>
            There are no builds awaiting your approval
        </div>
    </c:otherwise>
</c:choose>