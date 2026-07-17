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
    
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JLabel masterIpLabel;
    private JButton disconnectButton;
    private JLabel stagingLabel;
    private JProgressBar progressBar;
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

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));

        cardPanel = new JPanel(new CardLayout());
        cardLayout = (CardLayout) cardPanel.getLayout();
        
        cardPanel.add(createLoginPanel(), "LOGIN");
        cardPanel.add(createMainPanel(), "MAIN");

        add(cardPanel);

        // Frame-level DropTarget for robust drag-and-drop
        this.setDropTarget(new java.awt.dnd.DropTarget() {
            public synchronized void drop(java.awt.dnd.DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY);
                    java.util.List<File> droppedFiles = (java.util.List<File>) evt.getTransferable().getTransferData(java.awt.datatransfer.DataFlavor.javaFileListFlavor);
                    if (droppedFiles != null && !droppedFiles.isEmpty()) {
                        selectedFile = droppedFiles.get(0);
                        if (stagingLabel != null) {
                            stagingLabel.setText("Staged: " + selectedFile.getName());
                            selectedFileLabel.setText(" | " + selectedFile.getName());
                            uploadButton.setEnabled(true);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        registerEvent();
        setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UIConstants.BACKGROUND);

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
        innerPanel.setBackground(UIConstants.CARD_BACKGROUND);
        innerPanel.putClientProperty(com.formdev.flatlaf.FlatClientProperties.STYLE, 
            "arc: 20; border: 30,40,30,40");

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JLabel title = new JLabel("Slave Login");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        titlePanel.add(title, BorderLayout.CENTER);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(UIConstants.NORMAL_FONT);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new JTextField("username");
        usernameField.setPreferredSize(new Dimension(300, 38));
        usernameField.setMaximumSize(new Dimension(300, 38));
        usernameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan username");

        JLabel hostLabel = new JLabel("Master Server IP");
        hostLabel.setFont(UIConstants.NORMAL_FONT);
        hostLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        hostField = new JTextField("localhost");
        hostField.setPreferredSize(new Dimension(300, 38));
        hostField.setMaximumSize(new Dimension(300, 38));
        hostField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Contoh: 192.168.1.5");

        connectButton = new JButton("Connect");
        connectButton.setPreferredSize(new Dimension(150, 38));
        connectButton.setMaximumSize(new Dimension(150, 38));
        connectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        connectButton.setBackground(UIConstants.PRIMARY);
        connectButton.setForeground(Color.WHITE);

        usernameField.addActionListener(e -> connectButton.doClick());
        hostField.addActionListener(e -> connectButton.doClick());

        innerPanel.add(titlePanel);
        innerPanel.add(Box.createVerticalStrut(30));
        innerPanel.add(usernameLabel);
        innerPanel.add(Box.createVerticalStrut(5));
        innerPanel.add(usernameField);
        innerPanel.add(Box.createVerticalStrut(15));
        innerPanel.add(hostLabel);
        innerPanel.add(Box.createVerticalStrut(5));
        innerPanel.add(hostField);
        innerPanel.add(Box.createVerticalStrut(20));
        innerPanel.add(connectButton);

        panel.add(innerPanel);
        return panel;
    }

    private JPanel createMainPanel() {
        JPanel root = new JPanel(new BorderLayout(15,15));
        root.setBorder(new EmptyBorder(20,20,20,20));
        root.setBackground(UIConstants.BACKGROUND);

        JPanel topPanel = new JPanel(new BorderLayout(15,15));
        topPanel.setOpaque(false);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Slave Dashboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        
        masterIpLabel = new JLabel("Connected to: Unknown");
        masterIpLabel.setFont(UIConstants.NORMAL_FONT);
        masterIpLabel.setForeground(Color.GRAY);

        titlePanel.add(title);
        titlePanel.add(masterIpLabel);
        headerPanel.add(titlePanel, BorderLayout.WEST);

        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(createToolbar(), BorderLayout.SOUTH);

        root.add(topPanel, BorderLayout.NORTH);
        root.add(createCenterPanel(), BorderLayout.CENTER);
        root.add(createFooter(), BorderLayout.SOUTH);

        return root;
    }

    private JPanel createToolbar() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonPanel.setOpaque(false);

        browseButton = new JButton("Browse");
        uploadButton = new JButton("Upload");
        refreshButton = new JButton("Refresh");

        Dimension buttonSize = new Dimension(130,38);
        browseButton.setPreferredSize(buttonSize);
        uploadButton.setPreferredSize(buttonSize);
        refreshButton.setPreferredSize(buttonSize);

        browseButton.setFont(UIConstants.NORMAL_FONT);
        uploadButton.setFont(UIConstants.NORMAL_FONT);
        refreshButton.setFont(UIConstants.NORMAL_FONT);

        browseButton.setEnabled(false);
        uploadButton.setEnabled(false);
        refreshButton.setEnabled(false);

        browseButton.putClientProperty("JButton.buttonType", "roundRect");
        uploadButton.putClientProperty("JButton.buttonType", "roundRect");
        uploadButton.setBackground(UIConstants.PRIMARY);
        uploadButton.setForeground(Color.WHITE);

        buttonPanel.add(browseButton);
        buttonPanel.add(uploadButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(refreshButton);

        stagingLabel = new JLabel("Staged: None");
        stagingLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        stagingLabel.setForeground(Color.GRAY);
        stagingLabel.setBorder(new EmptyBorder(5, 5, 0, 0));

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(stagingLabel, BorderLayout.SOUTH);

        return mainPanel;
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
                com.formdev.flatlaf.FlatClientProperties.PLACEHOLDER_TEXT,
                "Cari file di server..."
        );

        searchField.setPreferredSize(new Dimension(400, 38));
        searchLabel.setFont(UIConstants.NORMAL_FONT);
        searchLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchField.setAlignmentX(Component.LEFT_ALIGNMENT);

        topPanel.add(searchLabel);
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(searchField);
        topPanel.add(Box.createVerticalStrut(10));

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
        fileTable.setFillsViewportHeight(true);

        tableScrollPane = new JScrollPane(fileTable);

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
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);

        statusLabel = new JLabel("Disconnected");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusLabel.setForeground(UIConstants.ERROR);

        selectedFileLabel = new JLabel(" | No file selected");
        selectedFileLabel.setFont(UIConstants.NORMAL_FONT);

        leftPanel.add(statusLabel);
        leftPanel.add(selectedFileLabel);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(150, 20));
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);

        downloadButton = new JButton("Download");
        downloadButton.setPreferredSize(new Dimension(140,38));
        downloadButton.setFont(UIConstants.NORMAL_FONT);
        downloadButton.setEnabled(false);

        disconnectButton = new JButton("Disconnect");
        disconnectButton.setPreferredSize(new Dimension(140,38));
        disconnectButton.setFont(UIConstants.NORMAL_FONT);
        disconnectButton.setForeground(Color.RED);

        rightPanel.add(progressBar);
        rightPanel.add(downloadButton);
        rightPanel.add(disconnectButton);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private void registerEvent(){
        connectButton.addActionListener(event->{
            try{
                slaveClient.setUsername(
                        usernameField.getText().trim()
                );

                slaveClient.connect(hostField.getText().trim());

                statusLabel.setText("Connected");
                statusLabel.setForeground(UIConstants.SUCCESS);
                masterIpLabel.setText("Connected to: " + hostField.getText().trim());

                connectButton.setEnabled(false);
                browseButton.setEnabled(true);
                uploadButton.setEnabled(false);
                refreshButton.setEnabled(true);

                cardLayout.show(cardPanel, "MAIN");

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
                stagingLabel.setText("Staged: " + selectedFile.getName());
                selectedFileLabel.setText(" | " + selectedFile.getName());
                uploadButton.setEnabled(true);
            }
        });

        uploadButton.addActionListener(event -> {
            if (selectedFile == null) {
                JOptionPane.showMessageDialog(this, "Silakan pilih file terlebih dahulu.");
                return;
            }

            uploadButton.setEnabled(false);
            browseButton.setEnabled(false);
            progressBar.setVisible(true);
            progressBar.setValue(0);

            SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
                @Override
                protected Void doInBackground() throws Exception {
                    slaveClient.upload(selectedFile, this::publish);
                    return null;
                }

                @Override
                protected void process(java.util.List<Integer> chunks) {
                    if (!chunks.isEmpty()) {
                        progressBar.setValue(chunks.get(chunks.size() - 1));
                    }
                }

                @Override
                protected void done() {
                    progressBar.setVisible(false);
                    browseButton.setEnabled(true);
                    try {
                        get();
                        JOptionPane.showMessageDialog(SlaveFrame.this, "Upload berhasil.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(SlaveFrame.this, "Upload gagal.");
                        ex.printStackTrace();
                    } finally {
                        selectedFile = null;
                        stagingLabel.setText("Staged: None");
                        selectedFileLabel.setText(" | No file selected");
                        refreshFiles();
                    }
                }
            };
            worker.execute();
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
                JOptionPane.showMessageDialog(this, "Pilih file terlebih dahulu.");
                return;
            }

            downloadButton.setEnabled(false);
            progressBar.setVisible(true);
            progressBar.setValue(0);

            String fileName = tableModel.getValueAt(selectedRow, 0).toString();

            SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
                @Override
                protected Void doInBackground() throws Exception {
                    slaveClient.download(fileName, this::publish);
                    return null;
                }

                @Override
                protected void process(java.util.List<Integer> chunks) {
                    if (!chunks.isEmpty()) {
                        progressBar.setValue(chunks.get(chunks.size() - 1));
                    }
                }

                @Override
                protected void done() {
                    progressBar.setVisible(false);
                    downloadButton.setEnabled(true);
                    try {
                        get();
                        JOptionPane.showMessageDialog(SlaveFrame.this, "File berhasil didownload.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(SlaveFrame.this, "Download gagal.");
                        ex.printStackTrace();
                    }
                }
            };
            worker.execute();
        });

        searchField.addActionListener(event -> {
            refreshFiles();
        });

        disconnectButton.addActionListener(event -> {
            try {
                slaveClient.disconnect();
            } catch (IOException e) {
                // ignore
            }
            statusLabel.setText("Disconnected");
            statusLabel.setForeground(UIConstants.ERROR);
            cardLayout.show(cardPanel, "LOGIN");
            tableModel.setRowCount(0);
            stagingLabel.setText("Staged: None");
            selectedFileLabel.setText(" | No file selected");
            uploadButton.setEnabled(false);
            browseButton.setEnabled(false);
            refreshButton.setEnabled(false);
            connectButton.setEnabled(true);
        });
}

    private void refreshFiles() {
        try {
            String keyword = searchField.getText().trim();

            allFiles = slaveClient.search(keyword);

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

