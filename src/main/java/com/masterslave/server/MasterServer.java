package com.masterslave.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import com.masterslave.listener.ServerListener;
import com.masterslave.server.service.FileTransferService;
import com.masterslave.storage.FileStorage;
import java.net.SocketException;
import com.masterslave.server.service.SearchService;
import com.masterslave.server.service.DownloadService;

/**
 * Bertanggung jawab menjalankan server dan menerima koneksi client.
 */
public class MasterServer {

    public static final int PORT = 5000;

    private ServerSocket serverSocket;

    private boolean running;

    // Menyimpan semua client yang sedang terhubung
    private final List<ClientHandler> clients = new ArrayList<>();

    private ServerListener listener;

    private final FileStorage fileStorage = new FileStorage();

    private final FileTransferService
            fileTransferService =
            new FileTransferService(fileStorage);

    private final SearchService
            searchService =
            new SearchService(fileStorage);

    private final DownloadService
            downloadService =
            new DownloadService(fileStorage);

    /**
     * Menjalankan server.
     */
    public void start() {

        if (running) {
            return;
        }

        try {

            serverSocket = new ServerSocket(PORT);

            running = true;

            if (listener != null) {
                listener.onServerStarted();
            }

            System.out.println("Server berjalan di port " + PORT);

            while (running) {

                Socket clientSocket = serverSocket.accept();

                // Membuat thread baru untuk setiap client yang terhubung
                ClientHandler clientHandler =
                        new ClientHandler(
                                this,
                                clientSocket,
                                fileTransferService,
                                searchService,
                                downloadService
                        );

                clients.add(clientHandler);

                Thread clientThread = new Thread(clientHandler);

                clientThread.start();
            }

        } catch (SocketException exception) {

            // Server dihentikan secara normal.
            System.out.println("Server berhenti.");

        }
        catch (IOException exception) {

            exception.printStackTrace();

        }

    }

    /**
     * Menghentikan server.
     */
    /**
     * Menghentikan server beserta seluruh client yang terhubung.
     */
    public void stop() {

        running = false;


        // Menutup seluruh koneksi client
        for (ClientHandler client : clients) {

            client.close();

        }

        clients.clear();

        try {

            if (serverSocket != null && !serverSocket.isClosed()) {

                serverSocket.close();

            }

        } catch (IOException exception) {

            exception.printStackTrace();

        }
        if (listener != null) {
            listener.onServerStopped();
        }


    }

    /**
     * Mengecek apakah server sedang berjalan.
     */
    public boolean isRunning() {

        return running;

    }

    /**
     * Mendaftarkan listener untuk menerima event server.
     */
    public void setServerListener(ServerListener listener) {

        this.listener = listener;

        fileTransferService.setServerListener(listener);

    }

    /**
     * Mengembalikan jumlah client yang sedang terhubung.
     */
    public int getClientCount() {
        return clients.size();
    }

    public synchronized void removeClient(
            ClientHandler clientHandler
    ) {

        clients.remove(clientHandler);

        if (listener != null) {

            listener.onClientDisconnected(
                    clientHandler.getUsername(),
                    clients.size()
            );

        }

    }

    public synchronized void clientLoggedIn(
            ClientHandler clientHandler
    ) {

        if (listener != null) {

            listener.onClientConnected(
                    clientHandler.getUsername(),
                    clients.size()
            );

        }

    }

}