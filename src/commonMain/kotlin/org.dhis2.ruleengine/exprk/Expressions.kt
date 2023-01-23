package org.dhis2.ruleengine.exprk

import org.dhis2.ruleengine.RuleVariableValue
import org.dhis2.ruleengine.exprk.functions.*
import org.dhis2.ruleengine.exprk.internal.*
import kotlin.math.abs
import kotlin.math.round
import org.dhis2.ruleengine.exprk.internal.Function

class ExpressionException(message: String) : RuntimeException(message)

@Suppress("unused")
class Expressions {
    private val evaluator = Evaluator()

    init {
        evaluator.addFunction(ADD_DAYS, AddDays())
        evaluator.addFunction(CEIL, Ceil())
        evaluator.addFunction(CONCATENATE, Concatenate())
        evaluator.addFunction(COUNT_IF_VALUE, CountIfValue{evaluator.valueMap})
        evaluator.addFunction(COUNT_IF_ZERO_POS, CountIfZeroPos{evaluator.valueMap})
        evaluator.addFunction(COUNT, Count{evaluator.valueMap})
        evaluator.addFunction(DAYS_BETWEEN, DaysBetween())
        evaluator.addFunction(EXTRACT_DATAMATRIX_VALUE, ExtractDataMatrixValue())
        evaluator.addFunction(FLOOR, Floor())
        evaluator.addFunction(HAS_USER_ROLE, HasUserRole { evaluator.supplementaryData })
        evaluator.addFunction(HAS_VALUE, HasValue{evaluator.valueMap})
        evaluator.addFunction(
            IN_ORG_UNIT_GROUP,
            InOrgUnitGroup({ evaluator.valueMap }, { evaluator.supplementaryData })
        )
        evaluator.addFunction(LAST_EVENT_DATE, LastEventDate { evaluator.valueMap })
        evaluator.addFunction(LEFT, Left())
        evaluator.addFunction(LENGTH, Length())
        evaluator.addFunction(MAX_VALUE, MaxValue { evaluator.valueMap })
        evaluator.addFunction(MIN_VALUE, MinValue { evaluator.valueMap })
        evaluator.addFunction(MODULUS, Modulus())
        evaluator.addFunction(MONTHS_BETWEEN, MonthsBetween())
        evaluator.addFunction(O_ZIP, Ozip())
        evaluator.addFunction(RIGHT, Right())
        evaluator.addFunction(ROUND, Round())
        evaluator.addFunction(SPLIT, Split())
        evaluator.addFunction(SUB_STRING, SubString())
        evaluator.addFunction(VALIDATE_PATTERN, ValidatePattern())
        evaluator.addFunction(WEEKS_BETWEEN, WeeksBetween())
        evaluator.addFunction(YEARS_BETWEEN, YearsBetween())
        evaluator.addFunction(ZING, Zing())
        evaluator.addFunction(ZPVC, Zpvc())
        evaluator.addFunction(ZSCORE_HFA, ZScoreHFA())
        evaluator.addFunction(ZSCORE_WFA, ZScoreWFA())
        evaluator.addFunction(ZSCORE_WFH, ZScoreWFH())

        define("true", "true")
        define("false", "true")

        evaluator.addFunction("if", object : Function() {
            override fun call(arguments: List<String?>): String {
                val condition = arguments[0]
                val thenValue = arguments[1]
                val elseValue = arguments[2]

                return if (condition.toBoolean()) {
                    thenValue ?: ""
                } else {
                    elseValue ?: ""
                }
            }
        })
    }

    fun define(name: String, value: Long): Expressions {
        define(name, value.toString())

        return this
    }

    fun define(name: String, value: Double): Expressions {
        define(name, value.toString())

        return this
    }

    fun define(name: String, expression: String): Expressions {
        val expr = parse(expression)
        evaluator.define(name, expr)

        return this
    }

    fun addFunction(name: String, function: Function): Expressions {
        evaluator.addFunction(name, function)

        return this
    }

    fun addFunction(name: String, func: (List<String?>) -> String): Expressions {
        evaluator.addFunction(name, object : Function() {
            override fun call(arguments: List<String?>): String {
                return func(arguments)
            }

        })

        return this
    }

    fun eval(expression: String): String {
        return evaluator.eval(parse(expression))
    }

    fun withValueMap(valueMap: Map<String, RuleVariableValue>): Expressions {
        evaluator.addValueMap(valueMap)
        return this
    }

    fun withSupplementaryData(supplementaryData: Map<String, List<String>>): Expressions {
        evaluator.addSupplementaryData(supplementaryData)
        return this
    }

    /**
     * eval an expression then round it with {@link Evaluator#mathContext} and call toEngineeringString <br>
     * if error will return message from Throwable
     * @param expression String
     * @return String
     */
    fun evalToString(expression: String): String {
        return try {
            evaluator.eval(parse(expression))
        } catch (e: Throwable) {
            e.cause?.message ?: e.message ?: "unknown error"
        }
    }

    private fun parse(expression: String): Expr {
        return parse(scan(expression))
    }

    private fun parse(tokens: List<Token>): Expr {
        return Parser(tokens).parse()
    }

    private fun scan(expression: String): List<Token> {
        return Scanner(expression).scanTokens()
    }
}