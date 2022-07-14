package com.github.aesoper101.intellij.kratos.diagnostic

import com.intellij.openapi.diagnostic.ErrorReportSubmitter
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.diagnostic.SubmittedReportInfo
import com.intellij.util.Consumer
import java.awt.Component

class KratosErrorReportSubmitter: ErrorReportSubmitter() {
    override fun getReportActionText(): String {
        return "Report to Aesoper101"
    }

    override fun submit(
        events: Array<out IdeaLoggingEvent>,
        additionalInfo: String?,
        parentComponent: Component,
        consumer: Consumer<in SubmittedReportInfo>
    ): Boolean {
        println("submit")
        return true
    }
}