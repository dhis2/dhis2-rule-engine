package org.dhis2.ruleengine.gs1

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.IllegalStateException

@RunWith(JUnit4::class)
class GS1DataMatrixValueFormatterTest {
    @Test
    fun shouldReturnGs1DataMatrixFormatter() {
        val testValue = "]d2\u001D01084700069915412110081996195256\u001D10DXB2005\u001D17220228"
        val formatter = GS1BaseValueFormatter.getFormatterFromValue(testValue)
        Assertions.assertThat(formatter).isInstanceOf(
            GS1DataMatrixValueFormatter::class.java
        )
    }

    @Test
    fun shouldFormatToDataMatrixValues() {
        val testValue = "]d2\u001D01084700069915412110081996195256\u001D10DXB2005\u001D17220228"
        val gtin = GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.GTIN)
        val lotNumber = GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.LOT_NUMBER)
        val expDate = GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.EXP_DATE)
        val serialNumber = GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.SERIAL_NUMBER)
        Assertions.assertThat(gtin).isEqualTo("08470006991541")
        Assertions.assertThat(serialNumber).isEqualTo("10081996195256")
        Assertions.assertThat(lotNumber).isEqualTo("DXB2005")
        Assertions.assertThat(expDate).isEqualTo("220228")
    }

    @Test
    fun shouldFormatToDataMatrixValuesWithoutInitialGS() {
        val testValue = "]d201084700069915412110081996195256\u001D10DXB2005\u001D17220228"
        val gtin = GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.GTIN)
        val lotNumber = GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.LOT_NUMBER)
        val expDate = GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.EXP_DATE)
        val serialNumber = GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.SERIAL_NUMBER)
        Assertions.assertThat(gtin).isEqualTo("08470006991541")
        Assertions.assertThat(serialNumber).isEqualTo("10081996195256")
        Assertions.assertThat(lotNumber).isEqualTo("DXB2005")
        Assertions.assertThat(expDate).isEqualTo("220228")
    }

    @Test
    fun shouldThrowExceptionIfValueIsNotAvailable() {
        val testValue = "]d201084700069915412110081996195256\u001D10DXB2005\u001D17220228"
        assertThrows<IllegalArgumentException> {
            GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.BILL_TO_LOC)
        }
    }

    @Test
    fun shouldThrowExceptionIfNoApplicationIdentifierIsFound() {
        val testValue = "]d2084700069915412110081996195256\u001DDXB2005\u001D220228"
        assertThrows<IllegalArgumentException> {
            GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.BILL_TO_LOC)
        }
    }
}