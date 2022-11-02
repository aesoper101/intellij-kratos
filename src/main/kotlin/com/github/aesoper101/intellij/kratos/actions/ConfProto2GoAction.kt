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
import com.intellij.openapi.vfs.VfsUtil

import com.google.protobuf.DescriptorProtos.FileDescriptorProto;

class ConfProto2GoAction :
        AnAction(KratosBundle.message("action.conf2go.name"), KratosBundle.message("action.conf2go.description"), null) {
    override fun actionPerformed(e: AnActionEvent) {
        val module = e.getData(PlatformDataKeys.MODULE) ?: return
        val project = e.project ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

        val projectDirectory = VgoUtil.findModuleRoot(file) ?: return

        val confPath = VfsUtil.getRelativePath(file.parent, projectDirectory)
        val filePath = VfsUtil.getRelativePath(file, projectDirectory)

        val descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
                .withRoots(projectDirectory)
                .withTitle(KratosBundle.message("action.conf2go.dialog.title"))
                .withShowHiddenFiles(false)


        val folder = FileChooser.chooseFile(descriptor, project, file.parent)
        if (folder != null) {
            val goOutfilePath = VfsUtil.getRelativePath(folder, projectDirectory)
            val params = listOf(
                    "--proto_path=./${confPath}",
                    "--proto_path=./third_party",
                    "--go_out=paths=source_relative:./${goOutfilePath}",
                    "--go_opt=Mkratos/http/api.proto=github.com/aesoper101/kratos-utils/protobuf/types/httppb",
                    "--go_opt=Mkratos/conf/database.proto=github.com/aesoper101/kratos-utils/protobuf/types/confpb",
                    "--go_opt=Mkratos/conf/logger.proto=github.com/aesoper101/kratos-utils/protobuf/types/confpb",
                    "--go_opt=Mkratos/conf/registry.proto=github.com/aesoper101/kratos-utils/protobuf/types/confpb",
                    "--go_opt=Mkratos/conf/other.proto=github.com/aesoper101/kratos-utils/protobuf/types/confpb",
                    "--go_opt=Mkratos/conf/server.proto=github.com/aesoper101/kratos-utils/protobuf/types/confpb",
                    "./${filePath}"
            )

            GoExecutor.`in`(module).withExePath("protoc")
                    .withParameters(params)
                    .withWorkDirectory(projectDirectory.path)
                    .withPresentableName(KratosBundle.message("action.conf2go.description"))
                    .executeWithOutput {
                        when (it.status) {
                            GoExecutor.ExecutionResult.Status.SUCCEEDED -> {
                                VfsUtil.markDirtyAndRefresh(
                                        true, true, true, projectDirectory
                                )
                            }

                            else -> {
                                it.message?.let { it1 ->
                                    NotificationManager.getInstance().createNotification().error(project,
                                            it1
                                    )
                                }
                            }
                        }

                    }
        }


    }

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)

        when {
            file == null || file.isDirectory || file.extension != "proto" -> {
                e.presentation.isVisible = false
                e.presentation.isEnabled = false
            }
        }
    }
}