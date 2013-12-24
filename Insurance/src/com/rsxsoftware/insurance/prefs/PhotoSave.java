package com.rsxsoftware.insurance.prefs;

import java.io.File;

public class PhotoSave {
    public static final String DELIMITER = ":";
    private final String objectId;
    private final String key;
    private final String path;

    public PhotoSave(String objectId, String key, String path) {
        this.objectId = objectId;
        this.key = key;
        this.path = path;
    }

    public PhotoSave(String storageValue) {
        final String[] split = storageValue.split(DELIMITER);
        objectId = split[0];
        key = split[1];
        path = split[2];
    }

    public String getObjectId() {
        return objectId;
    }

    public String getKey() {
        return key;
    }

    public String getPath() {
        return path;
    }

    public File getFilePath() {
        return new File(path);
    }

    public String createStorageValue() {
        return getObjectId() + DELIMITER + getKey() + DELIMITER + getPath();
    }

    @Override
    public String toString() {
        return createStorageValue();
    }
}
