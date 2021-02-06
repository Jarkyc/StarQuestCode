package com.spacebeaverstudios.sqbaseclasses.commands.arguments;

import java.util.List;

public abstract class Argument<T> {
    private final String name;

    public Argument (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract List<String> getSuggestions();
    public abstract T parse (String arg);
}