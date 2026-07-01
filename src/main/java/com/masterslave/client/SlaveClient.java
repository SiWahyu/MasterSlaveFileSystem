package com.masterslave.client;

import java.io.IOException;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.masterslave.protocol.Protocol;
import java.io.File;
import java.io.FileInputStream;

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

}