package com.github.aesoper101.intellij.kratos.actions

import com.github.aesoper101.intellij.kratos.KratosBundle
import com.github.aesoper101.intellij.kratos.configuration.KratosConfiguration
import com.github.aesoper101.intellij.kratos.core.builder.application.ApplicationGenerator
import com.intellij.ide.actions.CreateFileAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiUtilCore
import java.io.File

class NewApplicationAction : CreateFileAction(
    KratosBundle.message("action.newApp.name"),
    KratosBundle.message("action.newApp.description"),
    null
) {
    override fun create(newName: String, directory: PsiDirectory): Array<PsiElement> {
        return WriteAction.compute<Array<PsiElement>, RuntimeException> {
            ApplicationGenerator(directory, newName).doGenerate()

            directory.findSubdirectory(newName)?.children
        }
    }

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        val project = e.project ?: return

        when {
            !KratosConfiguration.getInstance(project).isMultipartTemplate || !file.isDirectory || file.name != "app" -> {
                e.presentation.isEnabled = false
                e.presentation.isVisible = false
            }
        }
    }
}