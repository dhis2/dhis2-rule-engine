package org.dhis2.ruleengine.gs1

abstract class GS1BaseValueFormatter : GS1ValueFormatter {
    override fun removeGS1Identifier(value: String?): String? {
        return value!!.substring(3)
    }

    companion object {
        fun getFormatterFromValue(value: String?): GS1ValueFormatter {
            if (value == null) throw IllegalArgumentException("Can't extract data from null value")
            if (value.length < 3) throw IllegalArgumentException("Value does not contains enough information")
            val gs1Identifier = value.substring(0, 3)
            return when (GS1Elements.fromKey(gs1Identifier)) {
                GS1Elements.GS1_d2_IDENTIFIER,
                GS1Elements.GS1_Q3_IDENTIFIER ->
                    GS1DataMatrixValueFormatter()

                GS1Elements.GS1_J1_IDENTIFIER,
                GS1Elements.GS1_d1_IDENTIFIER,
                GS1Elements.GS1_Q1_IDENTIFIER,
                GS1Elements.GS1_E0_IDENTIFIER,
                GS1Elements.GS1_E1_IDENTIFIER,
                GS1Elements.GS1_E2_IDENTIFIER,
                GS1Elements.GS1_E3_IDENTIFIER,
                GS1Elements.GS1_E4_IDENTIFIER,
                GS1Elements.GS1_I1_IDENTIFIER,
                GS1Elements.GS1_C1_IDENTIFIER,
                GS1Elements.GS1_e0_IDENTIFIER,
                GS1Elements.GS1_e1_IDENTIFIER,
                GS1Elements.GS1_e2_IDENTIFIER -> throw IllegalArgumentException("gs1 identifier $gs1Identifier is not supported")

                else -> throw IllegalArgumentException("Value does not start with a gs1 identifier")
            }
        }
    }
}