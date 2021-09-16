package jetbrains.buildServer.approvalPlugin.controllers

import jetbrains.buildServer.approvalPlugin.ApprovableBuildUtil as ABL
import jetbrains.buildServer.controllers.BaseFormXmlController
import jetbrains.buildServer.issueTracker.errors.NotFoundException
import jetbrains.buildServer.log.Loggers
import jetbrains.buildServer.serverSide.BuildPromotionManager
import jetbrains.buildServer.serverSide.BuildQueueEx
import jetbrains.buildServer.serverSide.BuildServerEx
import jetbrains.buildServer.serverSide.SBuildServer
import jetbrains.buildServer.web.openapi.WebControllerManager
import jetbrains.buildServer.web.util.SessionUser
import org.jdom.Element
import org.springframework.web.servlet.ModelAndView
import java.lang.IllegalArgumentException
import javax.lang.model.UnknownEntityException
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
        val user = SessionUser.getUser(request)

        val pluginUIContext = request.getParameter("pluginUIContext")
        if (!pluginUIContext.isNullOrEmpty()) {
            return simpleView(pluginUIContext)
        }

        val projectId = request.getParameter("projectId")
        if (!projectId.isNullOrEmpty()) {
            val project = (server as BuildServerEx).projectManager.findProjectByExternalId(projectId)
            if (project != null) {
                return simpleView(
                    "found builds: " + ABL.getApprovableBuildsByUser(buildQueue, project, user).size
                )
            }
        }

        val buildTypeId = request.getParameter("buildTypeId")
        if (!buildTypeId.isNullOrEmpty()) {
            return simpleView(
                "found builds: " + ABL.getApprovableBuildsByUser(buildQueue, buildTypeId, user).size
            )
        }

        val buildId = request.getParameter("buildId")
        if (!buildId.isNullOrEmpty()) {
            val build = promotionManager.findPromotionById(buildId.toLong())?.queuedBuild
            if (build != null && ABL.isBuildApprovableByUser(build, user)) {
                return simpleView(
                    "found build: ${build.buildTypeId} - ${build.itemId}"
                )
            }
        }
        return null
    }

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse, xmlResponse: Element) {
        val buildId = request.getParameter("buildId")
            ?: throw IllegalArgumentException("buildId should be specified for this request")

        val user = SessionUser.getUser(request)
            ?: throw IllegalStateException("Could not resolve user associated with this request")

        val buildPromotion = promotionManager.findPromotionById(buildId.toLong())
            ?: throw NotFoundException("No build promotion found under $buildId")

        val build = buildPromotion.queuedBuild

        if (ABL.isBuildApprovableByUser(build!!, user)) {
            ABL.markBuildApprovedBy(build, user.username)
        }
    }
}