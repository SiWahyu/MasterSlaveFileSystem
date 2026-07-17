package com.masterslave.storage;

import com.masterslave.common.model.FileInfo;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

/**
 * Mengelola folder penyimpanan file pada Master Server.
 */
public class FileStorage {

    /**
     * Folder tempat seluruh file disimpan.
     */
    public static final String STORAGE_DIRECTORY = "storage/uploads";

    private final List<FileInfo> fileInfos =
            new CopyOnWriteArrayList<>();

    /**
     * Membuat folder penyimpanan apabila belum tersedia.
     */
    public FileStorage() {

        createStorageDirectory();

    }

    /**
     * Membuat folder storage.
     */
    private void createStorageDirectory() {

        File directory = new File(STORAGE_DIRECTORY);

        System.out.println("Storage Path : " + directory.getAbsolutePath());

        if (!directory.exists()) {

            boolean created = directory.mkdirs();

            System.out.println("Folder dibuat : " + created);

        } else {

            System.out.println("Folder sudah ada.");

        }

    }

    /**
     * Mengembalikan lokasi folder upload.
     */
    public File getStorageDirectory() {

        return new File(STORAGE_DIRECTORY);

    }

    /**
     * Mengembalikan file berdasarkan nama file.
     */
    public File getFile(String fileName) {

        return new File(STORAGE_DIRECTORY, fileName);

    }

    /**
     * Mengecek apakah file tersedia.
     */
    public boolean exists(String fileName) {

        return getFile(fileName).exists();

    }

    /**
     * Mengembalikan seluruh file yang ada
     * pada folder storage/uploads.
     */
    public File[] getAllFiles() {

        File[] files = getStorageDirectory().listFiles();

        return files == null ? new File[0] : files;

    }

    public void addFileInfo(
            FileInfo fileInfo
    ) {

        fileInfos.add(fileInfo);

    }

    public List<FileInfo> getFileInfos() {

        return fileInfos;

    }

    public void printMetadata() {

        System.out.println("\n===== METADATA =====");

        for (FileInfo info : fileInfos) {

            System.out.println(
                    info.getFileName()
                            + " | "
                            + info.getUploadedBy()
                            + " | "
                            + info.getFileSize()
                            + " | "
                            + info.getUploadTime()
            );

        }

        System.out.println("====================\n");

    }

    public int getTotalFiles() {

        return fileInfos.size();

    }

}