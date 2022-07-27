package com.github.aesoper101.intellij.kratos.wizard


import com.github.aesoper101.intellij.kratos.KratosBundle
import com.github.aesoper101.intellij.kratos.configuration.ConfigurationProvider
import com.github.aesoper101.intellij.kratos.configuration.KratosConfiguration
import com.github.aesoper101.intellij.kratos.configuration.TemplateType
import com.github.aesoper101.intellij.kratos.project.KratosNewProjectSettings
import com.goide.sdk.combobox.GoSdkChooserCombo
import com.goide.vgo.configuration.VgoEnvironmentVariablesField
import com.goide.wizard.GoProjectGeneratorPeer
import com.intellij.openapi.Disposable
import com.intellij.openapi.observable.properties.ObservableMutableProperty
import com.intellij.openapi.observable.properties.PropertyGraph
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.util.text.StringUtil

import com.intellij.ui.dsl.builder.*
import com.intellij.util.ui.UI.PanelFactory


import javax.swing.*

// see  vgoProjectGeneratorPeer
class KratosProjectGeneratorPeer : GoProjectGeneratorPeer<KratosNewProjectSettings>(),
    ConfigurationProvider<KratosConfiguration> {

    private val myEnvironmentField = VgoEnvironmentVariablesField(false)




    override val kratosSettings: KratosConfiguration = KratosConfiguration.getInstance()

    override fun createSettingsPanel(
        p0: Disposable,
        p1: LabeledComponent<TextFieldWithBrowseButton>?,
        p2: GoSdkChooserCombo?,
        p3: Project?
    ): JPanel {
        val panel = JPanel()
        val gird = createGridPanel(p1, p2)

        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)



        gird.add(PanelFactory.panel(myEnvironmentField).withLabel(KratosBundle.message("module.builder.generator.peer.env")))



        gird.add(PanelFactory.panel(createTemplateSwitchPanel()).withLabel(KratosBundle.message("module.builder.type")))

        panel.add(gird.resize().createPanel())

        return panel
    }


    override fun getSettings(): KratosNewProjectSettings {

        return KratosNewProjectSettings(
            sdkFromCombo,
            myEnvironmentField.envs,
            kratosSettings,
        )
    }

    private fun createTemplateSwitchPanel(): DialogPanel {
        val typeProperty: ObservableMutableProperty<TemplateType> = PropertyGraph().property(TemplateType.SINGLE)

        typeProperty.afterChange { kratosSettings.templateType = it }

        return panel {
            row {
                segmentedButton(
                    listOf(
                        TemplateType.SINGLE,
                        TemplateType.MULTIPART
                    )
                ) { it.toString() }.bind(typeProperty)
            }.layout(RowLayout.LABEL_ALIGNED)
        }
    }


}


