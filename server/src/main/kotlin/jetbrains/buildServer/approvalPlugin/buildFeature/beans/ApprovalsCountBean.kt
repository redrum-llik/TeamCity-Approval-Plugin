package jetbrains.buildServer.approvalPlugin.buildFeature.beans

import jetbrains.buildServer.approvalPlugin.Constants

class ApprovalsCountBean {
    val key = Constants.FEATURE_SETTING_APPROVALS_COUNT
    val label = "Required number of approvals:"
    val description = "Number of approvals required to allow build to start, defaults to 1"
}