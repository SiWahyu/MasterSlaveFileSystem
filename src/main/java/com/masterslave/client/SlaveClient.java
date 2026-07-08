package com.masterslave.client;

import java.io.IOException;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.masterslave.protocol.Protocol;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.masterslave.common.model.FileInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * Client yang bertugas
 * berkomunikasi dengan Master.
 */
public class SlaveClient {
    private static final int PORT = 5000;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private static final String DOWNLOAD_DIRECTORY =
            System.getProperty("user.home")
                    + File.separator
                    + "Downloads";
    private String username;

    /**
     * Menghubungkan Slave ke Master.
     */
    public void connect(String host) throws IOException {
        socket = new Socket(host, PORT);

        input = new DataInputStream(
                socket.getInputStream()
        );

        output = new DataOutputStream(
                socket.getOutputStream()
        );

        output.writeUTF(Protocol.LOGIN);

        output.writeUTF(username);

        output.flush();
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

    public void upload(File file, java.util.function.Consumer<Integer> progressCallback) throws IOException {
        output.writeUTF(Protocol.UPLOAD);
        output.writeUTF(file.getName());
        output.writeLong(file.length());

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            long totalRead = 0;
            long fileSize = file.length();

            while ((bytesRead = fis.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
                totalRead += bytesRead;
                if (progressCallback != null && fileSize > 0) {
                    int percent = (int) ((totalRead * 100) / fileSize);
                    progressCallback.accept(percent);
                }
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
    public List<FileInfo> search(String keyword) throws IOException {
        output.writeUTF(Protocol.SEARCH);
        output.writeUTF(keyword);

        int total = input.readInt();

        List<FileInfo> files = new ArrayList<>();

        for (int i = 0; i < total; i++) {
            String fileName = input.readUTF();

            String uploadedBy = input.readUTF();

            long fileSize = input.readLong();

            String uploadTime = input.readUTF();

            files.add(
                    new FileInfo(
                            fileName,
                            uploadedBy,
                            fileSize,
                            uploadTime
                    )
            );
}

        return files;
}

    /**
     * Mengunduh file dari Master.
     */
    public void download(String fileName, java.util.function.Consumer<Integer> progressCallback) throws IOException {
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

        System.out.println("DOWNLOAD DIRECTORY : " + DOWNLOAD_DIRECTORY);
        File destination = new File(directory, fileName);
        System.out.println("DESTINATION : " + destination.getAbsolutePath());

        try (FileOutputStream fos = new FileOutputStream(destination)) {
            byte[] buffer = new byte[4096];
            long remaining = fileSize;
            long totalRead = 0;

            while (remaining > 0) {
                int read = input.read(buffer, 0, (int) Math.min(buffer.length, remaining));
                if (read == -1) break;

                fos.write(buffer, 0, read);
                remaining -= read;
                totalRead += read;
                
                if (progressCallback != null && fileSize > 0) {
                    int percent = (int) ((totalRead * 100) / fileSize);
                    progressCallback.accept(percent);
                }
            }
        }

        System.out.println("Download selesai : " + destination.getAbsolutePath());
}

    public void setUsername(String username) {
        this.username = username;
}
}

