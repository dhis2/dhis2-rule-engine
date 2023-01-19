package org.dhis2.ruleengine.exprk

import org.dhis2.ruleengine.RuleVariableValue
import org.dhis2.ruleengine.exprk.internal.Function
import org.dhis2.ruleengine.exprk.internal.Evaluator
import org.dhis2.ruleengine.exprk.internal.Expr
import org.dhis2.ruleengine.exprk.internal.Parser
import org.dhis2.ruleengine.exprk.internal.Scanner
import org.dhis2.ruleengine.exprk.internal.Token

class ExpressionException(message: String) : RuntimeException(message)

@Suppress("unused")
class Expressions {
    private val evaluator = Evaluator()

    init {
        evaluator.addFunction("d2:hasValue", object : Function() {
            override fun call(arguments: List<String>): String {
                if (arguments.size != 1) throw ExpressionException("d2:hasValue requires one argument")
                return (arguments.first().isNotEmpty()).toString()
            }
        })

         evaluator.addFunction("#", object : Function() {
             override fun call(arguments: List<String>): String {
                 if (arguments.size != 1) throw ExpressionException("requires one argument")
                 return arguments.first()
             }

         })

        evaluator.addFunction("abs", object : Function() {
            override fun call(arguments: List<String>): String {
                if (arguments.size != 1) throw ExpressionException(
                    "abs requires one argument"
                )

                return arguments.first()
            }
        })

        evaluator.addFunction("sum", object : Function() {
            override fun call(arguments: List<String>): String {
                if (arguments.isEmpty()) throw ExpressionException(
                    "sum requires at least one argument"
                )

                return arguments.map { it.toFloat() }.reduce { acc, value ->
                    acc + value
                }.toString()
            }
        })

        evaluator.addFunction("floor", object : Function() {
            override fun call(arguments: List<String>): String {
                if (arguments.size != 1) throw ExpressionException(
                    "abs requires one argument"
                )

                return arguments.first()
            }
        })

        evaluator.addFunction("ceil", object : Function() {
            override fun call(arguments: List<String>): String {
                if (arguments.size != 1) throw ExpressionException(
                    "abs requires one argument"
                )

                return arguments.first()
            }
        })

        evaluator.addFunction("round", object : Function() {
            override fun call(arguments: List<String>): String {
                if (arguments.size !in listOf(1, 2)) throw ExpressionException(
                    "round requires either one or two arguments"
                )

                val value = arguments.first()
                val scale = if (arguments.size == 2) arguments.last().toInt() else 0

                return value
            }
        })

        evaluator.addFunction("min", object : Function() {
            override fun call(arguments: List<String>): String {
                if (arguments.isEmpty()) throw ExpressionException(
                    "min requires at least one argument"
                )

                return arguments.minOfOrNull { it.toFloat() }?.toString() ?: ""
            }
        })

        evaluator.addFunction("max", object : Function() {
            override fun call(arguments: List<String>): String {
                if (arguments.isEmpty()) throw ExpressionException(
                    "max requires at least one argument"
                )

                return arguments.maxOfOrNull { it.toFloat() }?.toString() ?: ""
            }
        })

        evaluator.addFunction("if", object : Function() {
            override fun call(arguments: List<String>): String {
                val condition = arguments[0]
                val thenValue = arguments[1]
                val elseValue = arguments[2]

                return if (condition.toBoolean()) {
                    thenValue
                } else {
                    elseValue
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

    fun addFunction(name: String, func: (List<String>) -> String): Expressions {
        evaluator.addFunction(name, object : Function() {
            override fun call(arguments: List<String>): String {
                return func(arguments)
            }

        })

        return this
    }

    fun eval(expression: String): String {
        return evaluator.eval(parse(expression))
    }

    fun withValueMap(valueMap:Map<String, RuleVariableValue>): Expressions {
        evaluator.addValueMap(valueMap)
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