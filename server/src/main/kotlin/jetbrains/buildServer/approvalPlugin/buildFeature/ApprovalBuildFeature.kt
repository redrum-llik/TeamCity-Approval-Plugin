package jetbrains.buildServer.approvalPlugin.buildFeature

import jetbrains.buildServer.approvalPlugin.Constants
import jetbrains.buildServer.approvalPlugin.util.describeTimePeriod
import jetbrains.buildServer.serverSide.BuildFeature
import jetbrains.buildServer.web.openapi.PluginDescriptor

class ApprovalBuildFeature(descriptor: PluginDescriptor) : BuildFeature() {
    private val myEditUrl = descriptor.getPluginResourcesPath(
        "editApprovalFeatureSettings.jsp"
    )

    override fun getType(): String = Constants.FEATURE_TYPE

    override fun getDisplayName(): String = Constants.FEATURE_DISPLAY_NAME

    override fun getEditParametersUrl(): String = myEditUrl

    override fun isMultipleFeaturesPerBuildTypeAllowed(): Boolean = false

    override fun describeParameters(params: MutableMap<String, String>): String {
        return buildString {
            val config = ApprovalFeatureConfiguration(params)

            if (config.hasCustomApprovalsCount()) {
                appendLine("Build will stay in queue until it is approved by ${config.getRequiredApprovalsCount()} users")
            } else {
                appendLine("Build will stay in queue until it is approved")
            }

            if (config.treatManualStartOfBuildAsApproval()) {
                appendLine("Build started by a user with sufficient permissions will be marked as approved")
            } else {
                appendLine("Build requires explicit approval, even from the user who starts it")
            }

            appendLine("Build will be cancelled after ${describeTimePeriod(config.getTimeout())}")
        }
    }

    override fun getDefaultParameters(): MutableMap<String, String> {
        return mutableMapOf(
            Constants.FEATURE_SETTING_MANUAL_START_IS_APPROVAL to true.toString()
        )
    }
}
