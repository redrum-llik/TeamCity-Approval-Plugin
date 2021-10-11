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


<div>
    <a href="/buildConfiguration/Showcase_Approvals_FeatureBasicTest/5870">
        1st
    </a>
    <button>
        <svg width="16" height="16" class="ring-icon-glyph">
            <path d="M8 11.43 3.67 14l1.17-4.81L1 5.97l5.05-.4L8 1l1.95 4.57 5.05.4-3.85 3.22L12.33 14 8 11.43z"/>
        </svg>
    </button>
</div>

<div>
    <a href="/project/Showcase">
        Showcase
    </a>
     /
    <a href="/project/Showcase_Approvals">
        Approvals
    </a>
     /
    <a href="/buildConfiguration/Showcase_Approvals_FeatureBasicTest">
        Feature Basic Test
    </a>
</div>

<div>
    <a href="/buildConfiguration/Showcase_Approvals_FeatureBasicTest/5870" >
        <svg width="16" height="16" class="ring-icon-glyph">
            <path d="M13 3.07v-2H3v2A5 5 0 0 0 7.65 8 5 5 0 0 0 3 13v2h10v-2a5 5 0 0 0-4.65-5A5 5 0 0 0 13 3.07zm-8.6-.6h7.2v.6a3.67 3.67 0 0 1-.14.93H4.54a3.67 3.67 0 0 1-.14-.93zM11.6 13v.6H4.4V13a3.6 3.6 0 0 1 7.2 0z"/>
        </svg>
        Will timeout in 6h 0m
    </a>
    <a href="/buildConfiguration/Showcase_Approvals_FeatureBasicTest/5870?buildTab=changes">
        No changes
    </a>
</div>
<div>
    <button type="button" title="Cancel build...">
        <svg width="16" height="16" class="ring-icon-glyph">
            <path d="M13 3.05A7 7 0 1 0 13 13a7 7 0 0 0 0-9.95zM12 12a5.6 5.6 0 0 1-8 0 5.61 5.61 0 0 1 0-8 5.6 5.6 0 0 1 8 0 5.61 5.61 0 0 1 0 8zm-1.35-7.92L8 6.73 5.35 4.08 4.08 5.35 6.73 8l-2.65 2.65 1.27 1.27L8 9.27l2.65 2.65 1.27-1.27L9.27 8l2.65-2.65z"/>
        </svg>
    </button>
    <button tabindex="0" type="button" aria-label="Actions">
        <svg width="16" height="16" class="ring-icon-glyph">
            <path d="M13 3.05A7 7 0 1 0 13 13a7 7 0 0 0 0-9.95zM12 12a5.6 5.6 0 0 1-8 0 5.61 5.61 0 0 1 0-8 5.6 5.6 0 0 1 8 0 5.61 5.61 0 0 1 0 8zm-1.35-7.92L8 6.73 5.35 4.08 4.08 5.35 6.73 8l-2.65 2.65 1.27 1.27L8 9.27l2.65 2.65 1.27-1.27L9.27 8l2.65-2.65z"/>
        </svg>
    </button>
</div>
Triggered 5 minutes ago by you


<a href="/buildConfiguration/Showcase_Approvals_FeatureBasicTest/5870?buildTab=overview" title="Overview">
    Overview
</a>
<a href="/buildConfiguration/Showcase_Approvals_FeatureBasicTest/5870?buildTab=changes" title="Changes">
    Changes
</a>
<a href="/buildConfiguration/Showcase_Approvals_FeatureBasicTest/5870?buildTab=log" title="Build Log">
    Build Log
</a>
<a href="/buildConfiguration/Showcase_Approvals_FeatureBasicTest/5870?buildTab=queuedBuildCompatibilityTab" title="Build Chain">
    Build Chain
</a>
