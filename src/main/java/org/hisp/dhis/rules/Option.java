package org.hisp.dhis.rules;


/**
 * @author rajazubair
 */

public class Option
{
    private final String name;

    private final String code;

    public Option(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
