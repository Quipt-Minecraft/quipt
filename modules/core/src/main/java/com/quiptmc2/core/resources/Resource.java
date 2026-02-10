package com.quiptmc2.core.resources;

import com.quiptmc2.core.config.ConfigTemplate;
import com.quiptmc2.core.data.JsonSerializable;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.Objects;

public class Resource<I extends ResourceIdentifier> implements JsonSerializable {

    private final I id;
    private final File file;

    public Resource(I id, File managerFolder) {
        this.id = id;
        this.file = new File(managerFolder, id.toString() + ".json");

                fromJson(load());

//        throw new IllegalArgumentException("Resource with ID " + id + " not found in manager folder");

    }

    private JSONObject load()  {
        try {
            return new JSONObject(new String(Files.readAllBytes(file.toPath())));
        } catch (IOException e) {
            return json();
        }
    }

    public void save() {
        try {
            Files.write(file.toPath(), json().toString(2).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
