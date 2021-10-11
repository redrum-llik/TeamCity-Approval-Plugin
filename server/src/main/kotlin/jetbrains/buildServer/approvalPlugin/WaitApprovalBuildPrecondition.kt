package jetbrains.buildServer.approvalPlugin

import jetbrains.buildServer.BuildAgent
import jetbrains.buildServer.approvalPlugin.util.hasEnoughApprovals
import jetbrains.buildServer.approvalPlugin.util.hasTimedOut
import jetbrains.buildServer.approvalPlugin.util.requiresApproval
import jetbrains.buildServer.serverSide.BuildPromotionEx
import jetbrains.buildServer.serverSide.BuildQueueEx
import jetbrains.buildServer.serverSide.buildDistribution.*


class WaitApprovalBuildPrecondition(
    private val buildQueue: BuildQueueEx,
    ) : StartBuildPrecondition {
    override fun canStart(
        queuedBuild: QueuedBuildInfo,
        canBeStarted: MutableMap<QueuedBuildInfo, BuildAgent>,
        buildDistributorInput: BuildDistributorInput,
        emulationMode: Boolean
    ): WaitReason? {
        val buildPromotionEx = queuedBuild.buildPromotionInfo as BuildPromotionEx

        return when {
            buildPromotionEx.hasTimedOut() -> {
                buildQueue.removeQueuedBuilds(
                    listOf(buildPromotionEx.queuedBuild),
                    buildPromotionEx.owner,
                    "Build was not approved in specified time"
                )
                SimpleWaitReason("The build has timed out and is being cancelled currently")
            }
            buildPromotionEx.requiresApproval() && !buildPromotionEx.hasEnoughApprovals() -> {
                SimpleWaitReason("This build should be approved before it may start")
            }
            else -> {
                null
            }
        }
    }
}
