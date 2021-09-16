package jetbrains.buildServer.approvalPlugin.controllers

import jetbrains.buildServer.controllers.admin.AdminPage
import jetbrains.buildServer.serverSide.auth.Permission
import jetbrains.buildServer.web.openapi.PagePlaces
import jetbrains.buildServer.web.openapi.PluginDescriptor
import jetbrains.buildServer.web.openapi.PositionConstraint
import javax.servlet.http.HttpServletRequest

class ApprovalsAdminPage(
    pagePlaces: PagePlaces,
    descriptor: PluginDescriptor
) : AdminPage(
    pagePlaces,
    "approvals",
    descriptor.getPluginResourcesPath("approvalsAdminPage.jsp"),
    "Approvals"
) {
    init {
        setPosition(PositionConstraint.after("builds"))
        register()
    }

    override fun isAvailable(request: HttpServletRequest): Boolean {
        return super.isAvailable(request)
                && checkHasGlobalPermission(request, Permission.EDIT_PROJECT)
    }

    override fun getGroup(): String {
        return PROJECT_RELATED_GROUP
    }
}