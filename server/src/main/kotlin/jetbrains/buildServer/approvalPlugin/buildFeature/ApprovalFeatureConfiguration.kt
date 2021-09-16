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

    fun getTimeout(): Int {
        val count = params[Constants.FEATURE_SETTING_TIMEOUT]?.toInt()
        return when {
            count != null && count > 0 -> {
                count
            }
            else -> {
                Constants.DEFAULT_TIMEOUT_SECONDS
            }
        }
    }
}