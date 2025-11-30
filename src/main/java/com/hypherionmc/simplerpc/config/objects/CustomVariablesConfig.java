package com.hypherionmc.simplerpc.config.objects;

import shadow.hypherionmc.moonconfig.core.conversion.Path;
import shadow.hypherionmc.moonconfig.core.conversion.SpecComment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HypherionSA
 *
 * Main Config Structure that allows users to add their own RPC placeholders
 */
public final class CustomVariablesConfig {

    @Path("enabled")
    @SpecComment("Must these variables be parsed along with other variables")
    public boolean enabled = true;

    @Path("variables")
    @SpecComment("Your custom variables to add")
    public List<CustomVariable> variables = new ArrayList<>();

    public final static class CustomVariable {
        @Path("name")
        @SpecComment("The name of your variable. Will be parsed into {{custom.variablename}}")
        public String name = "";

        @Path("value")
        @SpecComment("The value that this variable will output")
        public String value = "";
    }

}
