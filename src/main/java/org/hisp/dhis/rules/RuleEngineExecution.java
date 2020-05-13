package org.hisp.dhis.rules;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.antlr.AntlrExprItem;
import org.hisp.dhis.antlr.Parser;
import org.hisp.dhis.rules.functions.*;
import org.hisp.dhis.rules.models.*;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.rules.variables.ProgramRuleConstant;
import org.hisp.dhis.rules.variables.ProgramRuleCustomVariable;
import org.hisp.dhis.rules.variables.ProgramRuleVariable;
import org.hisp.dhis.rules.variables.Variable;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.Callable;

import static org.hisp.dhis.antlr.AntlrParserUtils.ANTLR_EXPRESSION_ITEMS;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.*;
import static org.hisp.dhis.rules.parser.expression.ParserUtils.FUNCTION_EVALUATE;

class RuleEngineExecution
    implements Callable<List<RuleEffect>>
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

        .put( V_BRACE, new ProgramRuleVariable() )
        .put( HASH_BRACE, new Variable() )
        .put( C_BRACE, new ProgramRuleConstant() )
        .put( A_BRACE, new Variable() )
        .put( X_BRACE, new ProgramRuleCustomVariable() )

        .putAll( ANTLR_EXPRESSION_ITEMS )

        .build();

    private static final Log log = LogFactory.getLog( RuleEngineExecution.class );

    @Nonnull
    private final Map<String, List<String>> supplementaryData;

    @Nonnull
    private final List<Rule> rules;

    @Nonnull
    private Map<String, RuleVariableValue> valueMap;

    RuleEngineExecution( @Nonnull List<Rule> rules,
        @Nonnull Map<String, RuleVariableValue> valueMap, Map<String, List<String>> supplementaryData )
    {
        this.valueMap = new HashMap<>( valueMap );
        this.rules = rules;
        this.supplementaryData = supplementaryData;
    }

    @Override
    public List<RuleEffect> call()
    {
        List<RuleEffect> ruleEffects = new ArrayList<>();

        List<Rule> ruleList = new ArrayList<>( rules );

        Collections.sort( ruleList, new Comparator<Rule>()
        {
            @Override
            public int compare( Rule rule1, Rule rule2 )
            {
                Integer priority1 = rule1.priority();
                Integer priority2 = rule2.priority();
                if ( priority1 != null && priority2 != null )
                {
                    return priority1.compareTo( priority2 );
                }
                else if ( priority1 != null )
                {
                    return -1;
                }
                else if ( priority2 != null )
                {
                    return 1;
                }
                else
                {
                    return 0;
                }
            }
        } );

        for ( Rule rule : ruleList )
        {
            log.debug( "Evaluating programrule: " + rule.name() );

            try
            {
                if ( Boolean.valueOf( process( rule.condition() ) ) )
                {
                    for ( RuleAction action : rule.actions() )
                    {

                        //Check if action is assigning value to calculated variable
                        if ( isAssignToCalculatedValue( action ) )
                        {
                            RuleActionAssign ruleActionAssignVariable = (RuleActionAssign) action;
                            updateValueMap( ruleActionAssignVariable.content(), RuleVariableValue.create( process( ruleActionAssignVariable.data() ), RuleValueType.TEXT ) );
                        }
                        else
                        {
                            ruleEffects.add( create( action ) );
                        }
                    }
                }
            }
            catch ( Exception e )
            {
                e.printStackTrace();
                log.error( "Exception in " + rule.name() + ": " + e.getMessage() );
            }
        }

        return ruleEffects;
    }

    private String process( String condition )
    {
        if ( condition.isEmpty() )
        {
            return "";
        }
        CommonExpressionVisitor commonExpressionVisitor = CommonExpressionVisitor.newBuilder()
            .withFunctionMap( FUNCTIONS )
            .withVariablesMap( valueMap )
            .withFunctionMethod( FUNCTION_EVALUATE )
            .withSupplementaryData( supplementaryData )
            .validateCommonProperties();


        return Parser.visit( condition, commonExpressionVisitor ).toString();

    }

    private Boolean isAssignToCalculatedValue( RuleAction ruleAction )
    {
        return ruleAction instanceof RuleActionAssign && ((RuleActionAssign)ruleAction).field().isEmpty() ;
    }

    private void updateValueMap( String variable, RuleVariableValue value )
    {
        valueMap.put( RuleExpression.unwrapVariableName( variable ), value );
    }

    @Nonnull
    private RuleEffect create( @Nonnull RuleAction ruleAction )
    {
        if ( ruleAction instanceof RuleActionAssign )
        {
            RuleActionAssign ruleActionAssign = (RuleActionAssign) ruleAction;
            String data = process( ruleActionAssign.data() );
            updateValueMap( ruleActionAssign.field(), RuleVariableValue.create( data, RuleValueType.TEXT ) );
            return RuleEffect.create( ruleAction, data );
        } else if ( ruleAction instanceof RuleActionData ) {
            return RuleEffect.create( ruleAction, process( ((RuleActionData)ruleAction).data() ) );
        }

        return RuleEffect.create( ruleAction );
    }
}
