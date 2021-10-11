package jetbrains.buildServer.approvalPlugin

import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.approvalPlugin.util.*
import jetbrains.buildServer.log.Loggers
import jetbrains.buildServer.serverSide.BuildServerAdapter
import jetbrains.buildServer.serverSide.SBuildServer
import jetbrains.buildServer.serverSide.SQueuedBuild
import jetbrains.buildServer.serverSide.impl.LogUtil
import org.jetbrains.annotations.NotNull


class ApprovableBuildListener(private val sBuildServer: SBuildServer) : BuildServerAdapter() {
    private val LOG = Logger.getInstance(this.javaClass.name)

    private fun register() {
        sBuildServer.addListener(this)
    }

    override fun buildTypeAddedToQueue(@NotNull queuedBuild: SQueuedBuild) {
        val buildPromotionEx = queuedBuild.getBuildPromotionEx()
        if (buildPromotionEx.hasApprovalFeatureEnabled()) {
            LOG.debug("${LogUtil.describe(queuedBuild)}: marking as requiring approval")
            buildPromotionEx.markAsRequiringApproval()
            buildPromotionEx.setTimeout(
                buildPromotionEx.getApprovalFeatureConfiguration().getTimeout()
            )
        }
    }
}