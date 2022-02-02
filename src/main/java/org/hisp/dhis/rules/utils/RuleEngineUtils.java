package org.hisp.dhis.rules.utils;

/*
 * Copyright (c) 2004-2020, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import com.google.common.collect.ImmutableMap;
import org.hisp.dhis.antlr.AntlrExprItem;
import org.hisp.dhis.rules.ItemValueType;
import org.hisp.dhis.rules.functions.RuleFunctionAddDays;
import org.hisp.dhis.rules.functions.RuleFunctionCeil;
import org.hisp.dhis.rules.functions.RuleFunctionConcatenate;
import org.hisp.dhis.rules.functions.RuleFunctionCount;
import org.hisp.dhis.rules.functions.RuleFunctionCountIfValue;
import org.hisp.dhis.rules.functions.RuleFunctionCountIfZeroPos;
import org.hisp.dhis.rules.functions.RuleFunctionDaysBetween;
import org.hisp.dhis.rules.functions.RuleFunctionExtractDataMatrixValue;
import org.hisp.dhis.rules.functions.RuleFunctionFloor;
import org.hisp.dhis.rules.functions.RuleFunctionHasUserRole;
import org.hisp.dhis.rules.functions.RuleFunctionHasValue;
import org.hisp.dhis.rules.functions.RuleFunctionInOrgUnitGroup;
import org.hisp.dhis.rules.functions.RuleFunctionLastEventDate;
import org.hisp.dhis.rules.functions.RuleFunctionLeft;
import org.hisp.dhis.rules.functions.RuleFunctionLength;
import org.hisp.dhis.rules.functions.RuleFunctionMaxValue;
import org.hisp.dhis.rules.functions.RuleFunctionMinValue;
import org.hisp.dhis.rules.functions.RuleFunctionModulus;
import org.hisp.dhis.rules.functions.RuleFunctionMonthsBetween;
import org.hisp.dhis.rules.functions.RuleFunctionOizp;
import org.hisp.dhis.rules.functions.RuleFunctionRight;
import org.hisp.dhis.rules.functions.RuleFunctionRound;
import org.hisp.dhis.rules.functions.RuleFunctionSplit;
import org.hisp.dhis.rules.functions.RuleFunctionSubString;
import org.hisp.dhis.rules.functions.RuleFunctionValidatePattern;
import org.hisp.dhis.rules.functions.RuleFunctionWeeksBetween;
import org.hisp.dhis.rules.functions.RuleFunctionYearsBetween;
import org.hisp.dhis.rules.functions.RuleFunctionZScoreHFA;
import org.hisp.dhis.rules.functions.RuleFunctionZScoreWFA;
import org.hisp.dhis.rules.functions.RuleFunctionZScoreWFH;
import org.hisp.dhis.rules.functions.RuleFunctionZing;
import org.hisp.dhis.rules.functions.RuleFunctionZpvc;
import org.hisp.dhis.rules.parser.expression.function.OperatorCompareEqual;
import org.hisp.dhis.rules.parser.expression.function.OperatorCompareGreaterThan;
import org.hisp.dhis.rules.parser.expression.function.OperatorCompareGreaterThanOrEqual;
import org.hisp.dhis.rules.parser.expression.function.OperatorCompareLessThan;
import org.hisp.dhis.rules.parser.expression.function.OperatorCompareLessThanOrEqual;
import org.hisp.dhis.rules.parser.expression.function.OperatorCompareNotEqual;
import org.hisp.dhis.rules.parser.expression.function.OperatorGroupingParentheses;
import org.hisp.dhis.rules.parser.expression.function.OperatorLogicalAnd;
import org.hisp.dhis.rules.parser.expression.function.OperatorLogicalNot;
import org.hisp.dhis.rules.parser.expression.function.OperatorLogicalOr;
import org.hisp.dhis.rules.parser.expression.function.OperatorMathDivide;
import org.hisp.dhis.rules.parser.expression.function.OperatorMathMinus;
import org.hisp.dhis.rules.parser.expression.function.OperatorMathModulus;
import org.hisp.dhis.rules.parser.expression.function.OperatorMathMultiply;
import org.hisp.dhis.rules.parser.expression.function.OperatorMathPlus;
import org.hisp.dhis.rules.parser.expression.function.OperatorMathPower;
import org.hisp.dhis.rules.variables.ProgramRuleConstant;
import org.hisp.dhis.rules.variables.ProgramRuleCustomVariable;
import org.hisp.dhis.rules.variables.ProgramRuleVariable;
import org.hisp.dhis.rules.variables.Variable;

import java.util.Arrays;
import java.util.Map;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.*;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.A_BRACE;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.X_BRACE;

/**
 * @author Zubair Asghar
 */
