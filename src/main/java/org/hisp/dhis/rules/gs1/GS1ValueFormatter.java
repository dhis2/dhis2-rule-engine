package org.hisp.dhis.rules.gs1;

public interface GS1ValueFormatter {
    String formatValue( String value, GS1Elements valueToReturn);
    String removeGS1Identifier( String value );
}
