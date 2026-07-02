package com.masterslave.storage;

import java.io.File;
import java.io.IOException;

/**
 * Mengelola folder penyimpanan file pada Master Server.
 */
public class FileStorage {

    /**
     * Folder tempat seluruh file disimpan.
     */
    public static final String STORAGE_DIRECTORY = "storage/uploads";

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

}