public class RuleEngineUtils
{
    public final static ImmutableMap<Integer, AntlrExprItem> FUNCTIONS = ImmutableMap.<Integer, AntlrExprItem>builder()

        .put( D2_CEIL, new RuleFunctionCeil() )
        .put( D2_ADD_DAYS, new RuleFunctionAddDays() )
        .put( D2_CONCATENATE, new RuleFunctionConcatenate() )
        .put( D2_FLOOR, new RuleFunctionFloor() )
        .put( D2_SUBSTRING, new RuleFunctionSubString() )
        .put( D2_LENGTH, new RuleFunctionLength() )
        .put( D2_LEFT, new RuleFunctionLeft() )
        .put( D2_RIGHT, new RuleFunctionRight() )
        .put( D2_MODULUS, new RuleFunctionModulus() )
        .put( D2_ROUND, new RuleFunctionRound() )
        .put( D2_YEARS_BETWEEN, new RuleFunctionYearsBetween() )
        .put( D2_MONTHS_BETWEEN, new RuleFunctionMonthsBetween() )
        .put( D2_WEEKS_BETWEEN, new RuleFunctionWeeksBetween() )
        .put( D2_DAYS_BETWEEN, new RuleFunctionDaysBetween() )
        .put( D2_ZSCOREWFH, new RuleFunctionZScoreWFH() )
        .put( D2_ZSCOREWFA, new RuleFunctionZScoreWFA() )
        .put( D2_ZSCOREHFA, new RuleFunctionZScoreHFA() )
        .put( D2_SPLIT, new RuleFunctionSplit() )
        .put( D2_OIZP, new RuleFunctionOizp() )
        .put( D2_ZING, new RuleFunctionZing() )
        .put( D2_ZPVC, new RuleFunctionZpvc() )
        .put( D2_VALIDATE_PATTERN, new RuleFunctionValidatePattern() )
        .put( D2_MAX_VALUE, new RuleFunctionMaxValue() )
        .put( D2_MIN_VALUE, new RuleFunctionMinValue() )
        .put( D2_COUNT, new RuleFunctionCount() )
        .put( D2_COUNT_IF_VALUE, new RuleFunctionCountIfValue() )
        .put( D2_HAS_USER_ROLE, new RuleFunctionHasUserRole() )
        .put( D2_HAS_VALUE, new RuleFunctionHasValue() )
        .put( D2_IN_ORG_UNIT_GROUP, new RuleFunctionInOrgUnitGroup() )
        .put( D2_LAST_EVENT_DATE, new RuleFunctionLastEventDate() )
        .put( D2_COUNT_IF_ZERO_POS, new RuleFunctionCountIfZeroPos() )
        .put( D2_EXTRACT_DATA_MATRIX_VALUE, new RuleFunctionExtractDataMatrixValue() )

        .put( V_BRACE, new ProgramRuleVariable() )
        .put( HASH_BRACE, new Variable() )
        .put( C_BRACE, new ProgramRuleConstant() )
        .put( A_BRACE, new Variable() )
        .put( X_BRACE, new ProgramRuleCustomVariable() )

        .put( PAREN, new OperatorGroupingParentheses() )
        .put( PLUS, new OperatorMathPlus() )
        .put( MINUS, new OperatorMathMinus() )
        .put( POWER, new OperatorMathPower() )
        .put( MUL, new OperatorMathMultiply() )
        .put( DIV, new OperatorMathDivide() )
        .put( MOD, new OperatorMathModulus() )
        .put( NOT, new OperatorLogicalNot() )
        .put( EXCLAMATION_POINT, new OperatorLogicalNot() )
        .put( AND, new OperatorLogicalAnd() )
        .put( AMPERSAND_2, new OperatorLogicalAnd() )
        .put( OR, new OperatorLogicalOr() )
        .put( VERTICAL_BAR_2, new OperatorLogicalOr() )

