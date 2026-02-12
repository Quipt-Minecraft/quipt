package com.quiptmc2.core.resources;

import com.quiptmc2.core.QuiptIntegration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ResourceManager {

    protected final String name;
    protected final QuiptIntegration integration;
    protected final Map<ResourceIdentifier, Resource> resources = new HashMap<>();

    private final Function<Long, ResourceIdentifier> identifierFunction;
    private final File folder;
    private Function<ResourceIdentifier, Resource> resourceProcessor = null;

    public ResourceManager(QuiptIntegration integration, String name, File containerFolder, Function<Long, ResourceIdentifier> resourceIdentifier) {
        this.name = name;
        this.integration = integration;
        this.identifierFunction = resourceIdentifier;
        this.folder = containerFolder;
        if (!folder().exists()) folder().mkdirs();
    }

    public void processor(Function<ResourceIdentifier, Resource> processor) {
        resourceProcessor = processor;
    }

    public String name() {
        return name;
    }

    public File folder() {
        if (!folder.exists()) {
            integration.logger().log("ResourceManager", "Creating resource manager: " + (folder.mkdirs() ? "success" : "failed"));
        }
        return folder;
    }

    public ResourceIdentifier date(long date) {
        return identifierFunction.apply(date);
    }


    public ResourceIdentifier now() {
        return date(System.currentTimeMillis());
    }

    public Resource[] get(ResourceIdentifier identifier) {
        if (resourceProcessor == null) throw new IllegalStateException("Resource processor not set");
        if (!resources.containsKey(identifier)) return new Resource[]{create(identifier)};
        List<ResourceIdentifier> list = new ArrayList<>();
        for(ResourceIdentifier id : resources.keySet()){
            if(id.toString().startsWith(identifier.toString())) list.add(id);
        }
        Resource[] resourceArray = new Resource[list.size()];
        for(int i = 0; i < list.size(); i++){
            resourceArray[i] = resources.get(list.get(i));
        }
        return resourceArray;
    }

    public Resource create(ResourceIdentifier identifier) {
        while(resources.containsKey(identifier)){
            identifier.increment();
        }
        Resource resource = resourceProcessor.apply(identifier);
        resources.put(identifier, resource);
        return resource;
    }
}
