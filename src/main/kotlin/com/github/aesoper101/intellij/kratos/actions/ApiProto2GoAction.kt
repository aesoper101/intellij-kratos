package com.github.aesoper101.intellij.kratos.actions

import com.github.aesoper101.intellij.kratos.KratosBundle
import com.github.aesoper101.intellij.kratos.notification.Notification
import com.github.aesoper101.intellij.kratos.notification.NotificationManager
import com.goide.util.GoExecutor
import com.goide.vgo.VgoUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import java.io.File

class ApiProto2GoAction :
    AnAction(KratosBundle.message("action.api2go.name"), KratosBundle.message("action.api2go.description"), null) {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val module = e.getData(PlatformDataKeys.MODULE) ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

        val projectDirectory = VgoUtil.findModuleRoot(file) ?: return

        val apiPath = VfsUtil.getRelativePath(file.parent, projectDirectory)
        val filePath = VfsUtil.getRelativePath(file, projectDirectory)

        val params = listOf(
            "--proto_path=./${apiPath}",
            "--proto_path=./third_party",
            "--go_out=paths=source_relative:./${apiPath}",
            "--go-http_out=paths=source_relative:./${apiPath}",
            "--go-grpc_out=paths=source_relative:./${apiPath}",
            "--validate_out=paths=source_relative,lang=go:./${apiPath}",
            "--openapi_out=fq_schema_naming=true,default_response=false:./${apiPath}",
            "./${filePath}"
        )

        GoExecutor.`in`(module).withExePath("protoc")
            .withParameters(params)
            .withWorkDirectory(projectDirectory.path)
            .withPresentableName(KratosBundle.message("action.api2go.description"))
            .executeWithProgress {
                when(it.status) {
                    GoExecutor.ExecutionResult.Status.SUCCEEDED -> {
                        VfsUtil.markDirtyAndRefresh(
                            true, true, true, projectDirectory
                        )
                    }
                    else ->{
                        NotificationManager.getInstance().createNotification().error(project, it.message!!)
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