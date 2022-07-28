package com.github.aesoper101.intellij.kratos.actions

import com.github.aesoper101.intellij.kratos.KratosBundle
import com.goide.util.GoExecutor
import com.goide.util.GoUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys


class WireAction :
    AnAction(KratosBundle.message("action.wire.name"), KratosBundle.message("action.wire.description"), null) {
    override fun actionPerformed(e: AnActionEvent) {
        val module = e.getData(PlatformDataKeys.MODULE) ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

        val params = listOf<String>()



        GoExecutor.`in`(module).withExePath("wire").withParameters(params).withWorkDirectory(file.parent?.path)
            .withPresentableName(KratosBundle.message("action.wire.description")).executeWithProgress {
                file.parent?.refresh(true, false)
            }
    }

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)

        when {
            file == null || file.isDirectory || !file.path.endsWith("wire.go") -> {
                e.presentation.isVisible = false
                e.presentation.isEnabled = false
            }
        }
    }
}