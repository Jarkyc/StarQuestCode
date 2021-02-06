package com.spacebeaverstudios.sqbaseclasses.commands.arguments;

import java.util.List;

@SuppressWarnings("unused")
public class SelectionArgument extends Argument<String>{
    private final List<String> list;

    public SelectionArgument (String name, List<String> list) {
        super(name);
        this.list = list;
    }

    @Override
    public List<String> getSuggestions() {
        return list;
    }

    @Override
    public String parse (String arg) {
        for (String item : list) {
            if (item.equalsIgnoreCase(arg)) {
                return item;
            }
        }
        return null;
    }
}
