package com.github.aesoper101.intellij.kratos.core

import freemarker.template.Configuration
import freemarker.template.Template
import java.io.IOException


class FreemarkerConfiguration(basePackagePath: String) : Configuration(
    DEFAULT_INCOMPATIBLE_IMPROVEMENTS
) {
    constructor() : this("/templates") {}

    init {
        defaultEncoding = "UTF-8"
        setClassForTemplateLoading(javaClass, basePackagePath)
    }

    @Throws(IOException::class)
    override fun getTemplate(ftl: String): Template {
        return this.getTemplate(ftl, "UTF-8")
    }
}