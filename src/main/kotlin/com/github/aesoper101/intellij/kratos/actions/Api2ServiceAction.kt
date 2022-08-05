package com.github.aesoper101.intellij.kratos.actions

import com.github.aesoper101.intellij.kratos.KratosBundle
import com.goide.util.GoExecutor

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
        val projectDirectory = LocalFileSystem.getInstance().findFileByIoFile(File(project.presentableUrl!!)) ?: return

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
                .withWorkDirectory(project.presentableUrl)
                .executeWithProgress {
                    folder.refresh(true, false)
                }
        }
    }

    override fun update(e: AnActionEvent) {
        val project = e.project ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        val projectDirectory = LocalFileSystem.getInstance().findFileByIoFile(File(project.presentableUrl!!))!!
        val apiDir = projectDirectory.findChild("api")
        when {
            file == null || file.isDirectory || file.extension != "proto" || apiDir == null || !apiDir.isDirectory || !VfsUtil.isAncestor(
                apiDir,
                file,
                true
            ) -> {
                e.presentation.isVisible = false
                e.presentation.isEnabled = false
            }
        }
    }
}