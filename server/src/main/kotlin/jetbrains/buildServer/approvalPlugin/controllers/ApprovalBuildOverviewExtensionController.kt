package jetbrains.buildServer.approvalPlugin.controllers

import jetbrains.buildServer.controllers.BaseFormXmlController
import jetbrains.buildServer.serverSide.BuildPromotionEx
import jetbrains.buildServer.serverSide.BuildPromotionManager
import jetbrains.buildServer.serverSide.SBuildServer
import jetbrains.buildServer.users.SUser
import jetbrains.buildServer.web.openapi.PluginDescriptor
import jetbrains.buildServer.web.openapi.PluginUIContext
import jetbrains.buildServer.web.openapi.WebControllerManager
import jetbrains.buildServer.web.util.SessionUser
import jetbrains.buildServer.web.util.WebUtil
import org.jdom.Element
import org.springframework.security.web.firewall.RequestRejectedException
import org.springframework.web.servlet.ModelAndView
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
            "buildPromotionEx" to buildPromotionEx,
            "user" to user
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