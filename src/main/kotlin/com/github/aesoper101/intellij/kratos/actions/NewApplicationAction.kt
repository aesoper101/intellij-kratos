package com.github.aesoper101.intellij.kratos.actions

import com.github.aesoper101.intellij.kratos.KratosBundle
import com.github.aesoper101.intellij.kratos.core.builder.application.ApplicationGenerator
import com.intellij.ide.ui.newItemPopup.NewItemPopupUtil
import com.intellij.ide.ui.newItemPopup.NewItemSimplePopupPanel
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.util.text.StringUtil



class NewApplicationAction : AnAction(
    KratosBundle.message("action.newApp.name"),
    KratosBundle.message("action.newApp.description"),
    null
) {


    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
//        val view = e.getData(LangDataKeys.IDE_VIEW) ?: return
        val directory = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        val module = e.getData(PlatformDataKeys.MODULE) ?: return
        val contentPanel = NewItemSimplePopupPanel()
        val nameField = contentPanel.textField


        val popup =
            NewItemPopupUtil.createNewItemPopup(KratosBundle.message("action.kratos.new.name"), contentPanel, nameField)

        contentPanel.setApplyAction { event ->
            val name = nameField.text
            val validator = ApplicationNameInputValidator()
            if (validator.checkInput(name) && validator.canClose(name)) {
                when (directory.findChild(name)) {
                    null -> {
                        popup.closeOk(event)

                        WriteCommandAction.runWriteCommandAction(project) {
                            ApplicationGenerator(module, directory, name).doGenerate()
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

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

        when {
            !file.isDirectory || file.findChild("app") == null -> {
                e.presentation.isEnabled = false
                e.presentation.isVisible = false
            }
        }
    }

}

private class ApplicationNameInputValidator : InputValidator {
    override fun checkInput(inputString: String?): Boolean {
        return StringUtil.isNotEmpty(inputString)
    }

    override fun canClose(inputString: String?): Boolean {
        return true
    }

}