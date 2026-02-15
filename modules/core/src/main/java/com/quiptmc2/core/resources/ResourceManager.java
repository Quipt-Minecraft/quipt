package com.quiptmc2.core.resources;

import com.quiptmc2.core.QuiptIntegration;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;

public class ResourceManager<I extends ResourceIdentifier, R extends Resource<I>> {

    protected final String name;
    protected final QuiptIntegration integration;
    protected final Map<I, R> resources = new HashMap<>();

    private final Function<Long, I> identifierFunction;
    private final File folder;
    private final Class<R> resourceClass;

    public ResourceManager(QuiptIntegration integration, String name, File containerFolder, Function<Long, I> resourceIdentifier, Class<R> resourceClass) {
        this.name = name;
        this.integration = integration;
        this.identifierFunction = resourceIdentifier;
        this.folder = containerFolder;
        this.resourceClass = resourceClass;
        if (!folder().exists())
            integration.logger().log(name + " Resource Manager", "Creating resource root folder: " + (folder().mkdirs() ? "success" : "failed"));

    }

    public String name() {
        return name;
    }

    public File folder() {
        if (!folder.exists()) {
            integration.logger().log(name + " Resource Manager", "Creating resource manager: " + (folder.mkdirs() ? "success" : "failed"));
        }
        return folder;
    }

    public I date(long date) {
        return identifierFunction.apply(date);
    }


    public I now() {
        return date(System.currentTimeMillis());
    }

    public R[] get(I identifier) {
        if (!resources.containsKey(identifier)) {
            for (File file : Objects.requireNonNull(folder().listFiles())) {
                if (file.getName().startsWith(identifier.toString()))
                    load(identifier);
            }
        }
        List<I> list = new ArrayList<>();
        for (I id : resources.keySet()) {
            if (id.toString().startsWith(identifier.toString())) list.add(id);
        }
        @SuppressWarnings("unchecked")
        R[] array = (R[]) Array.newInstance(resourceClass, list.size());
        return list.toArray(array);
    }

    public R create(I identifier) {
        while (resources.containsKey(identifier)) {
            identifier.increment();
        }
        return load(identifier);
    }

    private R load(I identifier) {

        try {
            R resource = resourceClass.getConstructor(identifier.getClass(), File.class).newInstance(identifier, folder());
            resources.put(identifier, resource);
            return resource;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }
}
