package jetbrains.buildServer.approvalPlugin.controllers

import jetbrains.buildServer.approvalPlugin.util.addApprovedByUser
import jetbrains.buildServer.approvalPlugin.util.isApprovableByUser
import jetbrains.buildServer.controllers.BaseFormXmlController
import jetbrains.buildServer.serverSide.BuildPromotionEx
import jetbrains.buildServer.serverSide.BuildPromotionManager
import jetbrains.buildServer.serverSide.BuildQueueEx
import jetbrains.buildServer.serverSide.SBuildServer
import jetbrains.buildServer.web.openapi.WebControllerManager
import jetbrains.buildServer.web.util.SessionUser
import org.jdom.Element
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ApproveBuildController(
    private val server: SBuildServer,
    private val buildQueue: BuildQueueEx,
    private val promotionManager: BuildPromotionManager,
    controllerManager: WebControllerManager
) : BaseFormXmlController(server) {
    init {
        controllerManager.registerController("/approveBuild.html", this)
    }

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse): ModelAndView? {
        return null
    }

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse, xmlResponse: Element) {
        val buildId = request.getParameter("buildId")
            ?: throw IllegalArgumentException("buildId should be specified for this request")

        val user = SessionUser.getUser(request)
            ?: throw IllegalStateException("Could not resolve user associated with this request")

        val buildPromotion = promotionManager.findPromotionById(
            buildId.toLong()
        ) as BuildPromotionEx

        if (buildPromotion.isApprovableByUser(user)) {
            buildPromotion.addApprovedByUser(user)
        }
    }
}