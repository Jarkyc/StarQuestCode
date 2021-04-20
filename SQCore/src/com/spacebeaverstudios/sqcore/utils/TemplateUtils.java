package com.spacebeaverstudios.sqcore.utils;

import com.spacebeaverstudios.sqcore.SQCore;
import com.spacebeaverstudios.sqcore.objects.Template;

import java.io.*;
import java.util.ArrayList;

public class TemplateUtils {

    private static ArrayList<Template> templates = new ArrayList<>();

    public static void registerTemplate(Template template){
        templates.add(template);
    }

    public static ArrayList<Template> getTemplates(){
        return templates;
    }

    public static Template getTemplateByName(String name){
        for(Template template : templates){
            if(template.name.equalsIgnoreCase(name)) return template;
        }
        return null;
    }

    public static void loadTemplates(){
        File folder = new File(SQCore.getInstance().getDataFolder().getAbsolutePath() + "/templates");
        try{
            for(File file : folder.listFiles()){
                FileInputStream fileIn = new FileInputStream(folder + "/" + file.getName());
                ObjectInputStream ois = new ObjectInputStream(fileIn);
                Template template = (Template) ois.readObject();
                fileIn.close();
                ois.close();
                templates.add(template);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void saveTemplates(){
        for(Template template : templates){
            try{
                FileOutputStream fileOut = new FileOutputStream(SQCore.getInstance().getDataFolder().getAbsolutePath() + "/templates/" + template.name + ".ser");
                ObjectOutputStream oos = new ObjectOutputStream(fileOut);
                oos.writeObject(template);
                oos.close();
                fileOut.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}
