package com.quiptmc.fabric.api;

import net.fabricmc.loader.api.metadata.ContactInformation;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;

import java.io.File;
import java.util.Collection;

public class JavaMod {

    private final String name;
    private final String id;
    private final String version;
    private final String description;
    private final Collection<Person> authors;
    private final ContactInformation contact;

    private final File dataFolder;

    public JavaMod(ModMetadata metadata){
        this.name = metadata.getName();
        this.id = metadata.getId();
        this.version = metadata.getVersion().getFriendlyString();
        this.description = metadata.getDescription();
        this.authors = metadata.getAuthors();
        this.contact = metadata.getContact();
        this.dataFolder = new File("config/" + name +"/");
        if(!dataFolder.exists()) dataFolder.mkdirs();
    }

    public JavaMod(String name){
        this.name = name;
        this.id = name.toLowerCase();
        this.version = "1.0.0";
        this.description = "No description provided.";
        this.authors = null;
        this.contact = null;
        this.dataFolder = new File("config/" + name +"/");
        if(!dataFolder.exists()) dataFolder.mkdirs();
    }

    public JavaMod(String name, String version){
        this.name = name;
        this.id = name.toLowerCase();
        this.version = version;
        this.description = "No description provided.";
        this.authors = null;
        this.contact = null;
        this.dataFolder = new File("config/" + name +"/");
        if(!dataFolder.exists()) dataFolder.mkdirs();
    }
    public JavaMod(String name, String version, String id){
        this.name = name;
        this.id = id;
        this.version = version;
        this.description = "No description provided.";
        this.authors = null;
        this.contact = null;
        this.dataFolder = new File("config/" + name +"/");
        if(!dataFolder.exists()) dataFolder.mkdirs();
    }

    public JavaMod(String name, String version, String id, String description){
        this.name = name;
        this.id = id;
        this.version = version;
        this.description = description;
        this.authors = null;
        this.contact = null;
        this.dataFolder = new File("config/" + name +"/");
        if(!dataFolder.exists()) dataFolder.mkdirs();
    }

    public JavaMod(String name, String version, String id, String description, Collection<Person> authors, ContactInformation contact){
        this.name = name;
        this.id = id;
        this.version = version;
        this.description = description;
        this.authors = authors;
        this.contact = contact;
        this.dataFolder = new File("config/" + name +"/");
        if(!dataFolder.exists()) dataFolder.mkdirs();
    }


    public String name(){
        return name;
    }

    public File dataFolder(){
        return dataFolder;
    }

    public String id(){
        return id;
    }

    public String version(){
        return version;
    }

    public String description(){
        return description;
    }

    public Collection<Person> authors(){
        return authors;
    }

    public ContactInformation contact(){
        return contact;
    }


}

