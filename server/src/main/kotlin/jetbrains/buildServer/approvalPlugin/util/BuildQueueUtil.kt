package jetbrains.buildServer.approvalPlugin.util

import jetbrains.buildServer.serverSide.BuildPromotionEx
import jetbrains.buildServer.serverSide.BuildQueue
import jetbrains.buildServer.serverSide.SProject
import jetbrains.buildServer.serverSide.SQueuedBuild
import jetbrains.buildServer.users.SUser

fun SQueuedBuild.getBuildPromotionEx(): BuildPromotionEx {
    return buildPromotion as BuildPromotionEx
}

/**
 * Returns all builds that are marked as requiring approval.
 */

fun BuildQueue.getApprovableBuilds(): List<SQueuedBuild> {
    return items.filter {
        it.getBuildPromotionEx().requiresApproval()
    }
}

/**
 * Returns all builds that are marked as requiring approval under specified build type ID.
 */

fun BuildQueue.getApprovableBuilds(buildTypeId: String): List<SQueuedBuild> {
    return getItems(buildTypeId).filter {
        it.getBuildPromotionEx().requiresApproval()
    }
}

/**
 * Returns all builds that are marked as requiring approval and could be approved by the specified user.
 */

fun BuildQueue.getApprovableBuilds(user: SUser): List<SQueuedBuild> {
    return items.filter {
        val buildPromotion = it.getBuildPromotionEx()
        buildPromotion.requiresApproval() && buildPromotion.isApprovableByUser(user)
    }
}

/**
 * Returns all builds that are marked as requiring approval under specified build type ID and could be approved by the specified user.
 */

fun BuildQueue.getApprovableBuilds(
    buildTypeId: String,
    user: SUser
): List<SQueuedBuild> {
    return getItems(buildTypeId).filter {
        val buildPromotion = it.getBuildPromotionEx()
        buildPromotion.requiresApproval() && buildPromotion.isApprovableByUser(user)
    }
}