package org.hisp.dhis.rules.models;

import java.util.List;

public class RuleEffects
{

    private TrackerObjectType trackerObjectType;

    private String trackerObjectUid;

    private List<RuleEffect> ruleEffects;

    public RuleEffects( TrackerObjectType trackerObjectType, String trackerObjectUid,
        List<RuleEffect> ruleEffects )
    {
        this.trackerObjectType = trackerObjectType;
        this.trackerObjectUid = trackerObjectUid;
        this.ruleEffects = ruleEffects;
    }

    public TrackerObjectType getTrackerObjectType()
    {
        return trackerObjectType;
    }

    public String getTrackerObjectUid()
    {
        return trackerObjectUid;
    }

    public List<RuleEffect> getRuleEffects()
    {
        return ruleEffects;
    }

    public boolean isEnrollment(){
        return this.trackerObjectType == TrackerObjectType.ENROLLMENT;
    }

    public boolean isEvent(){
        return this.trackerObjectType == TrackerObjectType.EVENT;
    }
}
