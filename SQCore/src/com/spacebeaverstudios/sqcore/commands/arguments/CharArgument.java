package com.spacebeaverstudios.sqcore.commands.arguments;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class CharArgument extends Argument<Character> {
    public CharArgument(String name) {
        super(name);
    }

    @Override
    public List<String> getSuggestions() {
        return new ArrayList<>();
    }

    @Override
    public Character parse(String arg) {
        if (arg.length() > 1) {
            return null;
        } else {
            return arg.charAt(0);
        }
    }
}
