package com.github.aesoper101.intellij.kratos.actions

import com.github.aesoper101.intellij.kratos.KratosBundle
import com.github.aesoper101.intellij.kratos.notification.NotificationManager
import com.goide.util.GoExecutor
import com.goide.vgo.VgoUtil

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import java.io.File

class Api2ServiceAction : AnAction(
    KratosBundle.message("action.api2service.name"),
    KratosBundle.message("action.api2service.description"),
    null
) {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val module = e.getData(PlatformDataKeys.MODULE) ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        val projectDirectory = VgoUtil.findModuleRoot(file) ?: return

        val descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
            .withRoots(projectDirectory)
            .withTitle(KratosBundle.message("action.api2service.dialog.title"))
            .withShowHiddenFiles(false)


        val folder = FileChooser.chooseFile(descriptor, project, null)
        if (folder != null) {
            val sourcePath = VfsUtil.getRelativePath(file, projectDirectory)!!
            val target = VfsUtil.getRelativePath(folder, projectDirectory)!!

            GoExecutor.`in`(project, module).disablePty()
                .withExePath("kratos")
                .withParameters(listOf("proto", "server", sourcePath, "-t", target))
                .withWorkDirectory(projectDirectory.path)
                .executeWithProgress {
                    when(it.status) {
                        GoExecutor.ExecutionResult.Status.SUCCEEDED -> {
                            folder.refresh(true, false)
                        }
                        else ->{
                            NotificationManager.getInstance().createNotification().error(project, it.message!!)
                        }
                    }
                }
        }
    }

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)

        when {
            file == null || file.isDirectory || file.extension != "proto" || (file.parent?.name != "api" && file.parent?.parent?.name != "api" && file.parent?.parent?.parent?.name != "api") -> {
                e.presentation.isVisible = false
                e.presentation.isEnabled = false
            }
        }
    }
}