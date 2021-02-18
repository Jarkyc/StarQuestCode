package com.spacebeaverstudios.sqcore.commands.arguments;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class BooleanArgument extends Argument<Boolean>{
    public BooleanArgument (String name) {
        super(name);
    }

    @Override
    public List<String> getSuggestions() {
        return Arrays.asList("true", "false");
    }

    @Override
    public Boolean parse(String arg) {
        if (arg.equalsIgnoreCase("true")) {
            return true;
        } else if (arg.equalsIgnoreCase("false")) {
            return false;
        } else {
            return null;
        }
    }
}