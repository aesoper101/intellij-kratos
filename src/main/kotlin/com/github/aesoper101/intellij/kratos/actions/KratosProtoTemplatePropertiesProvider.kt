package com.github.aesoper101.intellij.kratos.actions

import com.goide.actions.file.GoTemplatePropertiesProvider
import com.goide.psi.impl.GoPackage
import com.goide.psi.impl.imports.GoImportPathUtil
import com.goide.sdk.GoPackageUtil
import com.goide.sdk.GoSdkUtil
import com.goide.util.GoUtil
import com.goide.vgo.VgoUtil
import com.goide.vgo.project.VgoImportPathsProvider
import com.intellij.ide.fileTemplates.DefaultTemplatePropertiesProvider
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager
import java.io.File
import java.util.*

const val KRATOS_PROTO_GO_MODULE_NAME = "KRATOS_PROTO_GO_" + FileTemplate.ATTRIBUTE_PACKAGE_NAME


class KratosProtoTemplatePropertiesProvider : GoTemplatePropertiesProvider() {

    override fun fillProperties(directory: PsiDirectory, props: Properties) {
        val goModFile = VgoUtil.findModuleRoot(directory.virtualFile)?.findChild("go.mod") ?: LocalFileSystem.getInstance()
            .findFileByIoFile(File(directory.project.basePath + "/go.mod"))!!
        val goModuleName = GoUtil.getModuleName(goModFile)

        if (goModuleName != null) {
            props.setProperty(KRATOS_PROTO_GO_MODULE_NAME, goModuleName)
        }

    }
}