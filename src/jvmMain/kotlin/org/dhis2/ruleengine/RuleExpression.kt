package org.dhis2.ruleengine

import org.hisp.dhis.parser.expression.antlr.ExpressionParser

abstract class RuleExpression {

    abstract fun variables(): Set<String>

    companion object {
        /* This method should probably be removed creating a new prefix for program rule variables that is
     *  not shared with indicators.*/
        fun getProgramRuleVariable(ctx: ExpressionParser.ExprContext): String {
            return if (isProgramRuleVariable(ctx)) {
                getProgramRuleVariableText(ctx)
            } else if (ctx.programVariable() != null) {
                ctx.programVariable().getText()
            } else {
                ctx.uid0.getText() + secondPart(ctx) + thirdPart(ctx)
            }
        }

        private fun getProgramRuleVariableText(ctx: ExpressionParser.ExprContext): String {
            return if (ctx.programRuleVariableName() != null) {
                ctx.programRuleVariableName().getText()
            } else {
                ctx.programRuleStringVariableName().getText().replace("\'".toRegex(), "")
            }
        }

        private fun isProgramRuleVariable(ctx: ExpressionParser.ExprContext): Boolean {
            return ctx.programRuleVariableName() != null || ctx.programRuleStringVariableName() != null
        }

        private fun secondPart(ctx: ExpressionParser.ExprContext): String {
            if (ctx.uid1 != null) {
                return "." + ctx.uid1.getText()
            } else if (ctx.wild1 != null) {
                return ctx.wild1.getText()
            }
            return ""
        }

        private fun thirdPart(ctx: ExpressionParser.ExprContext): String {
            if (ctx.uid2 != null && ctx.uid1 == null) {
                return ".*." + ctx.uid2.getText()
            } else if (ctx.uid2 != null) {
                return "." + ctx.uid2.getText()
            } else if (ctx.wild2 != null) {
                return ctx.wild2.getText()
            }
            return ""
        }
    }
}