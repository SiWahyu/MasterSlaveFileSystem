package com.masterslave.server.service;

import com.masterslave.storage.FileStorage;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Mengirim daftar file yang tersedia
 * pada Master Server.
 */
public class SearchService {

    private final FileStorage fileStorage;

    public SearchService(FileStorage fileStorage) {

        this.fileStorage = fileStorage;

    }

    /**
     * Mengirim seluruh nama file ke Slave.
     */
    public void sendFileList(
            DataOutputStream output
    ) throws IOException {

        File[] files = fileStorage.getAllFiles();

        output.writeInt(files.length);

        for (File file : files) {

            output.writeUTF(file.getName());

        }

        output.flush();

    }

}