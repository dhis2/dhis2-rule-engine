package org.hisp.dhis.rules.gs1;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class GS1DataMatrixValueFormatterTest {

    @Test
    public void shouldReturnGs1DataMatrixFormatter(){
        String testValue = "]d2\u001D01084700069915412110081996195256\u001D10DXB2005\u001D17220228";
        GS1ValueFormatter formatter = GS1BaseValueFormatter.getFormatterFromValue( testValue );
        assertThat( formatter ).isInstanceOf( GS1DataMatrixValueFormatter.class );
    }

    @Test
    public void shouldFormatToDataMatrixValues() {
        String testValue = "]d2\u001D01084700069915412110081996195256\u001D10DXB2005\u001D17220228";
        String gtin = new GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.GTIN);
        String lotNumber = new GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.LOT_NUMBER);
        String expDate = new GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.EXP_DATE);
        String serialNumber = new GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.SERIAL_NUMBER);

        assertThat(gtin).isEqualTo("08470006991541");
        assertThat(serialNumber).isEqualTo("10081996195256");
        assertThat(lotNumber).isEqualTo("DXB2005");
        assertThat(expDate).isEqualTo("220228");
    }

    @Test
    public void shouldFormatToDataMatrixValuesWithoutInitialGS() {
        String testValue = "]d201084700069915412110081996195256\u001D10DXB2005\u001D17220228";
        String gtin = new GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.GTIN);
        String lotNumber = new GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.LOT_NUMBER);
        String expDate = new GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.EXP_DATE);
        String serialNumber = new GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.SERIAL_NUMBER);

        assertThat(gtin).isEqualTo("08470006991541");
        assertThat(serialNumber).isEqualTo("10081996195256");
        assertThat(lotNumber).isEqualTo("DXB2005");
        assertThat(expDate).isEqualTo("220228");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfValueIsNotAvailable() {
        String testValue = "]d201084700069915412110081996195256\u001D10DXB2005\u001D17220228";
        new GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.BILL_TO_LOC);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfNoApplicationIdentifierIsFound() {
        String testValue = "]d2084700069915412110081996195256\u001DDXB2005\u001D220228";
        new GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.BILL_TO_LOC);
    }
}
