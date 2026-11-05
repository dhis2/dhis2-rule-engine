package org.hisp.dhis.rules

import org.hisp.dhis.rules.api.ExpressionFunctionParameter
import org.hisp.dhis.rules.api.ExpressionValueType

@JsExport
data class ExpressionFunctionJs(
    val name: String,
    val returnType: ExpressionValueType,
    val parameters: Array<ExpressionFunctionParameter>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class.js != other::class.js) return false

        other as ExpressionFunctionJs

        if (name != other.name) return false
        if (returnType != other.returnType) return false
        if (!parameters.contentEquals(other.parameters)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + returnType.hashCode()
        result = 31 * result + parameters.contentHashCode()
        return result
    }
}
