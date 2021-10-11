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

            appendLine("Build will be cancelled after ${describeTimePeriod(config.getTimeout())}")
        }
    }
}
