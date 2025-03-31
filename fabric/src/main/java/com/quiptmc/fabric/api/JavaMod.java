package com.quiptmc.fabric.api;

import net.fabricmc.loader.api.metadata.ContactInformation;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;

import java.io.File;
import java.util.Collection;

public class JavaMod {

    String name;
    String id;
    String version;
    String description;
    Collection<Person> authors;
    ContactInformation contact;

    File dataFolder;

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

