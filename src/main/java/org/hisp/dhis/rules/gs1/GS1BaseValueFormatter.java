package org.hisp.dhis.rules.gs1;

public abstract class GS1BaseValueFormatter
        implements GS1ValueFormatter
{

    public static GS1ValueFormatter getFormatterFromValue( String value )
    {
        if( value == null )
            throw new IllegalArgumentException( "Can't extract data from null value" );

        if( value.length() < 3 )
            throw new IllegalArgumentException( "Value does not contains enough information" );

        String gs1Identifier = value.substring( 0, 3 );
        switch ( GS1Elements.fromKey( gs1Identifier ) ){
            case GS1_d2_IDENTIFIER:
            case GS1_Q3_IDENTIFIER:
                return new GS1DataMatrixValueFormatter();
            case GS1_J1_IDENTIFIER:
            case GS1_d1_IDENTIFIER:
            case GS1_Q1_IDENTIFIER:
            case GS1_E0_IDENTIFIER:
            case GS1_E1_IDENTIFIER:
            case GS1_E2_IDENTIFIER:
            case GS1_E3_IDENTIFIER:
            case GS1_E4_IDENTIFIER:
            case GS1_I1_IDENTIFIER:
            case GS1_C1_IDENTIFIER:
            case GS1_e0_IDENTIFIER:
            case GS1_e1_IDENTIFIER:
            case GS1_e2_IDENTIFIER:
                throw new IllegalArgumentException( String.format( "gs1 identifier %s is not supported", gs1Identifier ) );
            default:
                throw new IllegalArgumentException( "Value does not start with a gs1 identifier" );
        }
    }

    @Override
    public String removeGS1Identifier( String value )
    {
        return value.substring( 3 );
    }
}
