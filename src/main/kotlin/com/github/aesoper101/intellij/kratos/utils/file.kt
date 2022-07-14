package com.github.aesoper101.intellij.kratos.utils

import com.github.aesoper101.intellij.kratos.notification.NotificationManager
import com.intellij.ide.fileTemplates.FileTemplateManager
import java.io.File
import java.util.*

fun File.dir(name: String, body: File.() -> Unit = {}) {
    val file = File(this, name)
    if (!file.exists() || !file.isDirectory) {
        file.mkdirs()
    }

    file.body()
}



