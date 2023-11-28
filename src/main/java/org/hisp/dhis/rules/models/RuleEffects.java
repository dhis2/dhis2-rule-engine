package org.hisp.dhis.rules.models;

import javax.annotation.Nonnull;
import java.util.List;

public record RuleEffects(
    @Nonnull TrackerObjectType trackerObjectType,
    @Nonnull String trackerObjectUid,
    @Nonnull List<RuleEffect> ruleEffects)
{
    public boolean isEnrollment(){
        return this.trackerObjectType == TrackerObjectType.ENROLLMENT;
    }

    public boolean isEvent(){
        return this.trackerObjectType == TrackerObjectType.EVENT;
    }
}
