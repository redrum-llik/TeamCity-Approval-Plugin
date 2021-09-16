package jetbrains.buildServer.approvalPlugin

import jetbrains.buildServer.serverSide.*
import jetbrains.buildServer.serverSide.auth.Permission
import jetbrains.buildServer.serverSide.buildDistribution.QueuedBuildInfo
import jetbrains.buildServer.users.SUser

object ApprovableBuildUtil {
    @JvmStatic
    fun getBuildPromotionEx(queuedBuild: SQueuedBuild): BuildPromotionEx {
        return queuedBuild.buildPromotion as BuildPromotionEx
    }

    @JvmStatic
    fun getBuildPromotionEx(queuedBuildInfo: QueuedBuildInfo): BuildPromotionEx {
        return queuedBuildInfo.buildPromotionInfo as BuildPromotionEx
    }

    @JvmStatic
    fun hasApprovalFeatureEnabled(buildType: SBuildType): Boolean {
        return buildType.buildFeatures.any {
            it.buildFeature.type == Constants.FEATURE_TYPE
        }
    }

    @JvmStatic
    fun hasApprovalFeatureEnabled(queuedBuild: SQueuedBuild): Boolean {
        return hasApprovalFeatureEnabled(queuedBuild.buildType)
    }

    @JvmStatic
    fun markBuildNeedsApproval(queuedBuild: SQueuedBuild) {
        getBuildPromotionEx(queuedBuild).setAttribute(Constants.NEEDS_APPROVAL, true)
    }

    @JvmStatic
    private fun parseApproverUsernames(rawApprovedBy: Any?): MutableSet<String> {
        return when (rawApprovedBy) {
            null -> {
                mutableSetOf()
            }
            else -> {
                (rawApprovedBy as String).split("|")
                    .toMutableSet()
            }
        }
    }

    @JvmStatic
    fun markBuildApprovedBy(queuedBuild: SQueuedBuild, approvedBy: String) {
        val promotion = getBuildPromotionEx(queuedBuild)
        val currentlyApprovedBy = parseApproverUsernames(
            promotion.getAttribute(Constants.APPROVED_BY)
        )

        currentlyApprovedBy.add(approvedBy)
        getBuildPromotionEx(queuedBuild).setAttribute(
            Constants.APPROVED_BY,
            currentlyApprovedBy.joinToString("|")
        )
    }

    @JvmStatic
    fun getUsernamesThatApprovedBuild(queuedBuild: SQueuedBuild): MutableSet<String> {
        val promotion = getBuildPromotionEx(queuedBuild)
        return parseApproverUsernames(
            promotion.getAttribute(Constants.APPROVED_BY)
        )
    }

    @JvmStatic
    fun getUsernamesThatApprovedBuild(queuedBuildInfo: QueuedBuildInfo): MutableSet<String> {
        val promotion = getBuildPromotionEx(queuedBuildInfo)
        return parseApproverUsernames(
            promotion.getAttribute(Constants.APPROVED_BY)
        )
    }

    @JvmStatic
    fun buildNeedsApproval(queuedBuild: SQueuedBuild): Boolean {
        return getBuildPromotionEx(queuedBuild).getAttribute(Constants.NEEDS_APPROVAL)
            .toString() // attribute is Any, but we only write in Boolean
            .toBoolean()
    }

    @JvmStatic
    fun buildNeedsApproval(queuedBuildInfo: QueuedBuildInfo): Boolean {
        return getBuildPromotionEx(queuedBuildInfo).getAttribute(Constants.NEEDS_APPROVAL)
            .toString() // attribute is Any, but we only write in Boolean
            .toBoolean()
    }

    @JvmStatic
    fun getApprovableBuilds(
        buildQueue: BuildQueue
    ): List<SQueuedBuild> {
        return buildQueue.items.filter {
            buildNeedsApproval(it)
        }
    }

    @JvmStatic
    // TODO(method accepts internal ID, but is meant to be used with external IDs)
    fun getApprovableBuilds(
        buildQueue: BuildQueue,
        buildTypeId: String
    ): List<SQueuedBuild> {
        return buildQueue.getItems(buildTypeId).filter {
            buildNeedsApproval(it)
        }
    }

    @JvmStatic
    fun getApprovableBuilds(
        buildQueue: BuildQueue,
        project: SProject
    ): List<SQueuedBuild> {
        val result = mutableListOf<SQueuedBuild>()
        project.buildTypes
            .filter {
                hasApprovalFeatureEnabled(it)
            }
            .forEach {
                result.addAll(buildQueue.getItems(it.buildTypeId))
            }
        return result
    }

    @JvmStatic
    fun isBuildApprovableByUser(
        queuedBuild: SQueuedBuild,
        user: SUser
    ): Boolean {
        return user.isPermissionGrantedForProject(
            queuedBuild.buildType.projectId,
            Permission.EDIT_PROJECT
        )
    }

    @JvmStatic
    fun getApprovableBuildsByUser(
        buildQueue: BuildQueue,
        user: SUser
    ): List<SQueuedBuild> {
        return getApprovableBuilds(buildQueue).filter {
            isBuildApprovableByUser(it, user)
        }
    }

    @JvmStatic
    fun getApprovableBuildsByUser(
        buildQueue: BuildQueue,
        buildTypeId: String,
        user: SUser
    ): List<SQueuedBuild> {
        return getApprovableBuilds(buildQueue, buildTypeId).filter {
            isBuildApprovableByUser(it, user)
        }
    }

    @JvmStatic
    fun getApprovableBuildsByUser(
        buildQueue: BuildQueue,
        project: SProject,
        user: SUser
    ): List<SQueuedBuild> {
        return getApprovableBuilds(buildQueue, project).filter {
            isBuildApprovableByUser(it, user)
        }
    }
}