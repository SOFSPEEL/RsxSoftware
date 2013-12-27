package com.rsxsoftware.insurance.prefs;

import com.parse.ParseObject;

import java.io.File;

public class PhotoSave {
    public static final String DELIMITER = ":";
    private final String objectId;
    private final String key;
    private final String path;
    private final String className;

    public PhotoSave(ParseObject object, String key, String path) {
        this.objectId = object.getObjectId();
        className = object.getClassName();
        this.key = key;
        this.path = path;
    }

    public PhotoSave(String storageValue) {
        final String[] split = storageValue.split(DELIMITER);
        int i = 0;
        objectId = split[i];
        className = split[++i];
        key = split[++i];
        path = split[++i];
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
        return getObjectId() + DELIMITER + className + DELIMITER + getKey() + DELIMITER + getPath();
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        return createStorageValue();
    }


}
