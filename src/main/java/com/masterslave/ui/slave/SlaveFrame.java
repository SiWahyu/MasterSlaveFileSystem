package com.masterslave.ui.slave;

import com.formdev.flatlaf.FlatClientProperties;
import com.masterslave.client.SlaveClient;
import com.masterslave.common.model.FileInfo;
import com.masterslave.util.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.io.File;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;


public class SlaveFrame extends JFrame {

    private JTextField usernameField;

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

    private List<FileInfo> allFiles = new ArrayList<>();

    private Timer autoRefreshTimer;


    public SlaveFrame() {

        initializeFrame();

    }

    private void initializeFrame() {

        setTitle("Slave Client");

        setSize(1000,700);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setContentPane(createContent());

        setVisible(true);



    }

    private JPanel createContent() {

        JPanel root = new JPanel(new BorderLayout(15,15));

        root.setBorder(new EmptyBorder(20,20,20,20));

        root.setBackground(UIConstants.BACKGROUND);

        JPanel topPanel = new JPanel(new BorderLayout(15,15));

        topPanel.setOpaque(false);

        topPanel.add(createHeader(), BorderLayout.NORTH);

        topPanel.add(createHostPanel(), BorderLayout.CENTER);

        topPanel.add(createToolbar(), BorderLayout.SOUTH);

        root.add(topPanel, BorderLayout.NORTH);

        root.add(createCenterPanel(), BorderLayout.CENTER);

        root.add(createFooter(), BorderLayout.SOUTH);

        registerEvent();

        return root;

    }

    private JPanel createHeader() {

        JPanel panel = new JPanel(new BorderLayout());

        panel.setOpaque(false);

        JLabel title = new JLabel("🖥 Slave Client");

        title.setFont(
                new Font("SansSerif", Font.BOLD, 24)
        );

        panel.add(title, BorderLayout.WEST);

        return panel;

    }

    private JPanel createHostPanel() {

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Username
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(UIConstants.NORMAL_FONT);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameField = new JTextField("username");
        usernameField.setPreferredSize(new Dimension(250, 38));
        usernameField.setMaximumSize(new Dimension(250, 38));

        usernameField.putClientProperty(
                FlatClientProperties.PLACEHOLDER_TEXT,
                "Masukkan username"
        );

        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Host Address
        JLabel hostLabel = new JLabel("Host Address");
        hostLabel.setFont(UIConstants.NORMAL_FONT);
        hostLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel hostRow = new JPanel(new FlowLayout(
                FlowLayout.LEFT,
                10,
                0
        ));

        hostRow.setOpaque(false);
        hostRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        hostField = new JTextField("localhost");
        hostField.setPreferredSize(new Dimension(330, 38));

        hostField.putClientProperty(
                FlatClientProperties.PLACEHOLDER_TEXT,
                "Host"
        );

        connectButton = new JButton("Connect");
        connectButton.setPreferredSize(new Dimension(130, 38));

        hostRow.add(hostField);
        hostRow.add(connectButton);

        // Layout
        panel.add(usernameLabel);
        panel.add(Box.createVerticalStrut(5));

        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(15));

        panel.add(hostLabel);
        panel.add(Box.createVerticalStrut(5));

        panel.add(hostRow);

