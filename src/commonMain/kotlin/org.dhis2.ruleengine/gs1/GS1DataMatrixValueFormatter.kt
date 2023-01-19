package org.dhis2.ruleengine.gs1

import org.dhis2.ruleengine.gs1.GS1Table.aiFixedLengthMap

class GS1DataMatrixValueFormatter : GS1BaseValueFormatter() {
    private var dataMap: MutableMap<String, String>? = null
    override fun formatValue(value: String?, valueToReturn: GS1Elements): String? {
        dataMap = mutableMapOf()
        val gs1Groups = removeGS1Identifier(value)!!.split(GS1Elements.GS1_GROUP_SEPARATOR.element.toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        for (gs1Group in gs1Groups) {
            handleGroupData(gs1Group)
        }
        return if (dataMap!!.containsKey(valueToReturn.element)) {
            dataMap!![valueToReturn.element]
        } else {
            throw IllegalArgumentException("Required key does not exist for provided value")
        }
    }

    private fun handleGroupData(gs1Group: String) {
        if (!gs1Group.isEmpty()) {
            val gs1GroupLength = gs1Group.length
            val ai = GS1Elements.getApplicationIdentifier(gs1Group)
            var nextValueLength = aiFixedLengthMap()[ai.substring(0, 2)]
            if (nextValueLength == null) nextValueLength = gs1GroupLength
            dataMap!![ai] = gs1Group.substring(ai.length, nextValueLength)
            handleGroupData(gs1Group.substring(nextValueLength))
        }
    }
}