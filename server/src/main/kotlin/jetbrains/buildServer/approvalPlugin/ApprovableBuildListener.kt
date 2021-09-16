package jetbrains.buildServer.approvalPlugin

import jetbrains.buildServer.log.Loggers
import jetbrains.buildServer.serverSide.BuildServerAdapter
import jetbrains.buildServer.serverSide.SBuildServer
import jetbrains.buildServer.serverSide.SQueuedBuild
import jetbrains.buildServer.serverSide.impl.LogUtil
import org.jetbrains.annotations.NotNull


class ApprovableBuildListener(private val sBuildServer: SBuildServer) : BuildServerAdapter() {
    private fun register() {
        sBuildServer.addListener(this)
    }

    override fun buildTypeAddedToQueue(@NotNull queuedBuild: SQueuedBuild) {
        if (ApprovableBuildUtil.hasApprovalFeatureEnabled(queuedBuild)) {
            Loggers.SERVER.debug("${LogUtil.describe(queuedBuild)}: marking as requiring approval")
            ApprovableBuildUtil.markBuildNeedsApproval(queuedBuild)
        }
    }
}