package com.masterslave.common.model;

import java.io.Serializable;

public class FileInfo implements Serializable {

    private String fileName;

    private String uploadedBy;

    private long fileSize;

    private String uploadTime;

    public FileInfo(
            String fileName,
            String uploadedBy,
            long fileSize,
            String uploadTime
    ) {

        this.fileName = fileName;
        this.uploadedBy = uploadedBy;
        this.fileSize = fileSize;
        this.uploadTime = uploadTime;

    }

    public String getFileName() {
        return fileName;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getUploadTime() {
        return uploadTime;
    }

}