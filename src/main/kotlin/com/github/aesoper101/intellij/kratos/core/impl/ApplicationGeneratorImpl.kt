package com.github.aesoper101.intellij.kratos.core.impl

import com.github.aesoper101.intellij.kratos.KratosConstants.KRATOS_BIZ_LOGIC_TEMPLATE_NAME
import com.github.aesoper101.intellij.kratos.KratosConstants.KRATOS_BIZ_TEMPLATES
import com.github.aesoper101.intellij.kratos.KratosConstants.KRATOS_CONFIGS_YAML_TEMPLATES
import com.github.aesoper101.intellij.kratos.KratosConstants.KRATOS_CONF_PROTO_TEMPLATES
import com.github.aesoper101.intellij.kratos.core.AbstractApplicationGenerator
import com.github.aesoper101.intellij.kratos.project.KratosNewProjectSettings
import com.github.aesoper101.intellij.kratos.utils.toCamelCase
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.module.Module
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.VfsUtil
import java.util.*


class ApplicationGeneratorImpl(module: Module, settings: KratosNewProjectSettings) :
    AbstractApplicationGenerator(module, settings) {
    override fun generateCmd(applicationDirectory: VirtualFile) {
        println("generateCmd")
    }

    override fun generateInternal(applicationDirectory: VirtualFile) {
        generateConfProto(applicationDirectory)

        generateBiz(applicationDirectory)
    }

    override fun generateConfigs(applicationDirectory: VirtualFile) {
        KRATOS_CONFIGS_YAML_TEMPLATES.forEach {
            val template = FileTemplateManager.getInstance(module.project).getJ2eeTemplate(it.fileTemplateName)

            VfsUtil.createDirectoryIfMissing(applicationDirectory, it.generatePath)
                .createChildData(null, it.generateName)
                .setBinaryContent(template.text.toByteArray())
        }
    }

    private fun generateConfProto(applicationDirectory: VirtualFile) {
        KRATOS_CONF_PROTO_TEMPLATES.forEach {
            val template = FileTemplateManager.getInstance(module.project).getJ2eeTemplate(it.fileTemplateName)

            VfsUtil.createDirectoryIfMissing(applicationDirectory, it.generatePath)
                .createChildData(null, it.generateName)
                .setBinaryContent(template.text.toByteArray())
        }

        // todo: run configure proto to go
    }

    private fun generateBiz(applicationDirectory: VirtualFile) {
        KRATOS_BIZ_TEMPLATES.forEach {
            val template = FileTemplateManager.getInstance(module.project).getJ2eeTemplate(it.fileTemplateName)

            val props = Properties()
            props.setProperty("NAME", settings.appFirstServiceName.toCamelCase())

            VfsUtil.createDirectoryIfMissing(applicationDirectory, it.generatePath)
                .createChildData(
                    null, if (it.fileTemplateName == KRATOS_BIZ_LOGIC_TEMPLATE_NAME) {
                        settings.appFirstServiceName + ".go"
                    } else {
                        it.generateName
                    }
                )
                .setBinaryContent(
                    template.getText(props).toByteArray()
                )
        }

        // todo: run configure proto to go
    }

}