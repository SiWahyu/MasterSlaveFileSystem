package com.masterslave.server.service;

import com.masterslave.storage.FileStorage;

import com.masterslave.protocol.Protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import com.masterslave.listener.ServerListener;

/**
 * Menangani proses upload file
 * dari Slave menuju Master.
 */
public class FileTransferService {

    private final FileStorage fileStorage;

    private ServerListener listener;

    public FileTransferService(FileStorage fileStorage) {

        this.fileStorage = fileStorage;

    }

    public void setServerListener(ServerListener listener) {

        this.listener = listener;

    }

    public void upload(
            DataInputStream input,
            DataOutputStream output
    ) throws IOException {

        String fileName = input.readUTF();

        long fileSize = input.readLong();

        File destination =
                fileStorage.getFile(fileName);

        try (FileOutputStream fos =
                     new FileOutputStream(destination)) {

            byte[] buffer = new byte[4096];

            long remaining = fileSize;

            while (remaining > 0) {

                int read = input.read(
                        buffer,
                        0,
                        (int) Math.min(
                                buffer.length,
                                remaining
                        )
                );

                if (read == -1) {

                    break;

                }

                fos.write(buffer, 0, read);

                remaining -= read;

            }

        }

        if (listener != null) {

            listener.onFileUploaded(fileName);

        }

        output.writeUTF(Protocol.SUCCESS);

    }

}