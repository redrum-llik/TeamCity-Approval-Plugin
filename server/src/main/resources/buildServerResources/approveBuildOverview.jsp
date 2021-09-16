<%@ include file="/include-internal.jsp" %>

<jsp:useBean id="pluginUIContext" scope="request" type="jetbrains.buildServer.web.openapi.PluginUIContext"/>

<div>
    <p>
        Build is waiting for approval (2h 23m left)
    </p>
</div>
<div>
    <p>
        <button class="ring-button-button ring-button-group-button ring-button-set-button ring-button-toolbar-button ring-button-light">
            user.first
        </button>
        <button class="ring-button-button ring-button-group-button ring-button-set-button ring-button-toolbar-button ring-button-light">
            user.second
        </button>
        <button class="ring-button-button ring-button-group-button ring-button-set-button ring-button-toolbar-button ring-button-light">
            &nbsp;
        </button>
        <button onclick="BS.ApprovalLogic.postApproveRequest()" class="ring-button-button ring-button-group-button ring-button-set-button ring-button-toolbar-button ring-button-light ring-button-primary">
            Approve...
        </button>
    </p>
</div>

<script type="text/javascript">
    BS.ApprovalLogic = {
        postApproveRequest: function() {
            var xhr = new XMLHttpRequest();
            xhr.open("POST", "/approveBuild.html?buildId=${pluginUIContext.buildId}", true)
            xhr.onreadystatechange = function() {
                window.location.reload(false)
            }
            xhr.send()
        }
    }
</script>
