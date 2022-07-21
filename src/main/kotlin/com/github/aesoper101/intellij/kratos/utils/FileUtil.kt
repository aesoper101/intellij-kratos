package com.github.aesoper101.intellij.kratos.utils

import com.intellij.openapi.vfs.VirtualFile
import java.io.InputStream
import java.io.InputStreamReader
import java.io.LineNumberReader
import java.nio.charset.StandardCharsets

class FileUtil {

    fun read(file: VirtualFile): String? {
        if (file.isDirectory) {
            return null
        }

        val inputStream = file.inputStream
        return try {
            val reader = InputStreamReader(inputStream, StandardCharsets.UTF_8)
            val line = LineNumberReader(reader)
            val buffer = StringBuilder()
            var str: String?
            while (line.readLine().also { str = it } != null) {
                buffer.append(str).append("\r\n")
            }
            buffer.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}