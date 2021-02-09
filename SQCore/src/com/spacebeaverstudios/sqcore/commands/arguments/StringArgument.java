package com.spacebeaverstudios.sqcore.commands.arguments;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class StringArgument extends Argument<String>{
    public StringArgument (String name) {
        super(name);
    }

    @Override
    public List<String> getSuggestions() {
        return new ArrayList<String>();
    }

    @Override
    public String parse (String arg) {
        return arg;
    }
}
