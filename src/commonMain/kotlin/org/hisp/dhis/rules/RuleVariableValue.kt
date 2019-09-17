package org.hisp.dhis.rules


import org.hisp.dhis.rules.models.RuleValueType
import org.hisp.dhis.rules.utils.Date
import org.hisp.dhis.rules.utils.SimpleDateFormat
import kotlin.jvm.JvmStatic

data class RuleVariableValue(val value: String?,
                             val type: RuleValueType,
                             val candidates: List<String>,
                             val eventDate: String?) {

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd"
        private const val NUMBER_PATTERN = "0.0"

        @JvmStatic
        fun create(ruleValueType: RuleValueType?): RuleVariableValue {
            return if (ruleValueType == null) throw IllegalArgumentException("Invalid value type")
            else internalCreate(null, ruleValueType, listOf(), getFormattedDate(Date()))
        }



        @JvmStatic
        fun create(value: String?, ruleValueType: RuleValueType?): RuleVariableValue {
            return if (ruleValueType == null) throw IllegalArgumentException("Invalid value type")
            else internalCreate(value, ruleValueType, listOf(), getFormattedDate(Date()))
        }


        @JvmStatic
        fun create(value: String?, ruleValueType: RuleValueType?, candidates: List<String>?, eventDate: String?): RuleVariableValue {
            return when {
                ruleValueType == null -> throw IllegalArgumentException("Invalid value type")
                candidates == null -> throw IllegalArgumentException("Candidate cannot be null")
                eventDate == null -> internalCreate(value, ruleValueType, candidates, getFormattedDate(Date()))
                else ->  internalCreate(value, ruleValueType, candidates, eventDate)
            }
        }

        private fun internalCreate(value: String?, ruleValueType: RuleValueType, candidates: List<String>, eventDate: String) : RuleVariableValue {
            return when (value) {
                null -> RuleVariableValue(null, ruleValueType, candidates, eventDate)
                else -> {
                    // clean-up the value before processing it
                    var processedValue = value.replace("'", "")

                    // if text processedValue, wrap it
                    if (RuleValueType.TEXT == ruleValueType) {
                        processedValue =  processedValue.wrap()
                    }

                    // TODO: UNCOMMENT WHEN VALUE FORMAT IN CLIENT IS READY
                    /* if (RuleValueType.NUMERIC.equals(ruleValueType)) {
                        processedValue = getFormattedNumber(value);
                    }*/
                    RuleVariableValue(processedValue, ruleValueType, candidates, eventDate)
                }
            }
        }


        private fun getFormattedDate(date: Date): String {
            val format = SimpleDateFormat()
            format.applyPattern(DATE_PATTERN)

            return format.format(date)
        }

        /*private fun getFormattedNumber(number: String): String {
            val otherSymbols = DecimalFormatSymbols(Locale.US)
            otherSymbols.decimalSeparator = '.'
            return DecimalFormat(NUMBER_PATTERN, otherSymbols).format(java.lang.Float.valueOf(number))
        }*/

    }
}