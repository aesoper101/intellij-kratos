package com.github.aesoper101.intellij.kratos

import com.github.aesoper101.intellij.kratos.KratosConstants.KRATOS_BIZ_TEMPLATES
import com.github.aesoper101.intellij.kratos.KratosConstants.KRATOS_CONF_PROTO_TEMPLATES
import com.github.aesoper101.intellij.kratos.KratosConstants.KRATOS_THIRD_PARTY_TEMPLATES
import com.goide.GoIcons
import com.intellij.ide.fileTemplates.FileTemplateDescriptor
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory


final class KratosFileTemplateGroupDescriptorFactory : FileTemplateGroupDescriptorFactory {

    override fun getFileTemplatesDescriptor(): FileTemplateGroupDescriptor {
        val rootGroup = FileTemplateGroupDescriptor("kratos", GoIcons.ICON)


        KRATOS_THIRD_PARTY_TEMPLATES.forEach {
            rootGroup.addTemplate(FileTemplateDescriptor(
                it.fileTemplateName,
                GoIcons.ICON
            ))
        }


        KRATOS_CONF_PROTO_TEMPLATES.forEach {
            rootGroup.addTemplate(FileTemplateDescriptor(
                it.fileTemplateName,
                GoIcons.ICON
            ))
        }

        KRATOS_BIZ_TEMPLATES.forEach {
            rootGroup.addTemplate(FileTemplateDescriptor(
                it.fileTemplateName,
                GoIcons.ICON
            ))
        }


        return rootGroup
    }
}


