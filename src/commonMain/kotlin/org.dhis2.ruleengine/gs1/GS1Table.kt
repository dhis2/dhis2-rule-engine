package org.dhis2.ruleengine.gs1

object GS1Table {
    fun aiFixedLengthMap(): Map<String, Int> {
        val aiFixedLengthMap: MutableMap<String, Int> = mutableMapOf()
        aiFixedLengthMap[GS1Elements.SSCC.element] = 20
        aiFixedLengthMap[GS1Elements.GTIN.element] = 16
        aiFixedLengthMap[GS1Elements.CONTENT.element] = 16
        aiFixedLengthMap["03"] = 16
        aiFixedLengthMap["04"] = 18
        aiFixedLengthMap[GS1Elements.PROD_DATE.element] = 8
        aiFixedLengthMap[GS1Elements.DUE_DATE.element] = 8
        aiFixedLengthMap[GS1Elements.PACK_DATE.element] = 8
        aiFixedLengthMap["14"] = 8
        aiFixedLengthMap[GS1Elements.BEST_BEFORE_DATE.element] = 8
        aiFixedLengthMap[GS1Elements.SELL_BY.element] = 8
        aiFixedLengthMap[GS1Elements.EXP_DATE.element] = 8
        aiFixedLengthMap["18"] = 8
        aiFixedLengthMap["19"] = 8
        aiFixedLengthMap[GS1Elements.VARIANT.element] = 4
        aiFixedLengthMap["31"] = 10
        aiFixedLengthMap["32"] = 10
        aiFixedLengthMap["33"] = 10
        aiFixedLengthMap["34"] = 10
        aiFixedLengthMap["35"] = 10
        aiFixedLengthMap["36"] = 10
        aiFixedLengthMap["41"] = 16
        return aiFixedLengthMap
    }

    fun aiNotFixedMaxLengthMap(): Map<String, Int> {
        val aiFixedLengthMap: MutableMap<String, Int> = mutableMapOf()
        aiFixedLengthMap[GS1Elements.LOT_NUMBER.element] = 22
        aiFixedLengthMap[GS1Elements.SERIAL_NUMBER.element] = 22
        return aiFixedLengthMap
    }
}