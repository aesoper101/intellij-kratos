package com.github.aesoper101.intellij.kratos.configuration

enum class TemplateType(private val cmd: String) {
    SINGLE("Single"),
    MULTIPART("Multipart");
    override fun toString(): String = cmd
}

