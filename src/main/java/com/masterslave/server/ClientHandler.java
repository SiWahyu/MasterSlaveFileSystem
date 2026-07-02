package com.masterslave.server;

import java.io.IOException;
import java.net.Socket;

import com.masterslave.protocol.Protocol;
import com.masterslave.server.service.FileTransferService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import com.masterslave.server.service.SearchService;
import com.masterslave.server.service.DownloadService;

/**
 * Menangani komunikasi antara server dan satu client.
 */
public class ClientHandler implements Runnable {

    private final Socket clientSocket;

    private final String clientName;

    private final FileTransferService fileTransferService;

    private final SearchService searchService;

    private final DownloadService downloadService;

    private DataInputStream input;

    private DataOutputStream output;

    public ClientHandler(Socket clientSocket,
                         FileTransferService fileTransferService,
                         SearchService searchService,
                         DownloadService downloadService) {

        this.clientSocket = clientSocket;

        this.fileTransferService = fileTransferService;

        this.searchService = searchService;

        this.downloadService = downloadService;

        this.clientName =
                clientSocket.getInetAddress().getHostAddress();

    }

    @Override
    public void run() {

        try {

            input = new DataInputStream(
                    clientSocket.getInputStream()
            );

            output = new DataOutputStream(
                    clientSocket.getOutputStream()
            );

            while (!clientSocket.isClosed()) {

                String command = input.readUTF();

                switch (command) {

                    case Protocol.UPLOAD ->

                            fileTransferService.upload(
                                    input,
                                    output
                            );

                    case Protocol.SEARCH ->

                            searchService.sendFileList(
                                    output
                            );

                    case Protocol.DOWNLOAD ->

                            downloadService.download(
                                    input,
                                    output
                            );

                    default ->

                            output.writeUTF(
                                    Protocol.FAILED
                            );

                }

            }

        } catch (IOException exception) {

            System.out.println(
                    "Client terputus : "
                            + clientName
            );

        }

    }

    /**
     * Mengembalikan nama client.
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * Menutup koneksi client.
     */
    public void close() {

        try {

            if (clientSocket != null && !clientSocket.isClosed()) {

                clientSocket.close();

            }

        } catch (IOException exception) {

            exception.printStackTrace();

        }

    }

}