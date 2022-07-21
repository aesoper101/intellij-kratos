package com.github.aesoper101.intellij.kratos.core

import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.openapi.vfs.VirtualFile
import java.net.URL
import java.util.Properties

sealed class GeneratorAsset {
    abstract val targetFileOrDirName: String
}


enum class TemplateCategory(private val cmd: String) {
    J2EE(FileTemplateManager.J2EE_TEMPLATES_CATEGORY),
    Code(FileTemplateManager.CODE_TEMPLATES_CATEGORY),
    Default(FileTemplateManager.DEFAULT_TEMPLATES_CATEGORY),
    Internal(FileTemplateManager.INTERNAL_TEMPLATES_CATEGORY),
    Includes(FileTemplateManager.INTERNAL_TEMPLATES_CATEGORY);

    override fun toString(): String = cmd
}


data class GeneratorTemplateFile(
    override val targetFileOrDirName: String,
    val templateName: String,
    val properties: Map<String, Any?> = emptyMap(),
    val templateCategory: TemplateCategory = TemplateCategory.J2EE,
    val evt: GenerateEvent? = null
) : GeneratorAsset()

data class GeneratorResource(
    override val targetFileOrDirName: String,
    val resource: URL,
) : GeneratorAsset()

data class GeneratorEmptyDirectory(
    override val targetFileOrDirName: String
) : GeneratorAsset()

