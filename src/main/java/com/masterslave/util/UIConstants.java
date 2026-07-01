package com.masterslave.util;

import java.awt.Color;
import java.awt.Font;

/**
 * Menyimpan konstanta yang digunakan oleh seluruh tampilan aplikasi.
 */
public final class UIConstants {

    private UIConstants() {
    }

    //    Font

    public static final Font TITLE_FONT =
            new Font("Segoe UI", Font.BOLD, 22);

    public static final Font SUBTITLE_FONT =
            new Font("Segoe UI", Font.BOLD, 16);

    public static final Font NORMAL_FONT =
            new Font("Segoe UI", Font.PLAIN, 14);

    public static final Font CARD_VALUE_FONT =
            new Font("Segoe UI", Font.BOLD, 30);

    public static final Font LOG_FONT =
            new Font("Consolas", Font.PLAIN, 13);


    //    color
    public static final Color BACKGROUND =
            new Color(245, 247, 250);

    public static final Color CARD_BACKGROUND =
            Color.WHITE;

    public static final Color PRIMARY =
            new Color(33, 150, 243);

    public static final Color SUCCESS =
            new Color(76, 175, 80);

    public static final Color TEXT =
            new Color(40, 40, 40);

    public static final Color BORDER =
            new Color(220, 220, 220);

}