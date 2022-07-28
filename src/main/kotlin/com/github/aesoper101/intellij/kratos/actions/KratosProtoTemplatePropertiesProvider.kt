package com.github.aesoper101.intellij.kratos.actions

import com.goide.psi.impl.GoPackage
import com.goide.psi.impl.imports.GoImportPathUtil
import com.goide.sdk.GoPackageUtil
import com.goide.util.GoUtil
import com.goide.vgo.VgoUtil
import com.goide.vgo.project.VgoImportPathsProvider
import com.intellij.ide.fileTemplates.DefaultTemplatePropertiesProvider
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager
import java.io.File
import java.util.*

const val KRATOS_PROTO_GO_MODULE_NAME = "KRATOS_PROTO_GO_" + FileTemplate.ATTRIBUTE_PACKAGE_NAME


class KratosProtoTemplatePropertiesProvider : DefaultTemplatePropertiesProvider {

    override fun fillProperties(directory: PsiDirectory, props: Properties) {
        val goModFile = LocalFileSystem.getInstance().findFileByIoFile(File(directory.project.basePath + "/go.mod"))!!
        val goModuleName = GoUtil.getModuleName(goModFile)

        props.setProperty(KRATOS_PROTO_GO_MODULE_NAME, goModuleName)
    }
}