package com.github.aesoper101.intellij.kratos.core.builder.project

import com.github.aesoper101.intellij.kratos.configuration.TemplateType
import com.github.aesoper101.intellij.kratos.core.builder.project.impl.MultipartAppProjectBuilder
import com.github.aesoper101.intellij.kratos.core.builder.project.impl.SingleAppProjectBuilder
import com.github.aesoper101.intellij.kratos.project.KratosNewProjectSettings
import com.goide.vgo.configuration.VgoProjectSettings
import com.goide.vgo.configuration.VgoSettings

import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.module.Module

class ProjectGenerator(private val module: Module, private val settings: KratosNewProjectSettings) {

    fun doGenerator(
        isCreatingNewProject: Boolean,
        contentRoot: String?
    ) {
        if (!StringUtil.isEmpty(contentRoot)) {

            when (settings.kratosSettings.templateType) {
                TemplateType.SINGLE -> {
                    SingleAppProjectBuilder(module, settings, contentRoot!!).doGenerate()
                }
                TemplateType.MULTIPART -> {
                    MultipartAppProjectBuilder(module, settings, contentRoot!!).doGenerate()
                }
            }
        }


        if (isCreatingNewProject || !VgoProjectSettings.getInstance(module.project).isIntegrationEnabled) {
            val vgoProjectSettings = VgoProjectSettings.getInstance(module.project)
            vgoProjectSettings.isIntegrationEnabled = true
            vgoProjectSettings.isAutoVendoringMode = settings.vendoringMode
            vgoProjectSettings.environment = settings.environment
            VgoSettings.getInstance().addEnvironmentVars(settings.environment)
        }

        settings.kratosSettings.saveState(module.project)
    }
}