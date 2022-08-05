package com.github.aesoper101.intellij.kratos.actions

import com.github.aesoper101.intellij.kratos.KratosBundle
import com.goide.util.GoExecutor
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import java.io.File

class ConfProto2GoAction :
    AnAction(KratosBundle.message("action.conf2go.name"), KratosBundle.message("action.conf2go.description"), null) {
    override fun actionPerformed(e: AnActionEvent) {
        val module = e.getData(PlatformDataKeys.MODULE) ?: return
        val project = e.project ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

        val projectDirectory = LocalFileSystem.getInstance().findFileByIoFile(File(project.presentableUrl!!))!!

        val confPath = VfsUtil.getRelativePath(file.parent, projectDirectory)
        val filePath = VfsUtil.getRelativePath(file, projectDirectory)

        val params = listOf(
            "--proto_path=./${confPath}",
            "--proto_path=./third_party",
            "--go_out=paths=source_relative:./${confPath}",
            "./${filePath}"
        )

        GoExecutor.`in`(module).withExePath("protoc").withParameters(params).withWorkDirectory(project.presentableUrl)
            .withPresentableName(KratosBundle.message("action.conf2go.description"))
            .executeWithProgress {
                VfsUtil.markDirtyAndRefresh(
                    true, true, true, projectDirectory
                )
            }
    }

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)

        when {
            file == null || file.isDirectory || file.extension != "proto" || file.parent.name != "conf" -> {
                e.presentation.isVisible = false
                e.presentation.isEnabled = false
            }
        }
    }
}