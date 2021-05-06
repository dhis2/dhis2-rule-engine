package org.hisp.dhis.rules;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.antlr.Parser;
import org.hisp.dhis.antlr.ParserExceptionWithoutContext;
import org.hisp.dhis.rules.models.*;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.rules.utils.RuleEngineUtils;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.Callable;

import static org.hisp.dhis.rules.parser.expression.ParserUtils.FUNCTION_EVALUATE;

class RuleEngineExecution
    implements Callable<List<RuleEffect>>
{
    private static final Log log = LogFactory.getLog( RuleEngineExecution.class );

    @Nonnull
    private final Map<String, List<String>> supplementaryData;

    @Nonnull
    private final List<Rule> rules;

    @Nonnull
    private Map<String, RuleVariableValue> valueMap;

    @Nonnull
    private RuleConditionEvaluator ruleConditionEvaluator;

    RuleEngineExecution( @Nonnull List<Rule> rules,
        @Nonnull Map<String, RuleVariableValue> valueMap, Map<String, List<String>> supplementaryData )
    {
        this.valueMap = new HashMap<>( valueMap );
        this.rules = rules;
        this.supplementaryData = supplementaryData;
        this.ruleConditionEvaluator = new RuleConditionEvaluator();
    }

    @Override
    public List<RuleEffect> call()
    {
        return ruleConditionEvaluator.getRuleEffects( valueMap, supplementaryData, this.rules );
    }
}
