package com.github.aesoper101.intellij.kratos.actions

import com.github.aesoper101.intellij.kratos.KratosBundle
import com.github.aesoper101.intellij.kratos.configuration.TemplateType
import com.github.aesoper101.intellij.kratos.core.builder.project.ProjectGenerator
import com.github.aesoper101.intellij.kratos.project.KratosNewProjectSettings
import com.goide.sdk.GoSdkService

import com.goide.vgo.VgoUtil
import com.goide.vgo.configuration.VgoProjectSettings
import com.intellij.ide.ui.newItemPopup.NewItemPopupUtil
import com.intellij.ide.ui.newItemPopup.NewItemSimplePopupPanel
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.Icon

open class KratosNewBaseAction(text: String, description: String, icon: Icon?) :
    AnAction(text, description, icon) {


    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val module = e.getData(PlatformDataKeys.MODULE) ?: return
        val directory = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        val contentPanel = NewItemSimplePopupPanel()
        val nameField = contentPanel.textField


        val popup =
            NewItemPopupUtil.createNewItemPopup(KratosBundle.message("action.kratos.new.name"), contentPanel, nameField)

        contentPanel.setApplyAction { event ->
            val name = nameField.text
            val validator = ProjectNameInputValidator()
            if (validator.checkInput(name) && validator.canClose(name)) {
                when (directory.findChild(name)) {
                    null -> {
                        popup.closeOk(event)

                        WriteCommandAction.runWriteCommandAction(project) {
                            doGenerate(project, module, VfsUtil.createDirectoryIfMissing(directory, name))
                        }
                    }
                    else -> {
                        contentPanel.setError(KratosBundle.message("action.kratos.new.application.exists", name))
                    }
                }
            }
        }

        popup.showCenteredInCurrentWindow(project)
    }


    open fun getTemplateType(): TemplateType = TemplateType.SINGLE

    private fun doGenerate(project: Project, module: Module, directory: VirtualFile) {
        val state = VgoProjectSettings.getInstance(project)
        val sdk = GoSdkService.getInstance(project).getSdk(module)
        val settings = KratosNewProjectSettings(sdk, state.environment, getTemplateType())
        ProjectGenerator(module, settings).doGenerator(true, directory.path, true)
    }

}

private class ProjectNameInputValidator : InputValidator {
    override fun checkInput(inputString: String?): Boolean {
        return StringUtil.isNotEmpty(inputString)
    }

    override fun canClose(inputString: String?): Boolean {
        return true
    }
}

class KratosNewSingleApplicationAction : KratosNewBaseAction(
    KratosBundle.message("action.kratos.new.single.name"),
    KratosBundle.message("action.kratos.new.single.description"),
    null
) {
    override fun getTemplateType(): TemplateType {
        return TemplateType.SINGLE
    }
}

class KratosNewMultipartApplicationAction : KratosNewBaseAction(
    KratosBundle.message("action.kratos.new.multipart.name"),
    KratosBundle.message("action.kratos.new.multipart.description"),
    null
) {
    override fun getTemplateType(): TemplateType {
        return TemplateType.MULTIPART
    }
}

class KratosNewGroupAction : ActionGroup(KratosBundle.message("action.kratos.new.name"), true) {
    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        return arrayOf(
            KratosNewSingleApplicationAction(),
            KratosNewMultipartApplicationAction(),
        )
    }

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        if (e.project == null || !isVgoWorkInner(file)) {
            e.presentation.isVisible = false
            e.presentation.isEnabled = false
        }
    }

    private fun isVgoWorkInner(directory: VirtualFile): Boolean {
        val workFile = directory.findChild(VgoUtil.GO_WORK)
        return workFile != null && !workFile.isDirectory
    }
}