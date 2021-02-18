package com.spacebeaverstudios.sqcore.commands.arguments;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class DoubleArgument extends Argument<Double>{
    public DoubleArgument (String name) {
        super(name);
    }

    @Override
    public List<String> getSuggestions() {
        return new ArrayList<>();
    }

    @Override
    public Double parse(String arg) {
        try {
            return Double.parseDouble(arg);
        } catch (Exception e) {
            return null;
        }
    }
}
