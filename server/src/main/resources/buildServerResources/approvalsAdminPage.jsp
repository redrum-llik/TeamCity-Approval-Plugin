<form id="filterForm" action="${reportUrl}" onsubmit="return BS.HealthStatusReport.submitScopeForm();">
    <div class="actionBar">
        <div>
            <label for="project">Show builds pending approval in:</label>
            <forms:select name="scopeProjectId" id="scopeProjectId" onchange="" enableFilter="true" style="width: 20em;">
                <forms:option value="${allProblems}" selected="${allProblems == healthStatusReportBean.scopeProjectId}">&lt;All Projects&gt;</forms:option>
                <c:forEach items="${healthStatusReportBean.projects}" var="projectBean">
                    <c:set var="project" value="${projectBean.project}" />
                    <forms:option value="${project.externalId}"
                                  selected="${project.externalId == healthStatusReportBean.scopeProjectId}"
                                  className="user-depth-${projectBean.limitedDepth}"
                                  title="${project.fullName}">
                        <c:out value="${project.extendedName}"/>
                    </forms:option>
                </c:forEach>
            </forms:select>
            &nbsp;
            <input class="btn btn_mini" type="submit" name="submitFilter" value="Show builds"/>
            &nbsp;
            <span style="cursor: default">
        <forms:saving id="progressIndicator" style="float: none; top: 0;"/>
        <span style="margin-left: 0.5em" id="progressStep" />
      </span>
        </div>
    </div>
</form>