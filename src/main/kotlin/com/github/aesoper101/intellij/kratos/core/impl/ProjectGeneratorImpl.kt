package com.github.aesoper101.intellij.kratos.core.impl

import com.github.aesoper101.intellij.kratos.KratosConstants.KRATOS_THIRD_PARTY_TEMPLATES
import com.github.aesoper101.intellij.kratos.core.AbstractProjectGenerator
import com.github.aesoper101.intellij.kratos.project.KratosNewProjectSettings
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.module.Module


class ProjectGeneratorImpl(module: Module, settings: KratosNewProjectSettings) :
    AbstractProjectGenerator(module, settings) {


    override fun generateProjectThirdParty(
        projectDirectory: VirtualFile
    ) {
        KRATOS_THIRD_PARTY_TEMPLATES.forEach {
            val template = FileTemplateManager.getInstance(module.project).getJ2eeTemplate(it.fileTemplateName)

            VfsUtil.createDirectoryIfMissing(projectDirectory, it.generatePath)
                .createChildData(null, it.generateName)
                .setBinaryContent(template.text.toByteArray())
        }
    }


    override fun generateApplication(
        projectDirectory: VirtualFile,
        appName: String
    ) {
        println("generateApplication")
        ApplicationGeneratorImpl(module, settings).doGenerator(projectDirectory, appName)
    }

    override fun generateDeploy() {
        println("generateDeploy")
    }

    override fun generatePkg() {
        println("generatePkg")
    }
}