package com.github.aesoper101.intellij.kratos.actions

import com.goide.sdk.GoPackageUtil
import com.goide.util.GoUtil
import com.goide.vgo.VgoUtil
import com.intellij.ide.fileTemplates.DefaultTemplatePropertiesProvider
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager
import java.io.File
import java.util.*

const val KRATOS_PROTO_GO_PACKAGE_NAME = "KRATOS_PROTO_GO_" + FileTemplate.ATTRIBUTE_PACKAGE_NAME
const val KRATOS_PROTO_PROTO_PACKAGE_NAME = "KRATOS_PROTO_PROTO_" + FileTemplate.ATTRIBUTE_PACKAGE_NAME
const val KRATOS_PROTO_JAVA_PACKAGE_NAME = "KRATOS_PROTO_JAVA_" + FileTemplate.ATTRIBUTE_PACKAGE_NAME


class KratosProtoTemplatePropertiesProvider : DefaultTemplatePropertiesProvider {

    override fun fillProperties(directory: PsiDirectory, props: Properties) {
        val isVendoringMode = VgoUtil.isVendoringMode(directory.project, null)
        val fileGoImportPath = GoPackageUtil.findFirstImportPath(directory, isVendoringMode)

        val projectDirectory = directory.project.basePath
        val projectFile = LocalFileSystem.getInstance().findFileByIoFile(File(projectDirectory!!))

        val modPath = GoPackageUtil.findFirstImportPath(
            PsiManager.getInstance(directory.project).findDirectory(projectFile!!),
            isVendoringMode
        )

        val protoPackageName =
            fileGoImportPath.substringAfter(modPath).substringAfter("/").replace("/", ".").lowercase()

        props.setProperty(
            KRATOS_PROTO_GO_PACKAGE_NAME,
            fileGoImportPath + ";" + fileGoImportPath.substringAfterLast("/").lowercase()
        )
        props.setProperty(KRATOS_PROTO_PROTO_PACKAGE_NAME, protoPackageName)
        props.setProperty(KRATOS_PROTO_JAVA_PACKAGE_NAME, protoPackageName)
    }
}