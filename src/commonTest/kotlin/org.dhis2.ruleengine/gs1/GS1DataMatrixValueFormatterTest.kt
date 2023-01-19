package org.dhis2.ruleengine.gs1

import kotlin.test.Test
import kotlin.test.assertTrue

class GS1DataMatrixValueFormatterTest {
    @Test
    fun shouldReturnGs1DataMatrixFormatter() {
        val testValue = "]d2\u001D01084700069915412110081996195256\u001D10DXB2005\u001D17220228"
        val formatter = GS1BaseValueFormatter.getFormatterFromValue(testValue)
        assertTrue {
            formatter is GS1DataMatrixValueFormatter
        }
    }

    @Test
    fun shouldFormatToDataMatrixValues() {
        val testValue = "]d2\u001D01084700069915412110081996195256\u001D10DXB2005\u001D17220228"
        val gtin = GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.GTIN)
        val lotNumber = GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.LOT_NUMBER)
        val expDate = GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.EXP_DATE)
        val serialNumber = GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.SERIAL_NUMBER)
        assertTrue {
            gtin == "08470006991541" &&
                    serialNumber == "10081996195256" &&
                    lotNumber == "DXB2005" &&
                    expDate == "220228"
        }
    }

    @Test
    fun shouldFormatToDataMatrixValuesWithoutInitialGS() {
        val testValue = "]d201084700069915412110081996195256\u001D10DXB2005\u001D17220228"
        val gtin = GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.GTIN)
        val lotNumber = GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.LOT_NUMBER)
        val expDate = GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.EXP_DATE)
        val serialNumber = GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.SERIAL_NUMBER)
        assertTrue { gtin == "08470006991541" }
        assertTrue { serialNumber == "10081996195256" }
        assertTrue { lotNumber == "DXB2005" }
        assertTrue { expDate == "220228" }
    }

    @Test
    fun shouldThrowExceptionIfValueIsNotAvailable() {
        val testValue = "]d201084700069915412110081996195256\u001D10DXB2005\u001D17220228"
        assertTrue {
            try {
                GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.BILL_TO_LOC)
                true
            } catch (e: Exception) {
                e is IllegalArgumentException
            }
        }
    }

    @Test
    fun shouldThrowExceptionIfNoApplicationIdentifierIsFound() {
        val testValue = "]d2084700069915412110081996195256\u001DDXB2005\u001D220228"
        assertTrue {
            try {
                GS1DataMatrixValueFormatter().formatValue(testValue, GS1Elements.BILL_TO_LOC)
                true
            } catch (e: Exception) {
                e is IllegalArgumentException
            }
        }
    }
}