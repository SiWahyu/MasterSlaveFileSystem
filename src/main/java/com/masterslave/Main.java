

import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.SwingUtilities;

import com.masterslave.ui.slave.SlaveFrame;


public class Main {

    public static void main(String[] args) {

        FlatIntelliJLaf.setup();

        SwingUtilities.invokeLater(() -> new SlaveFrame());

    }

}