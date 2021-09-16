package jetbrains.buildServer.approvalPlugin

object Constants {
    // plugin-level data
    const val FEATURE_DISPLAY_NAME = "Build Approval"
    const val FEATURE_TYPE = "approval-feature"

    // feature variables
    const val FEATURE_SETTING_APPROVALS_COUNT = "approversCount"
    const val FEATURE_SETTING_TIMEOUT = "timeout"

    // default values
    const val DEFAULT_APPROVALS_COUNT = 1
    const val DEFAULT_TIMEOUT_SECONDS = 21600

    // build promotion attributes
    const val NEEDS_APPROVAL = "teamcity.build.approval.required"
    const val APPROVED_BY = "teamcity.build.approval.approvedBy"
}

