package otomasyon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UyeOlmaPaneli extends JFrame implements ActionListener {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/havuzdb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private JTextField adField;
    private JTextField soyadField;
    private JTextField kullaniciAdiField;
    private JPasswordField sifreField;
    private JButton kaydetButton;
    private JButton girisYapButton;

    public UyeOlmaPaneli() {
        setTitle("Üye Olma Paneli");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(new Color(255,200,100));  
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        JPanel adPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        adPanel.setBackground(new Color(255,200,100));
        JLabel adLabel = new JLabel("Ad:");
        adField = new JTextField(19);
        adField.setForeground(new Color(0, 0, 0));
        adField.setBackground(new Color(255, 255, 255));
        adPanel.add(adLabel);
        adPanel.add(adField);

        JPanel soyadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        soyadPanel.setBackground(new Color(255,200,100));
        JLabel soyadLabel = new JLabel("Soyad:");
        soyadField = new JTextField(17);
        soyadField.setBackground(new Color(255, 255, 255));
        soyadPanel.add(soyadLabel);
        soyadPanel.add(soyadField);

        JPanel kullaniciAdiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        kullaniciAdiPanel.setBackground(new Color(255,200,100));
        JLabel kullaniciAdiLabel = new JLabel("Kullanıcı Adı:");
        kullaniciAdiField = new JTextField(14);
        kullaniciAdiField.setBackground(new Color(255, 255, 255));
        kullaniciAdiPanel.add(kullaniciAdiLabel);
        kullaniciAdiPanel.add(kullaniciAdiField);

        JPanel sifrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sifrePanel.setBackground(new Color(255,200,100));
        JLabel sifreLabel = new JLabel("Şifre:");
        sifreField = new JPasswordField(18);
        sifreField.setBackground(new Color(255, 255, 255));
        sifrePanel.add(sifreLabel);
        sifrePanel.add(sifreField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(255,200,100));
        kaydetButton = new JButton("Kaydet");
        kaydetButton.addActionListener(this);
        kaydetButton.setBackground(new Color(255,165,0));  
        girisYapButton = new JButton("Giriş Yap");
        girisYapButton.addActionListener(this);
        girisYapButton.setBackground(new Color(255,140,0));  
        buttonPanel.add(kaydetButton);
        buttonPanel.add(girisYapButton);

        contentPane.add(adPanel);
        contentPane.add(soyadPanel);
        contentPane.add(kullaniciAdiPanel);
        contentPane.add(sifrePanel);
        contentPane.add(buttonPanel);

        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);  
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == kaydetButton) {
            String ad = adField.getText();
            String soyad = soyadField.getText();
            String kullaniciAdi = kullaniciAdiField.getText();
            String sifre = new String(sifreField.getPassword());

            UyeKayit.kaydet(ad, soyad, kullaniciAdi, sifre);

            JOptionPane.showMessageDialog(this, "Üyelik oluşturuldu!");
            resetFields();
        } else if (e.getSource() == girisYapButton) {
            dispose();
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new LoginPaneli();
                }
            });
        }
    }

    private void resetFields() {
        adField.setText("");
        soyadField.setText("");
        kullaniciAdiField.setText("");
        sifreField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new UyeOlmaPaneli();
            }
        });
    }
}

class UyeKayitIslemi {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/havuzdb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void kaydet(String ad, String soyad, String kullaniciAdi, String sifre) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            System.out.println("Veritabanına bağlantı başarılı!");

            String sql = "INSERT INTO kullanicilar (ad, soyad, kullaniciad, sifre) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, ad);
                statement.setString(2, soyad);
                statement.setString(3, kullaniciAdi);
                statement.setString(4, sifre);

                statement.executeUpdate();
            }

            System.out.println("Üyelik oluşturuldu!");
        } catch (SQLException ex) {
            System.out.println("Veritabanı bağlantısı sırasında hata oluştu: " + ex.getMessage());
        }
    }
}
