package com.masterslave.ui.slave;

import com.formdev.flatlaf.FlatClientProperties;
import com.masterslave.client.SlaveClient;
import com.masterslave.server.MasterServer;
import com.masterslave.util.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.io.File;



public class SlaveFrame extends JFrame {

    private JTextField hostField;

    private JButton connectButton;

    private JButton browseButton;

    private JButton uploadButton;

    private JLabel statusLabel;

    private final SlaveClient slaveClient = new SlaveClient();

    private File selectedFile;

    private JLabel selectedFileLabel;

    public SlaveFrame() {

        initializeFrame();

    }

    private void initializeFrame() {

        setTitle("Slave Client");

        setSize(450,250);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setContentPane(createContent());

        setVisible(true);



    }

    private JPanel createContent(){

        JPanel panel = new JPanel();

        panel.setLayout(new BorderLayout(15,15));

        panel.setBorder(new EmptyBorder(20,20,20,20));

        panel.setBackground(UIConstants.BACKGROUND);

        JPanel form = new JPanel(new GridLayout(6,1,10,10));

        form.setOpaque(false);

        hostField = new JTextField("localhost");

        hostField.putClientProperty(
                FlatClientProperties.PLACEHOLDER_TEXT,
                "Host");

        connectButton = new JButton("Connect");

        browseButton = new JButton("Browse File");

        uploadButton = new JButton("Upload");

        selectedFileLabel = new JLabel("Belum ada file dipilih");
        selectedFileLabel.setFont(UIConstants.NORMAL_FONT);

        browseButton.setEnabled(false);

        uploadButton.setEnabled(false);

        statusLabel = new JLabel("Belum terhubung");

        form.add(hostField);

        form.add(connectButton);

        form.add(browseButton);

        form.add(uploadButton);

        form.add(selectedFileLabel);

        form.add(statusLabel);

        panel.add(form,BorderLayout.NORTH);

        registerEvent();

        return panel;

    }

    private void registerEvent(){

        connectButton.addActionListener(event->{

            try{

                slaveClient.connect();

                statusLabel.setText("🟢 Connected");

                connectButton.setEnabled(false);

                browseButton.setEnabled(true);

                uploadButton.setEnabled(true);

            }catch(IOException exception){

                JOptionPane.showMessageDialog(
                        this,
                        "Gagal terhubung ke server!"
                );

            }

        });

        browseButton.addActionListener(event -> {

            JFileChooser chooser = new JFileChooser();

            int result = chooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {

                selectedFile = chooser.getSelectedFile();

                selectedFileLabel.setText(
                        "📄 " + selectedFile.getName()
                );

            }

        });

        uploadButton.addActionListener(event -> {

            if (selectedFile == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Silakan pilih file terlebih dahulu."
                );

                return;

            }

            try {

                slaveClient.upload(selectedFile);

                JOptionPane.showMessageDialog(
                        this,
                        "Upload berhasil."
                );

            } catch (IOException exception) {

                JOptionPane.showMessageDialog(
                        this,
                        "Upload gagal."
                );

                exception.printStackTrace();

            }

        });

    }

}