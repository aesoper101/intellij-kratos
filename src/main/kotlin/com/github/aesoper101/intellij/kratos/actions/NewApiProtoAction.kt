package com.github.aesoper101.intellij.kratos.actions

import com.github.aesoper101.intellij.kratos.KratosBundle
import com.goide.psi.GoImportSpec
import com.goide.psi.impl.GoPackage
import com.goide.psi.impl.GoPsiImplUtil
import com.goide.psi.impl.GoPsiImplUtil.getPath
import com.goide.psi.impl.imports.GoImportPathUtil
import com.goide.sdk.GoPackageUtil
import com.goide.sdk.GoSdkUtil
import com.goide.util.GoUtil
import com.goide.vgo.VgoUtil
import com.goide.vgo.mod.psi.impl.VgoPsiImplUtil
import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiDirectoryContainer
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager

import java.io.File

private val NEW_API_PROTO_ACTION_NAME = KratosBundle.message("action.newApiProto.name")
private val NEW_API_PROTO_ACTION_DESCRIPTION = KratosBundle.message("action.newApiProto.description")


@Suppress("PrivatePropertyName")
class NewApiProtoAction : CreateFileFromTemplateAction(
    NEW_API_PROTO_ACTION_NAME,
    NEW_API_PROTO_ACTION_DESCRIPTION,
    null
), DumbAware {
    private val FILE_TEMPLATE = "Kratos Empty Api"
    private val FILE_DEMO_TEMPLATE = "Kratos Demo Api"
    private val ERROR_TEMPLATE = "Kratos Error"

    @Suppress("DialogTitleCapitalization")
    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle(NEW_API_PROTO_ACTION_NAME)
            .addKind("Empty Api Proto", null, FILE_TEMPLATE)
            .addKind("Api Error Proto", null, ERROR_TEMPLATE)
            .addKind("Api Proto", null, FILE_DEMO_TEMPLATE);
    }

    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String {
        return NEW_API_PROTO_ACTION_NAME
    }


    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        val project = e.project ?: return
        val projectDirectory = LocalFileSystem.getInstance().findFileByIoFile(File(project.presentableUrl!!))!!
        val apiDir = projectDirectory.findChild("api")

        when {
            file == null || !file.isDirectory || apiDir == null || !apiDir.isDirectory || !VfsUtil.isAncestor(
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
