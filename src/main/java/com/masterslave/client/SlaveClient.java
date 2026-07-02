package com.masterslave.client;

import java.io.IOException;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.masterslave.protocol.Protocol;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Client yang bertugas
 * berkomunikasi dengan Master.
 */
public class SlaveClient {

    private static final String HOST = "localhost";

    private static final int PORT = 5000;

    private Socket socket;

    private DataInputStream input;

    private DataOutputStream output;

    private static final String DOWNLOAD_DIRECTORY = "storage/downloads";

    /**
     * Menghubungkan Slave ke Master.
     */
    public void connect() throws IOException {

        socket = new Socket(HOST, PORT);

        input = new DataInputStream(
                socket.getInputStream()
        );

        output = new DataOutputStream(
                socket.getOutputStream()
        );

    }

    /**
     * Menutup koneksi.
     */
    public void disconnect() throws IOException {

        if (socket != null) {

            socket.close();

        }

    }

    public Socket getSocket() {

        return socket;

    }

    public DataOutputStream getOutput() {

        return output;

    }

    public DataInputStream getInput() {

        return input;

    }

    public void upload(File file) throws IOException {

        output.writeUTF(Protocol.UPLOAD);

        output.writeUTF(file.getName());

        output.writeLong(file.length());

        try (FileInputStream fis = new FileInputStream(file)) {

            byte[] buffer = new byte[4096];

            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {

                output.write(buffer, 0, bytesRead);

            }

        }

        output.flush();

        String response = input.readUTF();

        if (Protocol.SUCCESS.equals(response)) {

            System.out.println("Upload berhasil.");

        } else {

            System.out.println("Upload gagal.");

        }

    }

    /**
     * Meminta daftar file dari Master.
     */
    public String[] search() throws IOException {

        output.writeUTF(Protocol.SEARCH);

        output.flush();

        int totalFile = input.readInt();

        String[] files = new String[totalFile];

        for (int i = 0; i < totalFile; i++) {

            files[i] = input.readUTF();

        }

        return files;

    }

    /**
     * Mengunduh file dari Master.
     */
    public void download(String fileName) throws IOException {

        output.writeUTF(Protocol.DOWNLOAD);

        output.writeUTF(fileName);

        output.flush();

        String response = input.readUTF();

        if (!Protocol.SUCCESS.equals(response)) {

            System.out.println("File tidak ditemukan.");

            return;

        }

        long fileSize = input.readLong();

        File directory = new File(DOWNLOAD_DIRECTORY);

        if (!directory.exists()) {

            directory.mkdirs();

        }

        File destination = new File(directory, fileName);

        try (FileOutputStream fos = new FileOutputStream(destination)) {

            byte[] buffer = new byte[4096];

            long remaining = fileSize;

            while (remaining > 0) {

                int read = input.read(
                        buffer,
                        0,
                        (int) Math.min(buffer.length, remaining)
                );

                if (read == -1) {

                    break;

                }

                fos.write(buffer, 0, read);

                remaining -= read;

            }

        }

        System.out.println("Download selesai : " + destination.getAbsolutePath());

    }

}