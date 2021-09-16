package jetbrains.buildServer.approvalPlugin.buildFeature

import jetbrains.buildServer.approvalPlugin.Constants
import jetbrains.buildServer.serverSide.BuildFeature
import jetbrains.buildServer.web.openapi.PluginDescriptor

class ApprovalBuildFeature(descriptor: PluginDescriptor) : BuildFeature() {
    private val myEditUrl = descriptor.getPluginResourcesPath(
        "editApprovalFeatureSettings.jsp"
    )

    override fun getType(): String {
        return Constants.FEATURE_TYPE
    }

    override fun getDisplayName(): String {
        return Constants.FEATURE_DISPLAY_NAME
    }

    override fun getEditParametersUrl(): String? {
        return myEditUrl
    }

    override fun describeParameters(params: MutableMap<String, String>): String {
        return buildString {
            appendLine("The build will stay in queue until it is approved")
            val config = ApprovalFeatureConfiguration(params)
        }
    }
}
