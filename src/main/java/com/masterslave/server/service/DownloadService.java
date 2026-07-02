package com.masterslave.server.service;

import com.masterslave.protocol.Protocol;
import com.masterslave.storage.FileStorage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Mengirim file dari Master ke Slave.
 */
public class DownloadService {

    private final FileStorage fileStorage;

    public DownloadService(FileStorage fileStorage) {

        this.fileStorage = fileStorage;

    }

    public void download(
            DataInputStream input,
            DataOutputStream output
    ) throws IOException {

        String fileName = input.readUTF();

        File file = fileStorage.getFile(fileName);

        if (!file.exists()) {

            output.writeUTF(Protocol.FAILED);

            return;

        }

        output.writeUTF(Protocol.SUCCESS);

        output.writeLong(file.length());

        try (FileInputStream fis = new FileInputStream(file)) {

            byte[] buffer = new byte[4096];

            int read;

            while ((read = fis.read(buffer)) != -1) {

                output.write(buffer, 0, read);

            }

        }

        output.flush();

    }

}