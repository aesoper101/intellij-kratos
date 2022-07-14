package com.github.aesoper101.intellij.kratos.configuration

import com.goide.util.GoUtil
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project


interface KratosConfiguration : KratosTemplateConfiguration {
    fun deepCopy(): KratosConfiguration

    fun saveState(project: Project)
    fun modified(project: Project): Boolean

    val isMultipartTemplate: Boolean

    companion object {
        fun getInstance(p: Project): KratosConfiguration = KratosConfigurationState(p).deepCopy()

        fun getInstance(): KratosConfiguration = KratosConfigurationState()
    }
}


internal data class KratosConfigurationState(private val templateConfig: KratosTemplateConfigurationState = KratosTemplateConfigurationState()) :
    KratosConfiguration, KratosTemplateConfiguration by templateConfig {
    constructor(project: Project) : this(
        templateConfig = project.service<KratosTemplateConfigurationService>().myState
    )

    override fun deepCopy(): KratosConfigurationState {
        val templateConfigurationCopy = templateConfig.copy()
        return KratosConfigurationState(
            templateConfig = templateConfigurationCopy
        )
    }

    override fun saveState(project: Project) {
        GoUtil.cleanResolveCache(project)
        project.service<KratosTemplateConfigurationService>().myState = templateConfig.copy()
    }

    override fun modified(project: Project): Boolean {
        val currentSettings = KratosConfigurationState(project)
        return currentSettings.templateConfig != templateConfig
    }

    override val isMultipartTemplate: Boolean
        get() = templateConfig.templateType == TemplateType.MULTIPART
}


interface ConfigurationProvider<out Configuration> {
    val kratosSettings: Configuration
}