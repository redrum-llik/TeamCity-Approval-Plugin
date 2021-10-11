package jetbrains.buildServer.approvalPlugin.controllers

import jetbrains.buildServer.approvalPlugin.util.*
import jetbrains.buildServer.controllers.BaseFormXmlController
import jetbrains.buildServer.controllers.BuildDataExtensionUtil
import jetbrains.buildServer.log.Loggers
import jetbrains.buildServer.serverSide.*
import jetbrains.buildServer.serverSide.impl.LogUtil
import jetbrains.buildServer.users.SUser
import jetbrains.buildServer.web.openapi.PluginDescriptor
import jetbrains.buildServer.web.openapi.PluginUIContext
import jetbrains.buildServer.web.openapi.WebControllerManager
import jetbrains.buildServer.web.util.SessionUser
import jetbrains.buildServer.web.util.WebUtil
import org.jdom.Element
import org.springframework.security.web.firewall.RequestRejectedException
import org.springframework.web.servlet.ModelAndView
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ApprovalBuildOverviewExtensionController(
    server: SBuildServer,
    private val promotionManager: BuildPromotionManager,
    controllerManager: WebControllerManager,
    private val descriptor: PluginDescriptor
) : BaseFormXmlController(server) {
    init {
        controllerManager.registerController("/approvalBuildOverviewExtension.html", this)
    }

    private fun prepareMav(buildPromotionEx: BuildPromotionEx, user: SUser): ModelAndView {
        val mav = ModelAndView(
            descriptor.getPluginResourcesPath("approveBuildOverview.jsp")
        )

        val modelMap = mutableMapOf(
            "buildId" to buildPromotionEx.id,
            "isQueued" to (buildPromotionEx.queuedBuild != null),
            "timeoutTimestampDescribed" to buildPromotionEx.describeTimeoutDelta(),
            "currentApprovalCount" to buildPromotionEx.getApprovedBy().size,
            "requiredApprovalCount" to buildPromotionEx.getApprovalFeatureConfiguration().getRequiredApprovalsCount(),
            "currentlyApprovedBy" to buildPromotionEx.getApprovedBy().joinToString(),
            "isApprovedByCurrentUser" to buildPromotionEx.isApprovedByUser(user),
            "isApprovableByCurrentUser" to buildPromotionEx.isApprovableByUser(user)
        )

        mav.addAllObjects(modelMap)
        return mav
    }

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        if (WebUtil.sakuraUIOpened(request)) {
            val pluginUIContext = PluginUIContext.getFromRequest(request)
            val buildId = pluginUIContext.buildId
            if (buildId != null) {
                val buildPromotionEx = promotionManager
                    .findPromotionById(buildId) as BuildPromotionEx
                val user = SessionUser.getUser(request)
                return prepareMav(buildPromotionEx, user)
            } else {
                throw IllegalStateException("pluginUiContext is present but buildId is missing")
            }
        } else {
            throw RequestRejectedException("This controller works only with Sakura UI")
        }
    }

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse, xmlResponse: Element) {
        TODO("Not yet implemented")
    }
}