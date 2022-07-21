package com.github.aesoper101.intellij.kratos.project

import com.github.aesoper101.intellij.kratos.configuration.KratosConfiguration
import com.goide.sdk.GoSdk
import com.goide.vgo.wizard.VgoNewProjectSettings
import com.goide.wizard.GoNewProjectSettings

data class KratosNewProjectSettings(
    var goSdk: GoSdk,
    var envs: Map<String, String> = emptyMap(),
    var kratosSettings: KratosConfiguration,
    var moduleName: String,
    var appName: String,
//    var appFirstServiceName: String
) : VgoNewProjectSettings(goSdk, envs, true, false)
