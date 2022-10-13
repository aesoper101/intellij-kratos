package com.github.aesoper101.intellij.kratos.actions

import com.github.aesoper101.intellij.kratos.KratosBundle
import com.github.aesoper101.intellij.kratos.KratosConstants
import com.goide.util.GoGetPackageUtil
import com.intellij.execution.runners.RunTab.ToolbarActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys


class KratosUpgradeAction : AnAction(KratosBundle.message("action.upgrade.name"), KratosBundle.message("action.upgrade.description"), null) {

    private val deps = listOf(
            "google.golang.org/protobuf/cmd/protoc-gen-go@latest",
            "google.golang.org/grpc/cmd/protoc-gen-go-grpc@latest",
            "github.com/go-kratos/kratos/cmd/kratos/v2@latest",
            "github.com/go-kratos/kratos/cmd/protoc-gen-go-http/v2@latest",
            "github.com/google/gnostic/cmd/protoc-gen-openapi@latest",
            "entgo.io/ent/cmd/ent@latest",
            "github.com/google/wire/cmd/wire@latest",
            "github.com/envoyproxy/protoc-gen-validate@latest",
            "github.com/go-kratos/kratos/cmd/protoc-gen-go-errors/v2@latest"
    )


    override fun actionPerformed(e: AnActionEvent) {
        val module = e.getData(PlatformDataKeys.MODULE) ?: return

        deps.forEach {
            println(it)
            GoGetPackageUtil.installTool(
                    module.project,
                    module,
                    module.project.presentableUrl,
                    it,
                    true,
            )
        }

    }

}
