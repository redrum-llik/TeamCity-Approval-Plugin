package jetbrains.buildServer.approvalPlugin.controllers

import jetbrains.buildServer.serverSide.SBuildServer
import jetbrains.buildServer.web.openapi.*
import jetbrains.buildServer.web.util.SessionUser
import javax.servlet.http.HttpServletRequest
import jetbrains.buildServer.approvalPlugin.ApprovableBuildUtil as ABL

class ApprovalBuildOverviewExtension(
    pagePlaces: PagePlaces,
    descriptor: PluginDescriptor,
    private val myServer: SBuildServer
) : SimplePageExtension(
    pagePlaces,
    PlaceId("SAKURA_BUILD_OVERVIEW"),
    "approveBuildAction",
    descriptor.getPluginResourcesPath("approveBuildOverview.jsp")
) {
    init {
        register()
    }

    override fun isAvailable(request: HttpServletRequest): Boolean {
        val user = SessionUser.getUser(request) ?: return false
        return true
        // TODO(resolve build and )
    }
}