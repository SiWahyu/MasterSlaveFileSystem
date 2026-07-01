package com.masterslave.protocol;

/**
 * Berisi seluruh perintah komunikasi
 * antara Master dan Slave.
 */
public final class Protocol {

    private Protocol() {
    }

    // Request
    public static final String UPLOAD = "UPLOAD";
    public static final String SEARCH = "SEARCH";
    public static final String DOWNLOAD = "DOWNLOAD";
    public static final String LIST_FILES = "LIST_FILES";

    // Response
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";

}