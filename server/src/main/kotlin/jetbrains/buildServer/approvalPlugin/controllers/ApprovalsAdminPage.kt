package jetbrains.buildServer.approvalPlugin.controllers

import jetbrains.buildServer.approvalPlugin.util.getApprovableBuilds
import jetbrains.buildServer.controllers.admin.AdminPage
import jetbrains.buildServer.serverSide.BuildQueue
import jetbrains.buildServer.serverSide.BuildQueueEx
import jetbrains.buildServer.serverSide.auth.Permission
import jetbrains.buildServer.web.openapi.PagePlaces
import jetbrains.buildServer.web.openapi.PluginDescriptor
import jetbrains.buildServer.web.openapi.PositionConstraint
import jetbrains.buildServer.web.util.SessionUser
import javax.servlet.http.HttpServletRequest

class ApprovalsAdminPage(
    pagePlaces: PagePlaces,
    descriptor: PluginDescriptor,
    private val buildQueue: BuildQueue
) : AdminPage(
    pagePlaces,
    "approvals",
    descriptor.getPluginResourcesPath("approvalsAdminPage.jsp"),
    "Approvals"
) {
    init {
        setPosition(PositionConstraint.after("builds"))
        addCssFile("extensions.css")
        register()
    }

    override fun isAvailable(request: HttpServletRequest): Boolean {
        return super.isAvailable(request)
                && checkHasGlobalPermission(request, Permission.EDIT_PROJECT)
    }

    override fun fillModel(model: MutableMap<String, Any>, request: HttpServletRequest) {
        val user = SessionUser.getUser(request)
        val builds = (buildQueue as BuildQueueEx).getApprovableBuilds(user)
        model["user"] = user
        model["builds"] = builds
    }

    override fun getGroup(): String {
        return PROJECT_RELATED_GROUP
    }
}