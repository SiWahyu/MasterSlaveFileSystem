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


    public static final Color BACKGROUND =
            new Color(245, 247, 250);

    public static final Color CARD_BACKGROUND =
            new Color(255, 255, 255, 230); // Slight transparency

    public static final Color PRIMARY =
            new Color(0, 120, 215); // Windows 11 Blue

    public static final Color SUCCESS =
            new Color(16, 137, 62); // Start green

    public static final Color ERROR = 
            new Color(232, 17, 35); // Stop red

    public static final Color TERMINAL_BG =
            new Color(12, 12, 12); // Windows Terminal black

    public static final Color TERMINAL_FG =
            new Color(204, 204, 204); // Windows Terminal white/gray

    public static final Color TEXT =
            new Color(40, 40, 40);

    public static final Color BORDER =
            new Color(220, 220, 220);

}