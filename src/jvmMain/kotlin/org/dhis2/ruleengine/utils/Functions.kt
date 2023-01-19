package org.dhis2.ruleengine.utils

import org.dhis2.ruleengine.functions.*
import org.dhis2.ruleengine.parser.expression.function.*
import org.dhis2.ruleengine.variables.ProgramRuleConstant
import org.dhis2.ruleengine.variables.ProgramRuleCustomVariable
import org.dhis2.ruleengine.variables.ProgramRuleVariable
import org.dhis2.ruleengine.variables.Variable
import org.hisp.dhis.antlr.AntlrExprItem
import org.hisp.dhis.parser.expression.antlr.ExpressionParser

val FUNCTIONS = mutableMapOf<Int, AntlrExprItem>().apply {
    put(ExpressionParser.D2_CEIL, RuleFunctionCeil())
    put(ExpressionParser.D2_ADD_DAYS, RuleFunctionAddDays())
    put(ExpressionParser.D2_CONCATENATE, RuleFunctionConcatenate())
    put(ExpressionParser.D2_FLOOR, RuleFunctionFloor())
    put(ExpressionParser.D2_SUBSTRING, RuleFunctionSubString())
    put(ExpressionParser.D2_LENGTH, RuleFunctionLength())
    put(ExpressionParser.D2_LEFT, RuleFunctionLeft())
    put(ExpressionParser.D2_RIGHT, RuleFunctionRight())
    put(ExpressionParser.D2_MODULUS, RuleFunctionModulus())
    put(ExpressionParser.D2_ROUND, RuleFunctionRound())
    put(ExpressionParser.D2_YEARS_BETWEEN, RuleFunctionYearsBetween())
    put(ExpressionParser.D2_MONTHS_BETWEEN, RuleFunctionMonthsBetween())
    put(ExpressionParser.D2_WEEKS_BETWEEN, RuleFunctionWeeksBetween())
    put(ExpressionParser.D2_DAYS_BETWEEN, RuleFunctionDaysBetween())
    put(ExpressionParser.D2_ZSCOREWFH, RuleFunctionZScoreWFH())
    put(ExpressionParser.D2_ZSCOREWFA, RuleFunctionZScoreWFA())
    put(ExpressionParser.D2_ZSCOREHFA, RuleFunctionZScoreHFA())
    put(ExpressionParser.D2_SPLIT, RuleFunctionSplit())
    put(ExpressionParser.D2_OIZP, RuleFunctionOizp())
    put(ExpressionParser.D2_ZING, RuleFunctionZing())
    put(ExpressionParser.D2_ZPVC, RuleFunctionZpvc())
    put(ExpressionParser.D2_VALIDATE_PATTERN, RuleFunctionValidatePattern())
    put(ExpressionParser.D2_MAX_VALUE, RuleFunctionMaxValue())
    put(ExpressionParser.D2_MIN_VALUE, RuleFunctionMinValue())
    put(ExpressionParser.D2_COUNT, RuleFunctionCount())
    put(ExpressionParser.D2_COUNT_IF_VALUE, RuleFunctionCountIfValue())
    put(ExpressionParser.D2_HAS_USER_ROLE, RuleFunctionHasUserRole())
    put(ExpressionParser.D2_HAS_VALUE, RuleFunctionHasValue())
    put(ExpressionParser.D2_IN_ORG_UNIT_GROUP, RuleFunctionInOrgUnitGroup())
    put(ExpressionParser.D2_LAST_EVENT_DATE, RuleFunctionLastEventDate())
    put(ExpressionParser.D2_COUNT_IF_ZERO_POS, RuleFunctionCountIfZeroPos())
    put(ExpressionParser.D2_EXTRACT_DATA_MATRIX_VALUE, RuleFunctionExtractDataMatrixValue())
    put(ExpressionParser.V_BRACE, ProgramRuleVariable())
    put(ExpressionParser.HASH_BRACE, Variable())
    put(ExpressionParser.C_BRACE, ProgramRuleConstant())
    put(ExpressionParser.A_BRACE, Variable())
    put(ExpressionParser.X_BRACE, ProgramRuleCustomVariable())
    put(ExpressionParser.PAREN, OperatorGroupingParentheses())
    put(ExpressionParser.PLUS, OperatorMathPlus())
    put(ExpressionParser.MINUS, OperatorMathMinus())
    put(ExpressionParser.POWER, OperatorMathPower())
    put(ExpressionParser.MUL, OperatorMathMultiply())
    put(ExpressionParser.DIV, OperatorMathDivide())
    put(ExpressionParser.MOD, OperatorMathModulus())
    put(ExpressionParser.NOT, OperatorLogicalNot())
    put(ExpressionParser.EXCLAMATION_POINT, OperatorLogicalNot())
    put(ExpressionParser.AND, OperatorLogicalAnd())
    put(ExpressionParser.AMPERSAND_2, OperatorLogicalAnd())
    put(ExpressionParser.OR, OperatorLogicalOr())
    put(ExpressionParser.VERTICAL_BAR_2, OperatorLogicalOr()) // Comparison operators
    put(ExpressionParser.EQ, OperatorCompareEqual())
    put(ExpressionParser.NE, OperatorCompareNotEqual())
    put(ExpressionParser.GT, OperatorCompareGreaterThan())
    put(ExpressionParser.LT, OperatorCompareLessThan())
    put(ExpressionParser.GEQ, OperatorCompareGreaterThanOrEqual())
    put(ExpressionParser.LEQ, OperatorCompareLessThanOrEqual()) // Common ANTLR operators
    //.putAll( ANTLR_EXPRESSION_ITEMS  )
}