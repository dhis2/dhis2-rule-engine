package org.hisp.dhis.rules.gs1;

import java.util.HashMap;
import java.util.Map;

public class GS1ValueFormatter {
    private Map<String, String> dataMap;

    public String formatValue(String value, GS1Elements valueToReturn) {
        dataMap = new HashMap<>();
        String[] gs1Groups = value.split(GS1Elements.GS1_GROUP_SEPARATOR.getElement());
        for (String gs1Group : gs1Groups) {
            handleGroupData(gs1Group);
        }

        return dataMap.get(valueToReturn.getElement());
    }

    private void handleGroupData(String gs1Group) {
        if (!gs1Group.isEmpty()) {
            int gs1GroupLength = gs1Group.length();
            String ai = GS1Elements.getApplicationIdentifier(gs1Group);
            Integer nextValueLength = GS1Table.aiFixedLengthMap().get(ai);
            if (nextValueLength == null) nextValueLength = gs1GroupLength;
            dataMap.put(ai, gs1Group.substring(2, nextValueLength));
            handleGroupData(gs1Group.substring(nextValueLength));
        }
    }

    public boolean isGS1(String value) {
        return value.startsWith(GS1Elements.GS1_DATA_MATRIX_IDENTIFIER.getElement());
    }
}
