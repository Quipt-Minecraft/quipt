package com.quiptmc2.core.resources;

import com.quiptmc2.core.QuiptIntegration;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ResourceManager<I extends ResourceIdentifier, R extends Resource<I>> {

    protected final String name;
    protected final QuiptIntegration integration;
    protected final Map<I, R> resources = new HashMap<>();

    private final Function<Long, I> identifierFunction;
    private Function<I, R> resourceProcessor = null;

    public ResourceManager(QuiptIntegration integration, String name, Function<Long, I> resourceIdentifier) {
        this.name = name;
        this.integration = integration;
        this.identifierFunction = resourceIdentifier;
    }

    public void processor(Function<I, R> processor){
        resourceProcessor = processor;
    }

    public String name(){
        return name;
    }

    public File folder(){
        File folder = new File(integration.folder(), name);
        if(!folder.exists()){
            integration.logger().log("ResourceManager", "Creating resource manager: " + (folder.mkdirs() ? "success" : "failed"));
        }
        return folder;
    }

    public I date(long date) {
        return identifierFunction.apply(date);
    }


    public I now() {
        return date(System.currentTimeMillis());
    }

    public R get(I identifier) {
        if(resourceProcessor == null) throw new IllegalStateException("Resource processor not set");
        if(!resources.containsKey(identifier))
            return create(identifier);
        return resources.get(identifier);
    }

    private R create(I identifier) {
        R resource = resourceProcessor.apply(identifier);
        resources.put(identifier, resource);
        return resource;
    }
}
