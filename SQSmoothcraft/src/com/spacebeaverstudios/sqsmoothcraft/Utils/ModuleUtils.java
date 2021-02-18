package com.spacebeaverstudios.sqsmoothcraft.Utils;

import com.spacebeaverstudios.sqsmoothcraft.Objects.Modules.Module;

import java.util.HashMap;
import java.util.Map;

public class ModuleUtils {

    public static HashMap<String, Module> modulesByName = new HashMap<>();

    public static String getModuleName(Module mod){
        for(Map.Entry<String, Module> entry : modulesByName.entrySet()){
            String name = entry.getKey();
            Module module = entry.getValue();

            if(mod.getClass() == mod.getClass()) return name;

        }
        return null;
    }

    public static Module getModuleByName(String name){
        return modulesByName.get(name.toLowerCase());
    }

}