        // Comparison operators

        .put( EQ, new OperatorCompareEqual() )
        .put( NE, new OperatorCompareNotEqual() )
        .put( GT, new OperatorCompareGreaterThan() )
        .put( LT, new OperatorCompareLessThan() )
        .put( GEQ, new OperatorCompareGreaterThanOrEqual() )
        .put( LEQ, new OperatorCompareLessThanOrEqual() )

        // Common ANTLR operators
        //.putAll( ANTLR_EXPRESSION_ITEMS  )
        .build();

    public static final String ENV_VAR_CURRENT_DATE = "current_date";
    public static final String ENV_VAR_COMPLETED_DATE = "completed_date";
    public static final String ENV_VAR_EVENT_DATE = "event_date";
    public static final String ENV_VAR_EVENT_COUNT = "event_count";
    public static final String ENV_VAR_DUE_DATE = "due_date";
    public static final String ENV_VAR_EVENT_ID = "event_id";
    public static final String ENV_VAR_ENROLLMENT_DATE = "enrollment_date";
    public static final String ENV_VAR_ENROLLMENT_ID = "enrollment_id";
    public static final String ENV_VAR_ENROLLMENT_COUNT = "enrollment_count";
    public static final String ENV_VAR_INCIDENT_DATE = "incident_date";
    public static final String ENV_VAR_TEI_COUNT = "tei_count";
    public static final String ENV_VAR_EVENT_STATUS = "event_status";
    public static final String ENV_VAR_OU = "org_unit";
    public static final String ENV_VAR_ENROLLMENT_STATUS = "enrollment_status";
    public static final String ENV_VAR_PROGRAM_STAGE_ID = "program_stage_id";
    public static final String ENV_VAR_PROGRAM_STAGE_NAME = "program_stage_name";
    public static final String ENV_VAR_PROGRAM_NAME = "program_name";
    public static final String ENV_VAR_ENVIRONMENT = "environment";
    public static final String ENV_VAR_OU_CODE = "orgunit_code";

    // new environment variable must be added in this map
    public static final ImmutableMap<String, ItemValueType> ENV_VARIABLES = new ImmutableMap.Builder<String, ItemValueType>()
            .put( ENV_VAR_COMPLETED_DATE, ItemValueType.DATE )
            .put( ENV_VAR_CURRENT_DATE, ItemValueType.DATE )
            .put( ENV_VAR_EVENT_DATE, ItemValueType.DATE )
            .put( ENV_VAR_INCIDENT_DATE, ItemValueType.DATE )
            .put( ENV_VAR_ENROLLMENT_DATE, ItemValueType.DATE )
            .put( ENV_VAR_DUE_DATE, ItemValueType.DATE )
            .put( ENV_VAR_EVENT_COUNT, ItemValueType.NUMBER )
            .put( ENV_VAR_TEI_COUNT, ItemValueType.NUMBER )
            .put( ENV_VAR_ENROLLMENT_COUNT, ItemValueType.NUMBER )
            .put( ENV_VAR_EVENT_ID, ItemValueType.NUMBER )
            .put( ENV_VAR_PROGRAM_STAGE_ID, ItemValueType.NUMBER )
            .put( ENV_VAR_ENROLLMENT_ID, ItemValueType.NUMBER )
            .put( ENV_VAR_ENROLLMENT_STATUS, ItemValueType.TEXT )
            .put( ENV_VAR_EVENT_STATUS, ItemValueType.TEXT )
            .put( ENV_VAR_OU, ItemValueType.TEXT )
            .put( ENV_VAR_OU_CODE, ItemValueType.TEXT )
            .put( ENV_VAR_ENVIRONMENT, ItemValueType.TEXT )
            .put( ENV_VAR_PROGRAM_NAME, ItemValueType.TEXT )
            .put( ENV_VAR_PROGRAM_STAGE_NAME, ItemValueType.TEXT )
            .build();
}
