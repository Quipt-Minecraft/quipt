package com.quiptmc.core2.resources;

import com.quiptmc.core2.data.JsonSerializable;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Resource<I extends ResourceIdentifier> implements JsonSerializable {

    private final I id;
    private final File file;

    public Resource(I id, File managerFolder) {
        this.id = id;
        this.file = new File(managerFolder, id.toString() + ".json");
        fromJson(load());
    }

    public I id() {
        return id;
    }

    private JSONObject load() {
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
