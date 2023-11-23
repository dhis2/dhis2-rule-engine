package org.hisp.dhis.rules.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.CheckForNull;;
import javax.annotation.CheckForNull;

public class SupplementaryData
{
    private List<String> userRoles;
    private List<String> androidVersion;
    private Map<String, List<String>> orgUnitGroups;

    public enum SupplementaryDataKey
    {
        USER( "USER" ),
        ANDROID_VERSION( "android_version" );

        private String clientName;

        SupplementaryDataKey( String clientName )
        {
            this.clientName = clientName;
        }

        public String getClientName() {
            return clientName;
        }
    }

    private SupplementaryData(List<String> userRoles, List<String> androidVersion, Map<String, List<String>> orgUnitGroups) {
        this.userRoles = userRoles;
        this.androidVersion = androidVersion;
        this.orgUnitGroups =  orgUnitGroups;
    }

    public Integer getAndroidVersion()
    {
        return Integer.parseInt( androidVersion.get( 0 ) );
    }

    public List<String> getUserRoles()
    {
        return userRoles;
    }

    public List<String> getOrgUnitsInOrgUnitGroup( String orgUnitGroupCodeOrUid )
    {
        return orgUnitGroups.get( orgUnitGroupCodeOrUid );
    }

    public static class Builder {
        private List<String> userRoles;
        private List<String> androidVersion;
        private Map<String, List<String>> orgUnitGroups;

        public Builder setUserRoles( List<String> userRoleUids )
        {
            this.userRoles = userRoleUids;
            return this;
        }

        public Builder setAndroidVersion( List<String> androidVersion )
        {
            this.androidVersion = androidVersion;
            return this;
        }

        public Builder setOrgUnitGroups(String orgUnitGroupUid, @CheckForNull String orgUnitGroupCode, List<String> orgUnitUids ) {
            if ( this.orgUnitGroups == null )
            {
                this.orgUnitGroups = new HashMap<>();
            }
            if ( orgUnitGroupCode != null )
            {
                this.orgUnitGroups.put( orgUnitGroupCode, orgUnitUids );
            }
            this.orgUnitGroups.put( orgUnitGroupUid, orgUnitUids );
            return this;
        }

        public SupplementaryData build()
        {
            if(androidVersion == null){
                androidVersion = new ArrayList<>();
                androidVersion.add("-1");
            }

            if( userRoles == null ){
                userRoles = new ArrayList<>();
            }

            if( orgUnitGroups == null )
            {
                orgUnitGroups = new HashMap<>();
            }

            return new SupplementaryData( userRoles, androidVersion, orgUnitGroups );
        }
    }
}
