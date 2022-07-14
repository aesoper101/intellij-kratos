package com.github.aesoper101.intellij.kratos.configuration

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

interface KratosTemplateConfiguration {
    var templateType: TemplateType
    var kratosVersion: String
    var kratosCmdPath: String
}

data class KratosTemplateConfigurationState(
    override var templateType: TemplateType = TemplateType.SINGLE,
    override var kratosVersion: String = "1.0.0",
    override var kratosCmdPath: String = ""
) :
    KratosTemplateConfiguration

@State(name = "KratosPlugin", storages = [Storage("kratosTemplateSettings.xml")])
@Service(Service.Level.PROJECT)
internal class KratosTemplateConfigurationService: PersistentStateComponent<KratosTemplateConfigurationState> {
    var myState = KratosTemplateConfigurationState()

    fun getInstance(): KratosTemplateConfigurationService {
        return ApplicationManager.getApplication().getService(KratosTemplateConfigurationService::class.java)
    }

    override fun getState(): KratosTemplateConfigurationState {
       return myState
    }

    override fun loadState(state: KratosTemplateConfigurationState) {
        XmlSerializerUtil.copyBean(state, myState)
    }

}