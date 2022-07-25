package com.github.aesoper101.intellij.kratos


import com.goide.GoIcons
import com.intellij.ide.fileTemplates.FileTemplateDescriptor
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory


final class KratosFileTemplateGroupDescriptorFactory : FileTemplateGroupDescriptorFactory {

    override fun getFileTemplatesDescriptor(): FileTemplateGroupDescriptor {
        val rootGroup = FileTemplateGroupDescriptor("kratos", GoIcons.ICON)

//        rootGroup.addTemplate(FileTemplateDescriptor(".dockerfile"))
//        rootGroup.addTemplate(FileTemplateDescriptor(".makefile"))

        return rootGroup
    }
}


