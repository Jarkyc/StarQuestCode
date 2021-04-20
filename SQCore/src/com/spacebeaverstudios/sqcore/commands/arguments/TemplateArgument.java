package com.spacebeaverstudios.sqcore.commands.arguments;

import com.spacebeaverstudios.sqcore.objects.template.Template;

import java.util.ArrayList;
import java.util.List;

public class TemplateArgument extends Argument<Template> {
    public TemplateArgument(String name) {
        super(name);
    }

    @Override
    public List<String> getSuggestions() {
        return new ArrayList<>(Template.getTemplates().keySet());
    }

    @Override
    public Template parse(String arg) {
        return Template.getTemplates().get(arg);
    }
}
