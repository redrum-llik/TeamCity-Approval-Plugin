package jetbrains.buildServer.approvalPlugin.buildFeature

import jetbrains.buildServer.approvalPlugin.Constants

class ApprovalFeatureConfiguration(private val params: MutableMap<String, String>) {
    fun getRequiredApprovalsCount(): Int {
        val count = params[Constants.FEATURE_SETTING_APPROVALS_COUNT]?.toInt()
        return when {
            count != null && count > 0 -> {
                count
            }
            else -> {
                Constants.DEFAULT_APPROVALS_COUNT
            }
        }
    }

    fun hasCustomApprovalsCount(): Boolean {
        return getRequiredApprovalsCount() != Constants.DEFAULT_APPROVALS_COUNT
    }

    fun getTimeout(): Long {
        val timeoutSeconds = params[Constants.FEATURE_SETTING_TIMEOUT]?.toLong()
        return when {
            timeoutSeconds != null && timeoutSeconds > 0 -> {
                timeoutSeconds
            }
            else -> {
                Constants.DEFAULT_TIMEOUT_SECONDS
            }
        }
    }

    fun treatManualStartOfBuildAsApproval(): Boolean {
        return params[Constants.FEATURE_SETTING_MANUAL_START_IS_APPROVAL].toBoolean()
    }
}