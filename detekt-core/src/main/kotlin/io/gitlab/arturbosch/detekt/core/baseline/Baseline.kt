package io.gitlab.arturbosch.detekt.core.baseline

import io.gitlab.arturbosch.detekt.api.Finding

internal typealias FindingsIdList = Set<String>

internal data class Baseline(val blacklist: FindingsIdList, val whitelist: FindingsIdList)

internal const val SMELL_BASELINE = "SmellBaseline"
internal const val BLACKLIST = "Blacklist"
internal const val WHITELIST = "Whitelist"
internal const val ID = "ID"

internal val Finding.baselineId: String
    get() = this.id + ":" + this.signature
