package com.github.aesoper101.intellij.kratos.actions

import com.github.aesoper101.intellij.kratos.utils.toCamelCase
import com.intellij.ide.fileTemplates.DefaultCreateFromTemplateHandler;
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.openapi.project.Project

class KratosProtoCreateFromTemplateHandler: DefaultCreateFromTemplateHandler() {
    override fun handlesTemplate(template: FileTemplate): Boolean {
       return template.extension == "proto"
    }

    override fun prepareProperties(props: MutableMap<String, Any>) {
        val goPackageName = props[KRATOS_PROTO_GO_PACKAGE_NAME]
        val protoPackageName = props[KRATOS_PROTO_PROTO_PACKAGE_NAME]
        val filename = props[FileTemplate.ATTRIBUTE_FILE_NAME]
        if (goPackageName is String && protoPackageName is String && filename is String) {
            props["GoPackageName"] = goPackageName
            props["ProtoPackageName"] = protoPackageName
            props["JavaPackageName"] = protoPackageName
            props["ServiceName"] = filename.substringBefore(".").toCamelCase()
        }
    }
}