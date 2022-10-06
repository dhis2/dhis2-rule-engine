package org.hisp.dhis.rules.gs1;

import static org.hisp.dhis.rules.gs1.GS1Elements.BEST_BEFORE_DATE;
import static org.hisp.dhis.rules.gs1.GS1Elements.CONTENT;
import static org.hisp.dhis.rules.gs1.GS1Elements.DUE_DATE;
import static org.hisp.dhis.rules.gs1.GS1Elements.EXP_DATE;
import static org.hisp.dhis.rules.gs1.GS1Elements.GTIN;
import static org.hisp.dhis.rules.gs1.GS1Elements.LOT_NUMBER;
import static org.hisp.dhis.rules.gs1.GS1Elements.PACK_DATE;
import static org.hisp.dhis.rules.gs1.GS1Elements.PROD_DATE;
import static org.hisp.dhis.rules.gs1.GS1Elements.SELL_BY;
import static org.hisp.dhis.rules.gs1.GS1Elements.SERIAL_NUMBER;
import static org.hisp.dhis.rules.gs1.GS1Elements.SSCC;
import static org.hisp.dhis.rules.gs1.GS1Elements.VARIANT;

import java.util.HashMap;
import java.util.Map;

public class GS1Table
{
    public static Map<String, Integer> aiFixedLengthMap(){
        Map<String, Integer> aiFixedLengthMap = new HashMap<>();

        aiFixedLengthMap.put(SSCC.getElement(), 20);
        aiFixedLengthMap.put(GTIN.getElement(), 16);
        aiFixedLengthMap.put(CONTENT.getElement(), 16);
        aiFixedLengthMap.put( "03", 16);
        aiFixedLengthMap.put( "04", 18);
        aiFixedLengthMap.put(PROD_DATE.getElement(), 8);
        aiFixedLengthMap.put(DUE_DATE.getElement(), 8);
        aiFixedLengthMap.put(PACK_DATE.getElement(), 8);
        aiFixedLengthMap.put( "14", 8);
        aiFixedLengthMap.put(BEST_BEFORE_DATE.getElement(), 8);
        aiFixedLengthMap.put(SELL_BY.getElement(), 8);
        aiFixedLengthMap.put(EXP_DATE.getElement(), 8);
        aiFixedLengthMap.put( "18", 8);
        aiFixedLengthMap.put( "19", 8);
        aiFixedLengthMap.put(VARIANT.getElement(), 4);
        aiFixedLengthMap.put( "31", 10);
        aiFixedLengthMap.put( "32", 10);
        aiFixedLengthMap.put( "33", 10);
        aiFixedLengthMap.put( "34", 10);
        aiFixedLengthMap.put( "35", 10);
        aiFixedLengthMap.put( "36", 10);
        aiFixedLengthMap.put( "41", 16);

        return aiFixedLengthMap;
    }

    public static Map<String, Integer> aiNotFixedMaxLengthMap(){
        Map<String, Integer> aiFixedLengthMap = new HashMap<>();

        aiFixedLengthMap.put(LOT_NUMBER.getElement(), 22);
        aiFixedLengthMap.put(SERIAL_NUMBER.getElement(), 22);

        return aiFixedLengthMap;
    }
}
