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
            DataOutputStream output,
            String keyword
    ) throws IOException {

        java.util.List<FileInfo> allFiles = fileStorage.getFileInfos();
        java.util.List<FileInfo> filteredFiles = new java.util.ArrayList<>();
        String lowerKeyword = keyword != null ? keyword.toLowerCase() : "";

        for (FileInfo fileInfo : allFiles) {
            if (fileInfo.getFileName().toLowerCase().contains(lowerKeyword)) {
                filteredFiles.add(fileInfo);
            }
        }

        output.writeInt(
                filteredFiles.size()
        );

        for (FileInfo fileInfo : filteredFiles) {

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