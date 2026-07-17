package com.masterslave.ui.components;

import com.masterslave.util.UIConstants;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class DashboardCard extends JPanel {

    private final JLabel valueLabel;

    public DashboardCard(String title, String value) {

        setLayout(new BorderLayout(0, 10));
        setBackground(UIConstants.CARD_BACKGROUND);
        putClientProperty(com.formdev.flatlaf.FlatClientProperties.STYLE, "arc: 15");

        setBorder(new CompoundBorder(
                new LineBorder(UIConstants.BORDER),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIConstants.NORMAL_FONT);
        titleLabel.setForeground(Color.GRAY);

        valueLabel = new JLabel(value);
        valueLabel.setFont(UIConstants.CARD_VALUE_FONT);
        valueLabel.setForeground(UIConstants.TEXT);

        add(titleLabel, BorderLayout.NORTH);
        add(valueLabel, BorderLayout.CENTER);
    }

    /**
     * Mengubah nilai yang ditampilkan pada kartu.
     */
    public void setValue(String value) {
        valueLabel.setText(value);
    }
}