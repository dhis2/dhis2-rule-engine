package org.dhis2.ruleengine.exprk.internal

import org.dhis2.ruleengine.RuleVariableValue
import org.dhis2.ruleengine.exprk.ExpressionException
import org.dhis2.ruleengine.exprk.internal.TokenType.*
import kotlin.math.pow

internal class Evaluator() : ExprVisitor<String> {
    private val variables: LinkedHashMap<String, String> = linkedMapOf()
    private val functions: MutableMap<String, Function> = mutableMapOf()
    private var valueMap: Map<String, RuleVariableValue> = mutableMapOf()

    private fun define(name: String, value: String) {
        variables += name to value
    }

    fun define(name: String, expr: Expr): Evaluator {
        define(name.lowercase(), eval(expr))

        return this
    }

    fun addFunction(name: String, function: Function): Evaluator {
        functions += name.lowercase() to function

        return this
    }

    fun addValueMap(valueMap: Map<String, RuleVariableValue>): Evaluator {
        this.valueMap = valueMap

        return this
    }

    fun findValue(key: String): String? {
        return valueMap[key]?.value
    }

    fun eval(expr: Expr): String {
        return expr.accept(this)
    }

    override fun visitAssignExpr(expr: AssignExpr): String {
        val value = eval(expr.value)

        define(expr.name.lexeme, value)

        return value
    }

    override fun visitLogicalExpr(expr: LogicalExpr): String {
        val left = expr.left
        val right = expr.right

        return when (expr.operator.type) {
            BAR_BAR -> left or right
            AMP_AMP -> left and right
            else -> throw ExpressionException(
                "Invalid logical operator '${expr.operator.lexeme}'"
            )
        }
    }

    override fun visitBinaryExpr(expr: BinaryExpr): String {
        val left = eval(expr.left).toDouble()
        val right = eval(expr.right).toDouble()

        return when (expr.operator.type) {
            PLUS -> (left + right).toString()
            MINUS -> (left - right).toString()
            STAR -> (left * right).toString()
            SLASH -> left.div(right).toString()
            MODULO -> left.mod(right).toString()
            EXPONENT -> left.pow(right).toString()
            EQUAL_EQUAL -> (left == right).toString()
            NOT_EQUAL -> (left != right).toString()
            GREATER -> (left > right).toString()
            GREATER_EQUAL -> (left >= right).toString()
            LESS -> (left < right).toString()
            LESS_EQUAL -> (left <= right).toString()
            else -> throw ExpressionException(
                "Invalid binary operator '${expr.operator.lexeme}'"
            )
        }
    }

    override fun visitUnaryExpr(expr: UnaryExpr): String {
        val right = eval(expr.right)

        return when (expr.operator.type) {
            MINUS -> {
                (-1 * right.toDouble()).toString()
            }

            SQUARE_ROOT -> {
                right.toDouble().pow(0.5).toString()
            }

            else -> throw ExpressionException("Invalid unary operator")
        }
    }

    override fun visitCallExpr(expr: CallExpr): String {
        val name = expr.name
        val function = functions[name.lowercase()] ?: throw ExpressionException("Undefined function '$name'")

        return function.call(expr.arguments.map { eval(it) })
    }

    override fun visitLiteralExpr(expr: LiteralExpr): String {
        return expr.value
    }

    override fun visitVariableExpr(expr: VariableExpr): String {
        val name = expr.name.lexeme

        val value = variables[name.lowercase()] ?: valueMap[name]?.value
        return value ?: throw ExpressionException("Undefined variable '$name'")
    }

    override fun visitGroupingExpr(expr: GroupingExpr): String {
        return eval(expr.expression)
    }

    private infix fun Expr.or(right: Expr): String {
        val left = eval(this)

        // short-circuit if left is truthy
        if (left.isTruthy()) return "1"

        return eval(right).isTruthy().toString()
    }

    private infix fun Expr.and(right: Expr): String {
        val left = eval(this)

        // short-circuit if left is falsey
        if (!left.isTruthy()) return "0"

        return eval(right).isTruthy().toString()
    }

    private fun String.isTruthy(): Boolean {
        return this != "0"
    }

    private fun Boolean.toString(): String {
        return if (this) "1" else "0"
    }
}