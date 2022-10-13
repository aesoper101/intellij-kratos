package com.github.aesoper101.intellij.kratos.actions

import com.github.aesoper101.intellij.kratos.KratosBundle
import com.github.aesoper101.intellij.kratos.notification.Notification
import com.github.aesoper101.intellij.kratos.notification.NotificationManager
import com.github.aesoper101.intellij.kratos.utils.FileUtil
import com.goide.util.GoExecutor
import com.goide.vgo.VgoUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import java.io.File

class ValidateProto2GoAction :
    AnAction(KratosBundle.message("action.validate2go.name"), KratosBundle.message("action.validate2go.description"), null) {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val module = e.getData(PlatformDataKeys.MODULE) ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

        val projectDirectory = VgoUtil.findModuleRoot(file) ?: return

        val apiPath = VfsUtil.getRelativePath(file.parent, projectDirectory)
        val filePath = VfsUtil.getRelativePath(file, projectDirectory)

        val descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
            .withRoots(projectDirectory)
            .withTitle(KratosBundle.message("action.conf2go.dialog.title"))
            .withShowHiddenFiles(false)


        val folder = FileChooser.chooseFile(descriptor, project, file.parent)
        if (folder != null) {
            val goOutfilePath = VfsUtil.getRelativePath(folder, projectDirectory)

            val params = mutableListOf(
                "--proto_path=./${apiPath}",
                "--proto_path=./third_party",
                "--go_out=paths=source_relative:./${goOutfilePath}",
                "--validate_out=paths=source_relative,lang=go:./${goOutfilePath}",
                "./${filePath}"
            )

            GoExecutor.`in`(module).withExePath("protoc")
                .withParameters(params)
                .withWorkDirectory(projectDirectory.path)
                .withPresentableName(KratosBundle.message("action.api2go.description"))
                .executeWithOutput {
                    when (it.status) {
                        GoExecutor.ExecutionResult.Status.SUCCEEDED -> {
                            VfsUtil.markDirtyAndRefresh(
                                true, true, true, projectDirectory
                            )
                        }
                        else -> {
                            NotificationManager.getInstance().createNotification().error(project, it.message!!)
                        }
                    }
                }
        }


    }

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)


        val content = file?.let { FileUtil.read(it) }

        when {
            file == null || file.isDirectory || !StringUtil.contains(
                content!!,
                "validate/validate.proto"
            ) || file.extension != "proto" || (file.parent?.name != "api" && file.parent?.parent?.name != "api" && file.parent?.parent?.parent?.name != "api") -> {
                e.presentation.isVisible = false
                e.presentation.isEnabled = false
            }
        }
    }
}