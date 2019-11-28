package org.hisp.dhis.parser.expression;

/*
 * Copyright (c) 2004-2019, University of Oslo
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

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang3.Validate;
import org.hisp.dhis.common.DimensionalItemId;
import org.hisp.dhis.parser.expression.antlr.ExpressionBaseVisitor;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;
import org.hisp.dhis.parser.expression.literal.DefaultLiteral;
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.functions.RuleFunction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.hisp.dhis.parser.expression.ParserUtils.DOUBLE_VALUE_IF_NULL;
import static org.hisp.dhis.parser.expression.ParserUtils.ITEM_REGENERATE;
import static org.hisp.dhis.parser.expression.ParserUtils.castBoolean;
import static org.hisp.dhis.parser.expression.ParserUtils.castDouble;
import static org.hisp.dhis.parser.expression.ParserUtils.castString;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.BooleanLiteralContext;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExpressionContext;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ItemContext;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ItemNumStringLiteralContext;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.NumericLiteralContext;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.StringLiteralContext;

/**
 * Common traversal of the ANTLR4 org.hisp.dhis.parser.expression parse tree using the
 * visitor pattern.
 *
 * @author Jim Grace
 */
public class CommonExpressionVisitor
    extends ExpressionBaseVisitor<Object>
{
    /**
     * Map of ExprFunction instances to call for each org.hisp.dhis.parser.expression function
     */
    private Map<Integer, ExprFunction> functionMap;

    /**
     * Map of ExprItem instances to call for each org.hisp.dhis.parser.expression item
     */
    private Map<Integer, ExprItem> itemMap;

    /**
     * Method to call within the ExprFunction instance
     */
    private ExprFunctionMethod functionMethod;

    /**
     * Method to call within the ExprItem instance
     */
    private ExprItemMethod itemMethod;

    /**
     * Instance to call for each literal
     */
    private ExprLiteral expressionLiteral = new DefaultLiteral();

    /**
     * By default, replace nulls with 0 or ''.
     */
    private boolean replaceNulls = true;

    /**
     * Used to collect the string replacements to build a description.
     */
    private Map<String, String> itemDescriptions = new HashMap<>();

    /**
     * Constants to use in evaluating an org.hisp.dhis.parser.expression.
     */
    private Map<String, Double> constantMap = new HashMap<>();

    /**
     * Used to collect the dimensional item ids in the expression.
     */
    private Set<DimensionalItemId> itemIds = new HashSet<>();

    /**
     * Used to collect the sampled dimensional item ids in the expression.
     */
    private Set<DimensionalItemId> sampleItemIds = new HashSet<>();

    /**
     * Used to collect the organisation unit group ids in the org.hisp.dhis.parser.expression.
     */
    private Set<String> orgUnitGroupIds = new HashSet<>();

    /**
     * Organisation unit group counts to use in evaluating an org.hisp.dhis.parser.expression.
     */
    Map<String, Integer> orgUnitCountMap = new HashMap<>();

    /**
     * Count of days in period to use in evaluating an org.hisp.dhis.parser.expression.
     */
    private Double days = null;

    /**
     * Values to use for dimensional items in evaluating an org.hisp.dhis.parser.expression.
     */
    private Map<String, Double> itemValueMap;

    /**
     * Values to use for variables in evaluating an org.hisp.dhis.parser.expression.
     */
    private Map<String, RuleVariableValue> valueMap;

    /**
     * Supplementary data for users and org units
     */
    private Map<String, List<String>> supplementaryData;

    /**
     * Count of dimension items found.
     */
    private int itemsFound = 0;

    /**
     * Count of dimension item values found.
     */
    private int itemValuesFound = 0;

    /**
     * Reporting start date.
     */
    private Date reportingStartDate;

    /**
     * Reporting end date.
     */
    private Date reportingEndDate;

    /**
     * Idenfitiers of DataElements and Attribuetes in org.hisp.dhis.parser.expression.
     */
    private Set<String> dataElementAndAttributeIdentifiers;

    /**
     * Default value for data type double.
     */
    public static final double DEFAULT_DOUBLE_VALUE = 1d;

    /**
     * Default value for data type date.
     */
    public static final String DEFAULT_DATE_VALUE = "2017-07-08";

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    protected CommonExpressionVisitor()
    {
    }

    /**
     * Creates a new Builder for CommonExpressionVisitor.
     *
     * @return a Builder for CommonExpressionVisitor.
     */
    public static Builder newBuilder()
    {
        return new CommonExpressionVisitor.Builder();
    }

    // -------------------------------------------------------------------------
    // Visitor methods
    // -------------------------------------------------------------------------

    @Override
    public final Object visitExpression( ExpressionContext ctx )
    {
        return visit( ctx.expr() );
    }

    @Override
    public Object visitExpr( ExprContext ctx )
    {
        if ( itemMethod == ITEM_REGENERATE )
        {
            return regenerateAllChildren( ctx );
        }

        if ( ctx.d2Function() != null )
        {
            List<String> arguments = new ArrayList<>();
            for ( ExprContext exprContext : ctx.d2Function().arguments().expr() )
            {
                arguments.add( visit( exprContext ).toString() );
            }

            RuleFunction function = RuleFunction.create( ctx.d2Function().ID().getText().substring( 0, ctx.d2Function().ID().getText().length() - 1 ) );

            if ( function == null )
            {
                throw new ParserExceptionWithoutContext( "Function " + ctx.d2Function().getText() + " not supported" );
            }

            return function.evaluate( arguments, valueMap, supplementaryData );
        }

        if ( ctx.fun != null )
        {
            ExprFunction function = functionMap.get( ctx.fun.getType() );

            if ( function == null )
            {
                throw new ParserExceptionWithoutContext( "Function " + ctx.fun.getText() + " not supported for this type of org.hisp.dhis.parser.expression" );
            }

            return functionMethod.apply( function, ctx, this );
        }

        if ( ctx.expr().size() > 0 ) // If there's an expr, visit the expr
        {
            return visit( ctx.expr( 0 ) );
        }

        return visit( ctx.getChild( 0 ) ); // All others: visit first child.
    }

    @Override
    public Object visitItem( ItemContext ctx )
    {
        ExprItem item = itemMap.get( ctx.it.getType() );

        if ( item == null )
        {
            throw new ParserExceptionWithoutContext( "Item " + ctx.it.getText() + " not supported for this type of org.hisp.dhis.parser.expression" );
        }

        return itemMethod.apply( item, ctx, this );
    }

    @Override
    public Object visitVariable( ExpressionParser.VariableContext ctx )
    {
        RuleVariableValue variableValue = valueMap.get( ctx.string.getText() );
        String variable = variableValue.value() == null ?
            variableValue.type().defaultValue() : variableValue.value();

        if ( variable == null )
        {
            throw new ParserExceptionWithoutContext( "Variable " + ctx.var.getText() + " not present" );
        }

        return variable;
    }

    @Override
    public Object visitNumericLiteral( NumericLiteralContext ctx )
    {
        return expressionLiteral.getNumericLiteral( ctx );
    }

    @Override
    public Object visitStringLiteral( StringLiteralContext ctx )
    {
        return expressionLiteral.getStringLiteral( ctx );
    }

    @Override
    public Object visitBooleanLiteral( BooleanLiteralContext ctx )
    {
        return expressionLiteral.getBooleanLiteral( ctx );
    }

    @Override
    public Object visitEmpty( ExpressionParser.EmptyContext ctx )
    {
        return "";
    }

    @Override
    public Object visitTerminal( TerminalNode node )
    {
        return node.getText(); // Needed to regenerate an org.hisp.dhis.parser.expression.
    }

    // -------------------------------------------------------------------------
    // Logic for functions and items
    // -------------------------------------------------------------------------

    /**
     * Visits a context and casts the result as Double.
     *
     * @param ctx any context
     * @return the Double value
     */
    public Double castDoubleVisit( ParseTree ctx )
    {
        return castDouble( visit( ctx ) );
    }

    /**
     * Visits a context and casts the result as String.
     *
     * @param ctx any context
     * @return the Double value
     */
    public String castStringVisit( ParseTree ctx )
    {
        return castString( visit( ctx ) );
    }

    /**
     * Visits a context and casts the result as Boolean.
     *
     * @param ctx any context
     * @return the Boolean value
     */
    public Boolean castBooleanVisit( ParseTree ctx )
    {
        return castBoolean( visit( ctx ) );
    }

    /**
     * Visits a context while allowing null values (not replacing them
     * with 0 or ''), even if we would otherwise be replacing them.
     *
     * @param ctx any context
     * @return the value while allowing nulls
     */
    public Object visitAllowingNulls( ParserRuleContext ctx )
    {
        boolean savedReplaceNulls = replaceNulls;

        replaceNulls = false;

        Object result = visit( ctx );

        replaceNulls = savedReplaceNulls;

        return result;
    }

    /**
     * Gets the value of an item or numeric string literal
     *
     * If an item, gets the value while allowing nulls.
     *
     * @param ctx item or numeric string literal context
     * @return the value of the item or numeric string literal
     */
    public Object getItemNumStringLiteral( ItemNumStringLiteralContext ctx )
    {
        if ( ctx.item() != null )
        {
            return visitAllowingNulls( ctx.item() );
        }
        else if ( ctx.numStringLiteral().stringLiteral() != null )
        {
            return expressionLiteral.getStringLiteral( ctx.numStringLiteral().stringLiteral() );
        }

        return ctx.getText();
    }

    /**
     * Handles nulls and missing values.
     * <p/>
     * If we should replace nulls with the default value, then do so, and
     * remember how many items found, and how many of them had values, for
     * subsequent MissingValueStrategy analysis.
     * <p/>
     * If we should not replace nulls with the default value, then don't,
     * as this is likely for some function that is testing for nulls, and
     * a missing value should not count towards the MissingValueStrategy.
     *
     * @param value the (possibly null) value
     * @return the value we should return.
     */
    public Object handleNulls( Object value )
    {
        if ( replaceNulls )
        {
            itemsFound++;

            if ( value == null )
            {
                return DOUBLE_VALUE_IF_NULL;
            }
            else
            {
                itemValuesFound++;
            }
        }

        return value;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public Date getReportingStartDate()
    {
        return reportingStartDate;
    }

    public void setReportingStartDate( Date reportingStartDate )
    {
        this.reportingStartDate = reportingStartDate;
    }

    public Date getReportingEndDate()
    {
        return reportingEndDate;
    }

    public void setReportingEndDate( Date reportingEndDate )
    {
        this.reportingEndDate = reportingEndDate;
    }

    public Set<String> getDataElementAndAttributeIdentifiers()
    {
        return dataElementAndAttributeIdentifiers;
    }

    public void setDataElementAndAttributeIdentifiers(
        Set<String> dataElementAndAttributeIdentifiers )
    {
        this.dataElementAndAttributeIdentifiers = dataElementAndAttributeIdentifiers;
    }

    public Map<String, String> getItemDescriptions()
    {
        return itemDescriptions;
    }

    public Map<String, Double> getConstantMap()
    {
        return constantMap;
    }

    public void setConstantMap( Map<String, Double> constantMap )
    {
        this.constantMap = constantMap;
    }

    public boolean getReplaceNulls()
    {
        return replaceNulls;
    }

    public void setExpressionLiteral( ExprLiteral expressionLiteral )
    {
        this.expressionLiteral = expressionLiteral;
    }

    public Set<DimensionalItemId> getItemIds()
    {
        return itemIds;
    }

    public void setItemIds(Set<DimensionalItemId> itemIds )
    {
        this.itemIds = itemIds;
    }

    public Set<DimensionalItemId> getSampleItemIds()
    {
        return sampleItemIds;
    }

    public void setSampleItemIds(Set<DimensionalItemId> sampleItemIds )
    {
        this.sampleItemIds = sampleItemIds;
    }

    public Set<String> getOrgUnitGroupIds()
    {
        return orgUnitGroupIds;
    }

    public Map<String, Integer> getOrgUnitCountMap()
    {
        return orgUnitCountMap;
    }

    public void setOrgUnitCountMap( Map<String, Integer> orgUnitCountMap )
    {
        this.orgUnitCountMap = orgUnitCountMap;
    }

    public Map<String, Double> getItemValueMap()
    {
        return itemValueMap;
    }

    public void setValueMap( Map<String, RuleVariableValue> valueMap )
    {

        this.valueMap = valueMap;
    }

    public Map<String, RuleVariableValue> getValueMap()
    {
        return valueMap;
    }

    public void setItemValueMap( Map<String, Double> itemValueMap )
    {
        this.itemValueMap = itemValueMap;
    }

    public Double getDays()
    {
        return days;
    }

    public void setDays( Double days )
    {
        this.days = days;
    }

    public int getItemsFound()
    {
        return itemsFound;
    }

    public int getItemValuesFound()
    {
        return itemValuesFound;
    }

    // -------------------------------------------------------------------------
    // Builder
    // -------------------------------------------------------------------------

    /**
     * Builder for {@link CommonExpressionVisitor} instances.
     */
    public static class Builder
    {
        private CommonExpressionVisitor visitor;

        protected Builder()
        {
            this.visitor = new CommonExpressionVisitor();
        }

        public Builder withFunctionMap( Map<Integer, ExprFunction> functionMap )
        {
            this.visitor.functionMap = functionMap;
            return this;
        }

        public Builder withItemMap( Map<Integer, ExprItem> itemMap )
        {
            this.visitor.itemMap = itemMap;
            return this;
        }

        public Builder withFunctionMethod( ExprFunctionMethod functionMethod )
        {
            this.visitor.functionMethod = functionMethod;
            return this;
        }

        public Builder withItemMethod( ExprItemMethod itemMethod )
        {
            this.visitor.itemMethod = itemMethod;
            return this;
        }

        public Builder withVariablesMap( Map<String, RuleVariableValue> valueMap )
        {
            this.visitor.valueMap = valueMap;
            return this;
        }

        public Builder withSupplementaryData( Map<String, List<String>> supplementaryData )
        {
            this.visitor.supplementaryData = supplementaryData;
            return this;
        }

        private String getValue( RuleVariableValue variableValue )
        {
            return variableValue.value() == null ?
                variableValue.type().defaultValue() : variableValue.value();
        }

        public CommonExpressionVisitor validateCommonProperties()
        {
            Validate.notNull( this.visitor.functionMap, "Missing required property 'functionMap'" );
            Validate.notNull( this.visitor.itemMap, "Missing required property 'itemMap'" );
            Validate.notNull( this.visitor.functionMethod, "Missing required property 'functionMethod'" );
            Validate.notNull( this.visitor.itemMethod, "Missing required property 'itemMethod'" );

            return visitor;
        }
    }

    // -------------------------------------------------------------------------
    // Supportive Methods
    // -------------------------------------------------------------------------

    /**
     * Regenerates an org.hisp.dhis.parser.expression by visiting all the children of the
     * org.hisp.dhis.parser.expression node (including any terminal nodes).
     *
     * @param ctx the org.hisp.dhis.parser.expression context
     * @return the regenerated org.hisp.dhis.parser.expression (as a String)
     */
    private Object regenerateAllChildren( ExprContext ctx )
    {
        StringBuilder stringBuilder = new StringBuilder();
        for ( ParseTree child : ctx.children )
        {
            stringBuilder.append( this.castStringVisit( child ) );
        }

        return stringBuilder.toString();
    }
}
