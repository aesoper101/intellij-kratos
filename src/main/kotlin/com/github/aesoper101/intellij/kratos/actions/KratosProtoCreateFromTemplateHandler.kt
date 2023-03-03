package com.github.aesoper101.intellij.kratos.actions

import com.github.aesoper101.intellij.kratos.utils.FileUtil
import com.github.aesoper101.intellij.kratos.utils.toCamelCase
import com.github.aesoper101.intellij.kratos.utils.toSnakeCase
import com.goide.actions.file.GoCreateFromTemplateHandler
import com.goide.sdk.GoPackageUtil
import com.goide.vgo.VgoUtil
import com.intellij.ide.fileTemplates.DefaultCreateFromTemplateHandler;
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.openapi.project.Project
import kotlin.io.path.Path

class KratosProtoCreateFromTemplateHandler : DefaultCreateFromTemplateHandler() {
    override fun handlesTemplate(template: FileTemplate): Boolean {
        return template.extension == "proto"
    }


    override fun prepareProperties(props: MutableMap<String, Any>) {
        val goPackageName = props[KRATOS_PROTO_GO_MODULE_NAME]

        val filename = props[FileTemplate.ATTRIBUTE_FILE_NAME]
        val dirPath = props[FileTemplate.ATTRIBUTE_DIR_PATH]

        if (goPackageName is String && dirPath is String && filename is String) {
            props["GoPackageName"] =
                "$goPackageName/$dirPath;" + dirPath.substringAfterLast("/").toSnakeCase()
            props["ProtoPackageName"] = dirPath.replace("/", ".").toSnakeCase()
            props["JavaPackageName"] = dirPath.replace("/", ".").replace("-", "_").toSnakeCase()
            props["ServiceName"] = filename.substringBefore(".").toCamelCase()
        }
    }
}