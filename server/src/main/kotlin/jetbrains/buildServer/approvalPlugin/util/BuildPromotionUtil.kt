package jetbrains.buildServer.approvalPlugin.util

import jetbrains.buildServer.approvalPlugin.Constants
import jetbrains.buildServer.approvalPlugin.buildFeature.ApprovalFeatureConfiguration
import jetbrains.buildServer.log.Loggers
import jetbrains.buildServer.serverSide.BuildPromotionEx
import jetbrains.buildServer.serverSide.BuildTypeEx
import jetbrains.buildServer.serverSide.SBuildFeatureDescriptor
import jetbrains.buildServer.serverSide.auth.Permission
import jetbrains.buildServer.users.SUser
import java.time.Instant
import java.util.concurrent.TimeUnit
import kotlin.math.max


@Throws(NullPointerException::class)
private fun BuildPromotionEx.getBuildTypeOrDieTrying(): BuildTypeEx {
    return buildType ?: throw NullPointerException("Build type of the build could not be found")
}

/**
 * Returns true if this build's build type has approval feature enabled, or false otherwise.
 */

fun BuildPromotionEx.hasApprovalFeatureEnabled(): Boolean {
    return getBuildTypeOrDieTrying().buildFeatures.any { // only one allowed per build type
        it.buildFeature.type == Constants.FEATURE_TYPE
    }
}

private fun BuildPromotionEx.getApprovalFeature(): SBuildFeatureDescriptor {
    return getBuildTypeOrDieTrying().buildFeatures.first { // only one allowed per build type
        it.buildFeature.type == Constants.FEATURE_TYPE
    }
}

/**
 * Returns ApprovalFeatureConfiguration entity describing configuration of the Approvals feature on this build.
 */

fun BuildPromotionEx.getApprovalFeatureConfiguration(): ApprovalFeatureConfiguration {
    return ApprovalFeatureConfiguration(
        getApprovalFeature().parameters
    )
}

/**
 * Returns true if this build promotion requires approval, or false if it does not.
 */

fun BuildPromotionEx.requiresApproval(): Boolean {
    return getAttribute(Constants.NEEDS_APPROVAL)
        .toString() // attribute is Any, but we only write in Boolean
        .toBoolean()
}

/**
 * Mark that this build requires approval.
 */

fun BuildPromotionEx.markAsRequiringApproval() {
    setAttribute(Constants.NEEDS_APPROVAL, true)
}

/**
 * Returns true if this build may be approved by specified user, or false otherwise.
 */

fun BuildPromotionEx.isApprovableByUser(user: SUser): Boolean {
    return user.isPermissionGrantedForProject(
        getBuildTypeOrDieTrying().projectId,
        Permission.EDIT_PROJECT
    )
}

/**
 * Returns true if this build was approved by specified user, or false otherwise.
 */

fun BuildPromotionEx.isApprovedByUser(user: SUser): Boolean {
    return getApprovedBy().contains(user.username)
}

/**
 * Return set of usernames of users who have approved this build.
 */

fun BuildPromotionEx.getApprovedBy(): MutableSet<String> {
    return when (val rawApprovedBy = getAttribute(Constants.APPROVED_BY)) {
        null -> {
            mutableSetOf()
        }
        else -> {
            (rawApprovedBy as String).split("|")
                .toMutableSet()
        }
    }
}

/**
 * Returns true if current number of approvals for this build is greater or equal than the one specified in feature configuration.
 */

fun BuildPromotionEx.hasEnoughApprovals(): Boolean {
    val requiredApprovals = getApprovalFeatureConfiguration().getRequiredApprovalsCount()
    val currentApprovals = getApprovedBy().size

    return currentApprovals >= requiredApprovals
}

/**
 * Update set of usernames who approved this build with the specified user's one.
 */

fun BuildPromotionEx.addApprovedByUser(user: SUser) {
    val newApprovedBySet = getApprovedBy() + user.username

    setAttribute(
        Constants.APPROVED_BY,
        newApprovedBySet.joinToString("|")
    )
}

/**
 * Set timestamp for the moment when this build promotion will time out.
 */

fun BuildPromotionEx.setTimeout(timeout: Long) {
    val currentEpochSecond = Instant.now().epochSecond
    val targetEpochSecond = currentEpochSecond + timeout
    setAttribute(Constants.TIMEOUT_AT, targetEpochSecond)
}

/**
 * Retrieve timestamp for the moment when this build promotion will time out.
 */

fun BuildPromotionEx.getTimeout(): Long {
    return getAttribute(Constants.TIMEOUT_AT)
        .toString() // attribute is Any, but we only write in Long
        .toLong()
}

/**
 * Describe a time period, in "dd d HH h mm m" format. Expects period length in seconds.
 */

fun describeTimePeriod(period: Long): String {
    var p = max(0, period)

    val days = TimeUnit.SECONDS
        .toDays(p)
    p -= TimeUnit.DAYS.toSeconds(days)

    val hours = TimeUnit.SECONDS
        .toHours(p)
    p -= TimeUnit.HOURS.toSeconds(hours)

    val minutes = TimeUnit.SECONDS
        .toMinutes(p)

    return when {
        days > 0 -> "${days}d ${hours}h ${minutes}m"
        hours > 0 -> "${hours}h ${minutes}m"
        else -> "${minutes}m"
    }
}

/**
 * Describe delta between the timeout timestamp and now, in "dd d HH h mm m" format.
 */

fun BuildPromotionEx.describeTimeoutDelta(): String {
    val currentEpochSecond = Instant.now().epochSecond
    val delta = getTimeout() - currentEpochSecond
    return describeTimePeriod(delta)
}

/**
 * Returns true if this build promotion has timed out (e.g. timeout timestamp was reached), and false otherwise.
 */

fun BuildPromotionEx.hasTimedOut(): Boolean {
    val currentEpochSecond = Instant.now().epochSecond
    return currentEpochSecond >= getTimeout()
}