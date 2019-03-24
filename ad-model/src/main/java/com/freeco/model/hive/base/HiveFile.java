package com.freeco.model.hive.base;

import java.util.List;

public class HiveFile<T extends HiveBean> {

    private String fileName;

    private List<T> fileData;

    public HiveFile(String fileName, List<T> fileData) {
        this.fileName = fileName;
        this.fileData = fileData;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<T> getFileData() {
        return fileData;
    }

    public void setFileData(List<T> fileData) {
        this.fileData = fileData;
    }
}
