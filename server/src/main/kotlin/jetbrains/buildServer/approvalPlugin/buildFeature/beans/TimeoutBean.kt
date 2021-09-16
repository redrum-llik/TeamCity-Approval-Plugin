package jetbrains.buildServer.approvalPlugin.buildFeature.beans

import jetbrains.buildServer.approvalPlugin.Constants

class TimeoutBean {
    val key = Constants.FEATURE_SETTING_TIMEOUT
    val label = "Timeout in:"
    val description = "Amount of time (in seconds) before the build is cancelled, defaults to 6 hours"
}