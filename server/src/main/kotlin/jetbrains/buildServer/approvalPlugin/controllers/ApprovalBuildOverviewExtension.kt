package jetbrains.buildServer.approvalPlugin.controllers

import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.approvalPlugin.util.hasApprovalFeatureEnabled
import jetbrains.buildServer.serverSide.BuildPromotionEx
import jetbrains.buildServer.serverSide.BuildPromotionManager
import jetbrains.buildServer.web.openapi.PagePlaces
import jetbrains.buildServer.web.openapi.PlaceId
import jetbrains.buildServer.web.openapi.PluginUIContext
import jetbrains.buildServer.web.openapi.SimplePageExtension
import jetbrains.buildServer.web.util.WebUtil
import javax.servlet.http.HttpServletRequest


class ApprovalBuildOverviewExtension(
    private val promotionManager: BuildPromotionManager,
    pagePlaces: PagePlaces
) : SimplePageExtension(
    pagePlaces,
    PlaceId("SAKURA_BUILD_OVERVIEW"),
    "approveBuildAction",
    "/approvalBuildOverviewExtension.html"
) {
    init {
        register()
    }

    private val LOG = Logger.getInstance(this.javaClass.name)

    override fun isAvailable(request: HttpServletRequest): Boolean {
        if (WebUtil.sakuraUIOpened(request)) {
            val pluginUIContext = PluginUIContext.getFromRequest(request)
            val buildId = pluginUIContext.buildId
            if (buildId != null) {
                val buildPromotionEx = promotionManager
                    .findPromotionById(buildId) as BuildPromotionEx
                if (buildPromotionEx.hasApprovalFeatureEnabled()) {
                    return true
                }
            } else {
                LOG.debug("pluginUiContext is present but buildId is missing")
            }
        }
        return false
    }
}