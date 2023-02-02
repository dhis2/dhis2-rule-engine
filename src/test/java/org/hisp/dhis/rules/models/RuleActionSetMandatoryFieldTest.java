package org.hisp.dhis.rules.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith( JUnit4.class )
public class RuleActionSetMandatoryFieldTest
{

    @Test( expected = NullPointerException.class )
    public void createMustThrowOnNullArgument()
    {
        RuleActionSetMandatoryField.create( null );
    }

}
