package com.masterslave.listener;

/**
 * Interface callback untuk menerima event dari MasterServer.
 */
public interface ServerListener {

    /**
     * Dipanggil ketika server berhasil dijalankan.
     */
    void onServerStarted();

    /**
     * Dipanggil ketika server dihentikan.
     */
    void onServerStopped();

    /**
     * Dipanggil ketika ada client baru yang terhubung.
     *
     * @param clientName nama atau alamat client
     * @param totalClient jumlah client yang sedang terhubung
     */
    void onClientConnected(String clientName, int totalClient);

    void onFileUploaded(
            String username,
            String fileName,
            int totalFiles
    );

    void onClientDisconnected(
            String username,
            int totalClients
    );

}