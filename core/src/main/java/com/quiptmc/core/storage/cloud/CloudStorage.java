package com.quiptmc.core.storage.cloud;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

public class CloudStorage {

    public final Storage storage;

    public final String bucketName;

    public final Settings settings;
    public CloudStorage(Settings settings) {
        this.settings = settings;
        // Constructor logic here
        storage = StorageOptions.newBuilder().setCredentials(settings.credentials).setProjectId(settings.projectId).build().getService();
        bucketName = settings.bucketName;
    }

    public void write(File file) throws IOException {String name = file.getPath().replaceAll("\\\\", "/");
        BlobId blobId = BlobId.of(bucketName, name);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
    }

    public void read(String objectName) throws IOException {
        File destination = new File(objectName);
        if(!destination.getParentFile().exists()) {
            destination.getParentFile().mkdirs();
        }
        BlobId blobId = BlobId.of(bucketName, objectName);
        Blob blob = storage.get(blobId);
        if (blob != null) {
            blob.downloadTo(destination.toPath());
        }
    }

    public class Settings {

        public final String projectId;
        public final String bucketName;
        public final GoogleCredentials credentials;

        public Settings(String projectId, String bucketName, GoogleCredentials credentials) {
            this.projectId = projectId;
            this.bucketName = bucketName;
            this.credentials = credentials;
        }

    }
}
