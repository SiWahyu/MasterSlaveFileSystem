package com.masterslave.ui.slave;

import com.formdev.flatlaf.FlatClientProperties;
import com.masterslave.client.SlaveClient;
import com.masterslave.server.MasterServer;
import com.masterslave.util.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.io.File;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class SlaveFrame extends JFrame {

    private JTextField hostField;

    private JButton connectButton;

    private JButton browseButton;

    private JButton uploadButton;

    private JLabel statusLabel;

    private final SlaveClient slaveClient = new SlaveClient();

    private File selectedFile;

    private JLabel selectedFileLabel;

    private JButton refreshButton;

    private JTable fileTable;

    private DefaultTableModel tableModel;

    private JScrollPane tableScrollPane;

    private JButton downloadButton;

    private JTextField searchField;

    private String[] allFiles = new String[0];

    public SlaveFrame() {

        initializeFrame();

    }

    private void initializeFrame() {

        setTitle("Slave Client");

        setSize(1200, 700);

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

        JPanel form = new JPanel(new GridLayout(8,1,10,10));

        form.setOpaque(false);

        hostField = new JTextField("localhost");

        hostField.putClientProperty(
                FlatClientProperties.PLACEHOLDER_TEXT,
                "Host");

        connectButton = new JButton("Connect");

        browseButton = new JButton("Browse File");

        uploadButton = new JButton("Upload");

        refreshButton = new JButton("Refresh File");

        searchField = new JTextField();

        searchField.putClientProperty(
                FlatClientProperties.PLACEHOLDER_TEXT,
                "Cari file..."
        );

        refreshButton.setEnabled(false);

        selectedFileLabel = new JLabel("Belum ada file dipilih");
        selectedFileLabel.setFont(UIConstants.NORMAL_FONT);

        browseButton.setEnabled(false);

        uploadButton.setEnabled(false);

        statusLabel = new JLabel("Belum terhubung");

        form.add(hostField);

        form.add(connectButton);

        form.add(browseButton);

        form.add(uploadButton);

        form.add(refreshButton);

        form.add(searchField);

        form.add(selectedFileLabel);

        form.add(statusLabel);

        tableModel = new DefaultTableModel(
                new Object[]{"Nama File"},
                0
        ) {

            @Override
            public boolean isCellEditable(int row, int column) {

                return false;

            }

        };

        fileTable = new JTable(tableModel);

        fileTable.setRowHeight(28);

        tableScrollPane = new JScrollPane(fileTable);

        tableScrollPane.setPreferredSize(
                new Dimension(350,180)
        );

        downloadButton = new JButton("Download");

        downloadButton.setEnabled(false);

        panel.add(form, BorderLayout.NORTH);

        panel.add(tableScrollPane, BorderLayout.CENTER);

        panel.add(downloadButton, BorderLayout.SOUTH);

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

                refreshButton.setEnabled(true);

                refreshFiles();

                fileTable.clearSelection();

                downloadButton.setEnabled(false);

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

                // Reset pilihan file
                selectedFile = null;

                selectedFileLabel.setText("Belum ada file dipilih");

                uploadButton.setEnabled(false);

                // Refresh daftar file di Master
                refreshFiles();


            } catch (IOException exception) {

                JOptionPane.showMessageDialog(
                        this,
                        "Upload gagal."
                );

                exception.printStackTrace();

            }

        });

        refreshButton.addActionListener(event -> {

            refreshFiles();

        });

        fileTable.getSelectionModel().addListSelectionListener(event -> {

            downloadButton.setEnabled(
                    fileTable.getSelectedRow() != -1
            );

        });

        downloadButton.addActionListener(event -> {

            int selectedRow = fileTable.getSelectedRow();

            if (selectedRow == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Pilih file terlebih dahulu."
                );

                return;

            }

            String fileName = tableModel
                    .getValueAt(selectedRow, 0)
                    .toString();

            try {

                slaveClient.download(fileName);

                JOptionPane.showMessageDialog(
                        this,
                        "File berhasil didownload."
                );

            } catch (IOException exception) {

                JOptionPane.showMessageDialog(
                        this,
                        "Download gagal."
                );

                exception.printStackTrace();

            }

        });

        searchField.getDocument().addDocumentListener(
                new DocumentListener() {

                    @Override
                    public void insertUpdate(DocumentEvent event) {

                        filterTable();

                    }

                    @Override
                    public void removeUpdate(DocumentEvent event) {

                        filterTable();

                    }

                    @Override
                    public void changedUpdate(DocumentEvent event) {

                        filterTable();

                    }

                }
        );

    }

    private void filterTable() {

        String keyword =
                searchField.getText().toLowerCase();

        tableModel.setRowCount(0);

        for (String file : allFiles) {

            if (file.toLowerCase().contains(keyword)) {

                tableModel.addRow(
                        new Object[]{file}
                );

            }

        }

    }

    private void refreshFiles() {

        try {

            allFiles = slaveClient.search();

            tableModel.setRowCount(0);

            for (String file : allFiles) {

                tableModel.addRow(
                        new Object[]{file}
                );

            }

        } catch (IOException exception) {

            JOptionPane.showMessageDialog(
                    this,
                    "Gagal mengambil daftar file."
            );

            exception.printStackTrace();

        }

    }

}