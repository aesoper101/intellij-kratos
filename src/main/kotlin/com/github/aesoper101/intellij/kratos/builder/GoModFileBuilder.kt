package com.github.aesoper101.intellij.kratos.builder

import com.goide.psi.impl.GoPackage
import com.goide.sdk.GoSdkService
import com.goide.sdk.GoSdkVersion
import com.goide.vendor.GoVendoringUtil
import com.intellij.openapi.module.Module
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.util.ObjectUtils
import java.nio.charset.StandardCharsets

class GoModFileBuilder(
    private var contentRootVirtualFileDirectory: VirtualFile,
    private val modName: String = "",
    private val module: Module? = null,
) : Builder() {

    override fun build(): VirtualFile? {
        if (module == null) return null

        val goModFile: VirtualFile = contentRootVirtualFileDirectory.createChildData(module, "go.mod")
        val contentRootPsiDirectory =
            PsiManager.getInstance(module.project).findDirectory(contentRootVirtualFileDirectory)

        val moduleName =
            if (StringUtil.isNotEmpty(modName)) modName else if (contentRootPsiDirectory != null) GoPackage.`in`(
                contentRootPsiDirectory,
                ""
            )
                .getImportPath(GoVendoringUtil.isVendoringEnabled(module)) else null

        val content = StringBuilder()
        content.append("module ").append(
            ObjectUtils.notNull(moduleName, contentRootVirtualFileDirectory.name).replace(' ', '_')
        ).append('\n')
        val sdk = GoSdkService.getInstance(module.project).getSdk(module)
        val sdkVersion = sdk.majorVersion
        if (sdkVersion != GoSdkVersion.UNKNOWN && sdkVersion != GoSdkVersion.GO_DEVEL) {
            content.append("\ngo ").append(sdkVersion).append('\n')
        }

        goModFile.setBinaryContent(content.toString().toByteArray(StandardCharsets.UTF_8))

        return goModFile
    }
}