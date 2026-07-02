package com.masterslave.server.service;

import com.masterslave.storage.FileStorage;

import java.io.DataOutputStream;
import java.io.IOException;
import com.masterslave.common.model.FileInfo;


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

        output.writeInt(
                fileStorage.getFileInfos().size()
        );

        for (FileInfo fileInfo : fileStorage.getFileInfos()) {

            output.writeUTF(
                    fileInfo.getFileName()
            );

            output.writeUTF(
                    fileInfo.getUploadedBy()
            );

            output.writeLong(
                    fileInfo.getFileSize()
            );

            output.writeUTF(
                    fileInfo.getUploadTime()
            );

        }

    }

}