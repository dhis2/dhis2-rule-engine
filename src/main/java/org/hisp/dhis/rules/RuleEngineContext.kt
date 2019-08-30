package org.hisp.dhis.rules

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.RuleVariable

data class RuleEngineContext (val expressionEvaluator: RuleExpressionEvaluator,
                              val rules: PersistentList<Rule>,
                              val ruleVariables: PersistentList<RuleVariable>,
                              val supplementaryData: Map<String, List<String>>,
                              val calculatedValueMap: Map<String, Map<String, String>>,
                              val constantsValues: Map<String, String>) {

    fun toEngineBuilder(): RuleEngine.Builder {
        return RuleEngine.Builder(this)
    }

    class Builder constructor(private val evaluator: RuleExpressionEvaluator) {

        private var rules: PersistentList<Rule>? = null

        private var ruleVariables: PersistentList<RuleVariable>? = null

        private var supplementaryData: Map<String, List<String>>? = null

        private var calculatedValueMap: Map<String, Map<String, String>>? = null

        private var constantsValues: Map<String, String>? = null

        fun rules(rules: List<Rule>?) =
                apply {
                    rules?.let { this.rules = rules.toPersistentList() } ?:
                    throw IllegalArgumentException("rules == null")
                }

        fun ruleVariables(ruleVariables: List<RuleVariable>?) =
                apply {
                    ruleVariables?.let { this.ruleVariables = ruleVariables.toPersistentList() } ?:
                    throw IllegalArgumentException("ruleVariables == null")
                }

        fun supplementaryData(supplementaryData: Map<String, List<String>>?) =
                apply {
                    supplementaryData?.let { this.supplementaryData = supplementaryData } ?:
                    throw IllegalArgumentException("supplementaryData == null")
                }

        fun calculatedValueMap(calculatedValueMap: Map<String, Map<String, String>>?) =
                apply {
                    calculatedValueMap?.let { this.calculatedValueMap = calculatedValueMap } ?:
                    throw IllegalArgumentException("calculatedValueMap == null")
                }

        fun constantsValue(constantsValues: Map<String, String>?) =
                apply {
                    constantsValues?.let { this.constantsValues = constantsValues } ?:
                    throw IllegalArgumentException("constantsValue == null")
                }

        fun build(): RuleEngineContext {
            val rules = rules ?: persistentListOf()
            val ruleVariables = ruleVariables ?: persistentListOf()
            val supplementaryData = supplementaryData ?: hashMapOf()
            val calculatedValueMap = calculatedValueMap ?: hashMapOf()
            val constantsValues = constantsValues ?: hashMapOf()

            return RuleEngineContext(evaluator, rules, ruleVariables, supplementaryData, calculatedValueMap, constantsValues)
        }

    }

    companion object {

        @JvmStatic
        fun builder(evaluator: RuleExpressionEvaluator?) =
            evaluator?.let { Builder(it) } ?: throw IllegalArgumentException("evaluator == null")

    }
}
