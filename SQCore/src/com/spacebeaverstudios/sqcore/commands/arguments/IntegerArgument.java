package com.spacebeaverstudios.sqcore.commands.arguments;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class IntegerArgument extends Argument<Integer> {
    public IntegerArgument(String name) {
        super(name);
    }

    @Override
    public List<String> getSuggestions() {
        return new ArrayList<String>();
    }

    @Override
    public Integer parse(String arg) {
        try {
            return Integer.parseInt(arg);
        } catch (Exception e) {
            return null;
        }
    }
}
