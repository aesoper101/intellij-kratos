package com.github.aesoper101.intellij.kratos.core

import com.intellij.openapi.vfs.VirtualFile

internal data class GeneratorAssetContext(
    val assets: List<GeneratorAsset>,
    val outputDirectory: VirtualFile
)