package org.dhis2.ruleengine.gs1

interface GS1ValueFormatter {
    fun formatValue(value: String?, valueToReturn: GS1Elements): String?
    fun removeGS1Identifier(value: String?): String?
}