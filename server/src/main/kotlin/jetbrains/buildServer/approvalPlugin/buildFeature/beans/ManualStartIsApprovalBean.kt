package jetbrains.buildServer.approvalPlugin.buildFeature.beans

import jetbrains.buildServer.approvalPlugin.Constants

class ManualStartIsApprovalBean {
    val key = Constants.FEATURE_SETTING_MANUAL_START_IS_APPROVAL
    val label = "Treat manually started build as approval:"
    val description = "If started by user with sufficient permissions, mark build as approved by user"
}