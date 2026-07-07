package com.masterslave.ui.master;

import com.masterslave.ui.components.DashboardCard;
import com.masterslave.util.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import com.masterslave.ui.components.ControlPanel;
import com.masterslave.server.MasterServer;
import javax.swing.table.DefaultTableModel;
import com.masterslave.listener.ServerListener;

public class MasterFrame extends JFrame implements ServerListener {

    private DashboardCard clientCard;
    private DashboardCard fileCard;
    private DashboardCard statusCard;

    private JLabel statusLabel;

    private JTable clientTable;

    private JTextArea logArea;
    private ControlPanel controlPanel;
    private final MasterServer masterServer = new MasterServer();

    public MasterFrame() {

        initializeFrame();

        initializeComponents();

        masterServer.setServerListener(this);

        registerEvents();

    }


    /**
     * Mengatur properti dasar jendela.
     */
    private void initializeFrame() {

        setTitle("Master Server");

        setSize(1200, 700);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setContentPane(createContent());

        setVisible(true);

    }

    /**
     * Inisialisasi data awal komponen.
     */
    private void initializeComponents() {

        addLog("Server belum dijalankan.");

    }

    /**
     * Membuat seluruh isi halaman.
     */
    private JPanel createContent() {

        JPanel root = new JPanel(new BorderLayout(15, 15));

        root.setBackground(UIConstants.BACKGROUND);

        root.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        topPanel.setOpaque(false);

        topPanel.add(createHeader(), BorderLayout.NORTH);

        topPanel.add(createToolbar(), BorderLayout.SOUTH);

        root.add(topPanel, BorderLayout.NORTH);

        root.add(createCenterPanel(), BorderLayout.CENTER);

        return root;

    }

    /**
     * Header aplikasi.
     */
    private JPanel createHeader() {

        JPanel panel = new JPanel(new BorderLayout());

        panel.setOpaque(false);

        JLabel titleLabel = new JLabel("Master Server");

        titleLabel.setFont(UIConstants.TITLE_FONT);

        statusLabel = new JLabel("● Offline");

        statusLabel.setFont(UIConstants.NORMAL_FONT);

        statusLabel.setForeground(Color.RED);

        panel.add(titleLabel, BorderLayout.WEST);

        panel.add(statusLabel, BorderLayout.EAST);

        return panel;
    }

    //    Tool Bar
    private JPanel createToolbar() {

        controlPanel = new ControlPanel();

        JPanel panel = new JPanel(new BorderLayout());

        panel.setOpaque(false);

        panel.add(controlPanel, BorderLayout.WEST);

        return panel;

    }

    /**
     * Panel utama.
     */
    private JPanel createCenterPanel() {

        JPanel panel = new JPanel(new BorderLayout(15, 15));

        panel.setOpaque(false);

        panel.add(createDashboard(), BorderLayout.NORTH);

        panel.add(createClientTable(), BorderLayout.CENTER);

        panel.add(createLogPanel(), BorderLayout.SOUTH);

        return panel;

    }

    /**
     * Dashboard statistik.
     */
    private JPanel createDashboard() {

        JPanel panel = new JPanel(new GridLayout(1, 3, 15, 15));

        panel.setOpaque(false);

        clientCard = new DashboardCard("Connected Client", "0");

        fileCard = new DashboardCard("Total Files", "0");

        statusCard = new DashboardCard("Server Status", "Offline");

        panel.add(clientCard);

        panel.add(fileCard);

        panel.add(statusCard);

        return panel;

    }

    /**
     * Tabel client.
     */
    private JPanel createClientTable() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        panel.setOpaque(false);

        JLabel title = new JLabel("Connected Clients");

        title.setFont(UIConstants.SUBTITLE_FONT);

        panel.add(title, BorderLayout.NORTH);

        clientTable = new JTable();

