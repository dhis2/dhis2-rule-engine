package org.hisp.dhis.rules

import org.hisp.dhis.rules.api.ExpressionValueType

@JsExport
data class ExpressionOperatorJs(
    val symbol: String,
    val aliases: Array<String>,
    val returnType: ExpressionValueType,
    val operandsType: ExpressionValueType,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class.js != other::class.js) return false

        other as ExpressionOperatorJs

        if (symbol != other.symbol) return false
        if (!aliases.contentEquals(other.aliases)) return false
        if (returnType != other.returnType) return false
        if (operandsType != other.operandsType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = symbol.hashCode()
        result = 31 * result + aliases.contentHashCode()
        result = 31 * result + returnType.hashCode()
        result = 31 * result + operandsType.hashCode()
        return result
    }
}
