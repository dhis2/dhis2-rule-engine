package org.hisp.dhis.rules;


/**
 * @author rajazubair
 */

public class Option
{
    private String name;

    private String code;

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

    public static DataItem.Builder builder()
    {
        return new DataItem.Builder();
    }

    public static class Builder
    {
        private String name;
        private String code;

        public Option.Builder name(String name )
        {
            this.name = name;
            return this;
        }

        public Option.Builder code(String code )
        {
            this.code = code;
            return this;
        }

        public Option build()
        {
            return new Option(name, code );
        }
    }
}
