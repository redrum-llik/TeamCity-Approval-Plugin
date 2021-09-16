package jetbrains.buildServer.approvalPlugin.controllers

import jetbrains.buildServer.log.Loggers
import jetbrains.buildServer.serverSide.auth.Permission
import jetbrains.buildServer.web.openapi.PagePlaces
import jetbrains.buildServer.web.openapi.PlaceId
import jetbrains.buildServer.web.openapi.PluginDescriptor
import jetbrains.buildServer.web.openapi.SimplePageExtension
import jetbrains.buildServer.web.util.SessionUser
import javax.servlet.http.HttpServletRequest

class ApprovalsHeaderLinkExtension(
    pagePlaces: PagePlaces,
    descriptor: PluginDescriptor
) : SimplePageExtension(
    pagePlaces,
    PlaceId("SAKURA_HEADER_NAVIGATION_AFTER"),
    "activeApprovals",
    descriptor.getPluginResourcesPath("approvalsHeaderLink.jsp")
) {
    init {
        register()
    }

    override fun isAvailable(request: HttpServletRequest): Boolean {
        val user = SessionUser.getUser(request) ?: return false
        return user.isPermissionGrantedForAnyProject(Permission.EDIT_PROJECT)
    }
}