        return panel;
    }

    private JPanel createToolbar() {

        JPanel panel = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT,
                        10,
                        0
                )
        );

        panel.setOpaque(false);

        browseButton = new JButton("Browse");

        uploadButton = new JButton("Upload");

        refreshButton = new JButton("Refresh");

        Dimension buttonSize =
                new Dimension(130,38);

        browseButton.setPreferredSize(buttonSize);

        uploadButton.setPreferredSize(buttonSize);

        refreshButton.setPreferredSize(buttonSize);

        browseButton.setFont(UIConstants.NORMAL_FONT);

        uploadButton.setFont(UIConstants.NORMAL_FONT);

        refreshButton.setFont(UIConstants.NORMAL_FONT);

        browseButton.setEnabled(false);

        uploadButton.setEnabled(false);

        refreshButton.setEnabled(false);

        panel.add(browseButton);

        panel.add(uploadButton);

        panel.add(refreshButton);

        return panel;

    }

    private JPanel createCenterPanel() {

        JPanel panel = new JPanel(
                new BorderLayout(10,10)
        );

        panel.setOpaque(false);

        JPanel topPanel = new JPanel();

        topPanel.setOpaque(false);

        topPanel.setLayout(
                new BoxLayout(
                        topPanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel searchLabel =
                new JLabel("Search File");

        searchField = new JTextField();

        searchField.putClientProperty(
                FlatClientProperties.PLACEHOLDER_TEXT,
                "🔍 Cari file di server..."
        );

        selectedFileLabel =
                new JLabel("📄 Belum ada file dipilih");

        selectedFileLabel.setFont(
                UIConstants.NORMAL_FONT
        );

        searchField.setPreferredSize(
                new Dimension(400, 38)
        );

        searchLabel.setFont(UIConstants.NORMAL_FONT);

        searchLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        searchField.setAlignmentX(Component.LEFT_ALIGNMENT);

        selectedFileLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        topPanel.add(searchLabel);

        topPanel.add(Box.createVerticalStrut(5));

        topPanel.add(searchField);

        topPanel.add(Box.createVerticalStrut(10));

        topPanel.add(selectedFileLabel);

        tableModel = new DefaultTableModel(
                new Object[]{
                        "Nama File",
                        "Uploaded By",
                        "Size",
                        "Upload Time"
                },
                0
        ) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {

                return false;

            }

        };

        fileTable = new JTable(tableModel);

        fileTable.setRowHeight(28);

        tableScrollPane =
                new JScrollPane(fileTable);

        panel.add(
                topPanel,
                BorderLayout.NORTH
        );

        panel.add(
                tableScrollPane,
                BorderLayout.CENTER
        );

        return panel;

    }

    private JPanel createFooter() {

        JPanel panel = new JPanel(
                new BorderLayout()
        );

        panel.setOpaque(false);

        statusLabel = new JLabel("🔴 Belum terhubung");

        statusLabel.setFont(UIConstants.NORMAL_FONT);

        downloadButton =
                new JButton("Download");

        downloadButton.setPreferredSize(
                new Dimension(140,38)
        );

        downloadButton.setFont(
                UIConstants.NORMAL_FONT
        );

        downloadButton.setEnabled(false);

        panel.add(
                statusLabel,
                BorderLayout.WEST
        );

        panel.add(
                downloadButton,
                BorderLayout.EAST
        );

        return panel;

    }

    private void registerEvent(){

        connectButton.addActionListener(event->{

            try{
                slaveClient.setUsername(
                        usernameField.getText().trim()
                );

                slaveClient.connect();

                startAutoRefresh();

                statusLabel.setText("🟢 Connected");

                connectButton.setEnabled(false);

                browseButton.setEnabled(true);

                uploadButton.setEnabled(false);

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
                         selectedFile.getName()
                );

                uploadButton.setEnabled(true);

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

        for (FileInfo file : allFiles) {

            if (file.getFileName()
                    .toLowerCase()
                    .contains(keyword)) {

                tableModel.addRow(
                        new Object[]{
                                file.getFileName(),
                                file.getUploadedBy(),
                                file.getFileSize(),
                                file.getUploadTime()
                        }
                );

            }

        }

    }

    private void refreshFiles() {

        try {

            allFiles = slaveClient.search();

            tableModel.setRowCount(0);

            for (FileInfo file : allFiles) {

                tableModel.addRow(
                        new Object[]{
                                file.getFileName(),
                                file.getUploadedBy(),
                                file.getFileSize(),
                                file.getUploadTime()
                        }
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

    private void startAutoRefresh() {

        autoRefreshTimer = new Timer(2000, event -> {

            try {

                refreshFiles();

            } catch (Exception exception) {

                // Abaikan jika server sedang berhenti
            }

        });

        autoRefreshTimer.start();

    }

    private void stopAutoRefresh() {

        if (autoRefreshTimer != null) {

            autoRefreshTimer.stop();

        }

    }

}