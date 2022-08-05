package com.github.aesoper101.intellij.kratos.actions

import com.github.aesoper101.intellij.kratos.KratosBundle
import com.goide.util.GoExecutor
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

        val projectDirectory = LocalFileSystem.getInstance().findFileByIoFile(File(project.presentableUrl!!))!!

        val apiPath = VfsUtil.getRelativePath(file.parent, projectDirectory)
        val filePath = VfsUtil.getRelativePath(file, projectDirectory)

        val params = listOf(
            "--proto_path=./${apiPath}",
            "--proto_path=./third_party",
            "--go_out=paths=source_relative:./${apiPath}",
            "--go-http_out=paths=source_relative:./${apiPath}",
            "--go-grpc_out=paths=source_relative:./${apiPath}",
            "--openapi_out=fq_schema_naming=true,default_response=false:.",
            "./${filePath}"
        )

        GoExecutor.`in`(module).withExePath("protoc").withParameters(params).withWorkDirectory(project.presentableUrl)
            .withPresentableName(KratosBundle.message("action.api2go.description"))
            .executeWithProgress {
                VfsUtil.markDirtyAndRefresh(
                    true, true, true, projectDirectory
                )
            }

    }

    override fun update(e: AnActionEvent) {
        val project = e.project ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        val projectDirectory = LocalFileSystem.getInstance().findFileByIoFile(File(project.presentableUrl!!))!!
        val apiDir = projectDirectory.findChild("api")
        when {
            file == null || file.isDirectory ||  file.extension != "proto" || apiDir == null || !apiDir.isDirectory || !VfsUtil.isAncestor(
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