package com.github.aesoper101.intellij.kratos.actions

import com.github.aesoper101.intellij.kratos.KratosBundle
import com.github.aesoper101.intellij.kratos.KratosConstants
import com.goide.util.GoGetPackageUtil
import com.intellij.execution.runners.RunTab.ToolbarActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys


class KratosUpgradeAction : AnAction(KratosBundle.message("action.upgrade.name"), KratosBundle.message("action.upgrade.description"), null) {

    override fun actionPerformed(e: AnActionEvent) {
        val module = e.getData(PlatformDataKeys.MODULE)?: return
//        val project = e.project ?: return

        GoGetPackageUtil.installTool(
            module.project,
            module,
            module.project.presentableUrl,
            KratosConstants.KRATOS_PACKAGE_IMPORT_PATH,
            true,
        )
    }

}
