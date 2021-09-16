package jetbrains.buildServer.approvalPlugin

import jetbrains.buildServer.BuildAgent
import jetbrains.buildServer.serverSide.buildDistribution.*
import jetbrains.buildServer.approvalPlugin.ApprovableBuildUtil as ABL


class WaitApprovalBuildPrecondition : StartBuildPrecondition {
    override fun canStart(
        queuedBuild: QueuedBuildInfo,
        canBeStarted: MutableMap<QueuedBuildInfo, BuildAgent>,
        buildDistributorInput: BuildDistributorInput,
        emulationMode: Boolean
    ): WaitReason? {
        return when {
            ABL.buildNeedsApproval(queuedBuild)
                    && ABL.getUsernamesThatApprovedBuild(queuedBuild).size <= 0
            -> {
                SimpleWaitReason("This build should be approved before it may start")
            }
            else -> {
                null
            }
        }
    }
}
