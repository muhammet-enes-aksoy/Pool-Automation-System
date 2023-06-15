package otomasyon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingConstants;

public class KullaniciPaneli extends JFrame {
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JList<String> havuzList;
    private JTextArea textArea;
    private JTextField rezervasyonField;
    private JTextField havuzIdField;
    private JButton ekleButton;
    private JButton rezervasyonlarimButton;
    private int kullaniciId;

    public KullaniciPaneli(int kullaniciId) {
        this.kullaniciId = kullaniciId;
        setupUI();
    }

    private void setupUI() {
        setTitle("Kullanıcı Paneli");
        getContentPane().setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        getContentPane().setBackground(new Color(255, 228, 181)); 

        // Sol panel
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(255, 128, 64));  

        JLabel havuzlarLabel = new JLabel("Havuzlar:");
        havuzlarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(havuzlarLabel);

        DefaultListModel<String> havuzListModel = new DefaultListModel<>();
        havuzList = new JList<>(havuzListModel);
        havuzList.setBackground(new Color(250, 173, 69));
        JScrollPane havuzScrollPane = new JScrollPane(havuzList);
        leftPanel.add(havuzScrollPane);

        getContentPane().add(leftPanel, BorderLayout.WEST);

        // Sağ panel
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(255, 165, 79));  

        JLabel havuzIdLabel = new JLabel("Havuz ID:");
        havuzIdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(havuzIdLabel);

        havuzIdField = new JTextField(10);
        rightPanel.add(havuzIdField);

        JLabel rezervasyonLabel = new JLabel("Rezervasyon:");
        rezervasyonLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(rezervasyonLabel);

        rezervasyonField = new JTextField(20);
        rightPanel.add(rezervasyonField);

        ekleButton = new JButton("Rezervasyon Ekle");
        ekleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        ekleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String rezervasyonData = rezervasyonField.getText();
                int havuzId = Integer.parseInt(havuzIdField.getText());
                rezervasyonEkle(kullaniciId, havuzId, rezervasyonData);
                updateHavuzlar();
            }
        });
        rightPanel.add(ekleButton);

        rezervasyonlarimButton = new JButton("Rezervasyonlarım");
        rezervasyonlarimButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rezervasyonlarimButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rezervasyonlarimGoster(kullaniciId);
            }
        });
        rightPanel.add(rezervasyonlarimButton);

        getContentPane().add(rightPanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        updateHavuzlar();
    }

    private void rezervasyonEkle(int kullaniciId, int havuzId, String rezervasyonData) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/havuzdb", "root", "")) {
            String sql = "INSERT INTO rezervasyonlar (kullaniciid, havuzid, havuzgiris) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, kullaniciId);
            statement.setInt(2, havuzId);
            statement.setString(3, rezervasyonData);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Rezervasyon başarıyla eklendi.");
            } else {
                System.out.println("Rezervasyon eklenirken bir hata oluştu.");
            }

            statement.close();
        } catch (SQLException ex) {
            System.out.println("Veritabanı bağlantısı sırasında hata oluştu: " + ex.getMessage());
        }
    }

    private void updateHavuzlar() {
        DefaultListModel<String> havuzListModel = (DefaultListModel<String>) havuzList.getModel();
        havuzListModel.clear();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/havuzdb", "root", "")) {
            String sql = "SELECT * FROM havuzlar";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int havuzId = resultSet.getInt("id");
                String havuzAdi = resultSet.getString("havuzad");
                String doluluk = resultSet.getString("doluluk");
                havuzListModel.addElement(havuzId + " - " + havuzAdi + " (" + doluluk + ")");
            }

            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            System.out.println("Veritabanı bağlantısı sırasında hata oluştu: " + ex.getMessage());
        }
    }

    private void rezervasyonlarimGoster(int kullaniciId) {
        JFrame rezervasyonlarimFrame = new JFrame("Rezervasyonlarım");
        rezervasyonlarimFrame.getContentPane().setLayout(new BorderLayout());
        rezervasyonlarimFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        rezervasyonlarimFrame.setSize(400, 300);
        
        DefaultListModel<String> rezervasyonListModel = new DefaultListModel<>();
        JList<String> rezervasyonList = new JList<>(rezervasyonListModel);
        JScrollPane rezervasyonScrollPane = new JScrollPane(rezervasyonList);
        
        JButton silButton = new JButton("Rezervasyon Sil");
        silButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = rezervasyonList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectedReservation = rezervasyonList.getSelectedValue();
                    int rezervasyonId = Integer.parseInt(selectedReservation.split(" - ")[0]);
                    rezervasyonSil(rezervasyonId);
                    rezervasyonListModel.remove(selectedIndex);
                }
            }
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(silButton);
        
        rezervasyonlarimFrame.getContentPane().add(rezervasyonScrollPane, BorderLayout.CENTER);
        rezervasyonlarimFrame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/havuzdb", "root", "")) {
            String sql = "SELECT * FROM rezervasyonlar WHERE kullaniciid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, kullaniciId);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                int rezervasyonId = resultSet.getInt("id");
                int havuzId = resultSet.getInt("havuzid");
                String havuzAdi = getHavuzAdi(havuzId);
                String rezervasyonData = resultSet.getString("havuzgiris");
                rezervasyonListModel.addElement(rezervasyonId + " - " + havuzAdi + " - " + rezervasyonData);
            }
            
            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            System.out.println("Veritabanı bağlantısı sırasında hata oluştu: " + ex.getMessage());
        }
        
        rezervasyonlarimFrame.setVisible(true);
    }

    private String getHavuzAdi(int havuzId) {
        String havuzAdi = "";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/havuzdb", "root", "")) {
            String sql = "SELECT havuzad FROM havuzlar WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, havuzId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                havuzAdi = resultSet.getString("havuzad");
            }

            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            System.out.println("Veritabanı bağlantısı sırasında hata oluştu: " + ex.getMessage());
        }

        return havuzAdi;
    }

    private void rezervasyonSil(int rezervasyonId) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/havuzdb", "root", "")) {
            String sql = "DELETE FROM rezervasyonlar WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, rezervasyonId);
            
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Rezervasyon başarıyla silindi.");
            } else {
                System.out.println("Rezervasyon silinirken bir hata oluştu.");
            }

            statement.close();
        } catch (SQLException ex) {
            System.out.println("Veritabanı bağlantısı sırasında hata oluştu: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new KullaniciPaneli(1);
            }
        });
    }
}