        clientTable.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                        "ID",
                        "Client Name",
                        "Status"
                }
        ));

        // Mengatur tinggi setiap baris tabel
        clientTable.setRowHeight(30);

        // Menghilangkan garis antar sel
        clientTable.setShowGrid(false);

        // Menghilangkan jarak antar sel
        clientTable.setIntercellSpacing(new Dimension(0, 0));

        // Mencegah pengguna memindahkan urutan kolom
        clientTable.getTableHeader().setReorderingAllowed(false);

        clientTable.getTableHeader().setFont(UIConstants.NORMAL_FONT);

        clientTable.setFont(UIConstants.NORMAL_FONT);

        JScrollPane scrollPane = new JScrollPane(clientTable);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;

    }

    /**
     * Area log.
     */
    private JPanel createLogPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        panel.setOpaque(false);

        JLabel title = new JLabel("Activity Log");

        title.setFont(UIConstants.SUBTITLE_FONT);

        panel.add(title, BorderLayout.NORTH);

        logArea = new JTextArea(8, 0);

        logArea.setEditable(false);

        logArea.setFont(UIConstants.LOG_FONT);

        // Membuat teks otomatis pindah ke baris berikutnya
        logArea.setLineWrap(true);

        // Memastikan perpindahan baris tidak memotong kata
        logArea.setWrapStyleWord(true);

        // Memberikan jarak antara teks dengan tepi area log
        logArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(logArea);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;

    }

    /**
     * Menambahkan pesan ke activity log.
     */
    public void addLog(String message) {

        String time = java.time.LocalTime.now()
                .withNano(0)
                .toString();

        logArea.append("[" + time + "] " + message + "\n");

    }

    /**
     * Menambahkan satu baris client ke tabel.
     */
    public void addClient(String clientName) {

        DefaultTableModel model =
                (DefaultTableModel) clientTable.getModel();

        model.addRow(new Object[]{
                model.getRowCount() + 1,
                clientName,
                "Connected"
        });

    }

    /**
     * Menghapus seluruh data client dari tabel.
     */
    public void clearClientTable() {

        DefaultTableModel model =
                (DefaultTableModel) clientTable.getModel();

        model.setRowCount(0);

    }

    /**
     * Menghapus client dari tabel saat disconnect.
     */
    public void removeClientFromTable(String clientName) {

        DefaultTableModel model =
                (DefaultTableModel) clientTable.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {

            if (model.getValueAt(i, 1).equals(clientName)) {

                model.removeRow(i);
                
                break;

            }

        }

        // Perbarui ID
        for (int i = 0; i < model.getRowCount(); i++) {
            
            model.setValueAt(i + 1, i, 0);
            
        }

    }

    /**
     * Mengubah jumlah client pada dashboard.
     */
    public void updateClientCount(int totalClient) {

        clientCard.setValue(String.valueOf(totalClient));

    }

    /**
     * Mendaftarkan aksi pada tombol toolbar.
     */
    private void registerEvents() {

        controlPanel.getStopButton().addActionListener(event -> {

            System.out.println("STOP BUTTON DIKLIK");

            masterServer.stop();

        });

        // Event tombol Start Server
        controlPanel.getStartButton().addActionListener(event -> {

            new Thread(masterServer::start).start();

        });

        // Event tombol Stop Server
        controlPanel.getStopButton().addActionListener(event -> {

            masterServer.stop();

        });

    }

    @Override
    public void onServerStarted() {

        SwingUtilities.invokeLater(() -> {

            System.out.println("onServerStarted dipanggil");

            statusLabel.setText("🟢 Running");
            statusLabel.setForeground(UIConstants.SUCCESS);

            statusCard.setValue("Running");

            controlPanel.setServerRunning(true);

            System.out.println("Start = " + controlPanel.getStartButton().isEnabled());
            System.out.println("Stop  = " + controlPanel.getStopButton().isEnabled());

            addLog("Server berhasil dijalankan.");

        });

    }

    @Override
    public void onServerStopped() {

        SwingUtilities.invokeLater(() -> {

            statusLabel.setText("🔴 Offline");
            statusLabel.setForeground(Color.RED);

            statusCard.setValue("Offline");

            updateClientCount(0);

            clearClientTable();

            controlPanel.setServerRunning(false);

            addLog("Server dihentikan.");

        });

    }

    @Override
    public void onClientConnected(String clientName, int totalClient) {

        SwingUtilities.invokeLater(() -> {

            addClient(clientName);

            updateClientCount(totalClient);

            addLog("Client terhubung : " + clientName);

        });

    }
    @Override
    public void onFileUploaded(
            String username,
            String fileName,
            int totalFiles
    ) {

        SwingUtilities.invokeLater(() -> {

            addLog(
                    "📤 " + username +
                            " mengupload " +
                            fileName
            );

            fileCard.setValue(
                    String.valueOf(totalFiles)
            );

        });

    }

    @Override
    public void onClientDisconnected(
            String username,
            int totalClients
    ) {

        System.out.println("EVENT DISCONNECT");

        SwingUtilities.invokeLater(() -> {

            removeClientFromTable(username);

            updateClientCount(totalClients);

            addLog("🔴 " + username + " disconnected.");

        });

    }

}