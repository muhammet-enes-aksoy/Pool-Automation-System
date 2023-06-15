package otomasyon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginPaneli extends JFrame implements ActionListener {
    private JTextField kullaniciAdiField;
    private JPasswordField sifreField;
    private JButton loginButton;
    private JButton uyeOlButton;

    public LoginPaneli() {
        setTitle("Login Paneli");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel(new GridBagLayout());
        contentPane.setBackground(new Color(255,200,100));  
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_END;
        JLabel kullaniciAdiLabel = new JLabel("Kullanıcı Adı:");
        contentPane.add(kullaniciAdiLabel, c);

        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        kullaniciAdiField = new JTextField(15);
        contentPane.add(kullaniciAdiField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_END;
        JLabel sifreLabel = new JLabel("Şifre:");
        contentPane.add(sifreLabel, c);

        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_START;
        sifreField = new JPasswordField(15);
        contentPane.add(sifreField, c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        loginButton = new JButton("Giriş Yap");
        loginButton.addActionListener(this);
        loginButton.setBackground(new Color(255,165,0));
        contentPane.add(loginButton, c);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        uyeOlButton = new JButton("Üye Ol");
        uyeOlButton.addActionListener(this);
        uyeOlButton.setBackground(new Color(255,140,0)); 
        contentPane.add(uyeOlButton, c);

        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);  
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String kullaniciAdi = kullaniciAdiField.getText();				
            String sifre = new String(sifreField.getPassword());

            int kullaniciId = dogrulaKullanici(kullaniciAdi, sifre);
            if (kullaniciId != -1) {
                if (kullaniciAdi.equals("admin")) {
                    dispose();
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            new AdminPaneli();
                        }
                    });
                } else {
                    dispose();
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            new KullaniciPaneli(kullaniciId);
                        }
                    });
                }
            } else {
                JOptionPane.showMessageDialog(this, "Hatalı kullanıcı adı veya şifre!", "Giriş Hatası", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == uyeOlButton) {
            dispose();
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new UyeOlmaPaneli();
                }
            });
        }
    }

    private int dogrulaKullanici(String kullaniciAdi, String sifre) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/havuzdb", "root", "")) {
            String sql = "SELECT * FROM kullanicilar WHERE kullaniciad = ? AND sifre = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, kullaniciAdi);
            statement.setString(2, sifre);

            ResultSet resultSet = statement.executeQuery();
            int kullaniciId = -1;

            if (resultSet.next()) {
                kullaniciId = resultSet.getInt("id");
            }

            resultSet.close();
            statement.close();

            return kullaniciId;
        } catch (SQLException ex) {
            System.out.println("Veritabanı bağlantısı sırasında hata oluştu: " + ex.getMessage());
        }

        return -1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginPaneli();
            }
        });
    }
}
