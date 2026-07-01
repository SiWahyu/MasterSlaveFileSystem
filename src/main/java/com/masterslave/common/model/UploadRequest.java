package com.masterslave.common.model;

import java.io.File;

/**
 * Merepresentasikan permintaan upload file dari Slave ke Master.
 */
public class UploadRequest {

    private final File file;

    public UploadRequest(File file) {

        this.file = file;

    }

    public File getFile() {

        return file;

    }

}