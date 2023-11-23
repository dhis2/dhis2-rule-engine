package org.hisp.dhis.rules.models;

import java.util.List;

public record RuleEffects(
    TrackerObjectType trackerObjectType,
    String trackerObjectUid,
    List<RuleEffect> ruleEffects)
{
    public boolean isEnrollment(){
        return this.trackerObjectType == TrackerObjectType.ENROLLMENT;
    }

    public boolean isEvent(){
        return this.trackerObjectType == TrackerObjectType.EVENT;
    }
}
