package com.spacebeaverstudios.sqbaseclasses.commands.arguments;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class BooleanArgument extends Argument<Boolean>{
    public BooleanArgument (String name) {
        super(name);
    }

    @Override
    public List<String> getSuggestions() {
        List<String> suggestions = new ArrayList<String>();

        suggestions.add("true");
        suggestions.add("false");

        return suggestions;
    }

    @Override
    public Boolean parse (String arg) {
        if (arg.equalsIgnoreCase("true")) {
            return true;
        } else if (arg.equalsIgnoreCase("false")) {
            return false;
        } else {
            return null;
        }
    }
}