package com.github.aesoper101.intellij.kratos.builder

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.openapi.vfs.VirtualFile
import java.util.*

interface BuilderInterface {
    fun build(): VirtualFile?
}


open class Builder : BuilderInterface {

    private fun getInternalTemplate(templateName: String, properties: Properties = Properties()): String {
        val template = FileTemplateManager
            .getDefaultInstance()
            .getInternalTemplate(templateName)

        return if (properties.isEmpty) {
            template.text
        } else {
            template.getText(properties)
        }
    }

    override fun build(): VirtualFile? {
        return null
    }
}