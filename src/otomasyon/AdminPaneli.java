package otomasyon;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class AdminPaneli extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/havuzdb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private JTable kullaniciTable;
    private JTable havuzTable;
    private JTable havuzAyarTable;
    private JTable rezervasyonTable;

    private Map<Integer, String[]> kullaniciDegisiklikler = new HashMap<>();
    private Map<Integer, String[]> havuzDegisiklikler = new HashMap<>();
    private Map<Integer, Float[]> havuzAyarDegisiklikler = new HashMap<>();
    private Map<Integer, Object[]> rezervasyonDegisiklikler = new HashMap<>();

    private Connection connection;

    public AdminPaneli() {
        setTitle("Admin Paneli");
        setLayout(new GridLayout(2, 2));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Kullanıcı Tablosu
        JPanel kullaniciPanel = new JPanel(new BorderLayout());
        kullaniciTable = new JTable();
        JButton kullaniciEkleButton = new JButton("Kullanıcı Ekle");
        JButton kullaniciSilButton = new JButton("Kullanıcı Sil");
       
        kullaniciSilButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = kullaniciTable.getSelectedRow();
                if (selectedRow != -1) {
                    int kullaniciId = (int) kullaniciTable.getValueAt(selectedRow, 0);
                    int choice = JOptionPane.showConfirmDialog(null, "Seçilen kullanıcıyı silmek istediğinize emin misiniz?", "Kullanıcı Sil", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        kullaniciSil(kullaniciId);
                    }
                }
            }
        });
        kullaniciEkleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                kullaniciEkle();
            }
        });
        JScrollPane kullaniciScrollPane = new JScrollPane(kullaniciTable);
        kullaniciPanel.add(new JLabel("Kullanıcılar"), BorderLayout.NORTH);
        kullaniciPanel.add(kullaniciScrollPane, BorderLayout.CENTER);
        kullaniciPanel.add(kullaniciEkleButton, BorderLayout.WEST);
        kullaniciPanel.add(kullaniciSilButton, BorderLayout.EAST);
        add(kullaniciPanel);

        // Havuz Tablosu
        JPanel havuzPanel = new JPanel(new BorderLayout());
        havuzTable = new JTable();
        JButton havuzEkleButton = new JButton("Havuz Ekle");
        JButton havuzSilButton = new JButton("Havuz Sil");
     
        havuzEkleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                havuzEkle();
            }
        });
        havuzSilButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = havuzTable.getSelectedRow();
                if (selectedRow != -1) {
                    int havuzId = (int) havuzTable.getValueAt(selectedRow, 0);
                    int choice = JOptionPane.showConfirmDialog(null, "Seçilen havuzu silmek istediğinize emin misiniz?", "Havuz Sil", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        havuzSil(havuzId);
                    }
                }
            }
        });
        
        JScrollPane havuzScrollPane = new JScrollPane(havuzTable);
        havuzPanel.add(new JLabel("Havuzlar"), BorderLayout.NORTH);
        havuzPanel.add(havuzScrollPane, BorderLayout.CENTER);
        havuzPanel.add(havuzEkleButton, BorderLayout.WEST);
        havuzPanel.add(havuzSilButton, BorderLayout.EAST);
        add(havuzPanel);

        // Havuz Ayar Tablosu
        JPanel havuzAyarPanel = new JPanel(new BorderLayout());
        havuzAyarTable = new JTable();
        JButton havuzAyarEkleButton = new JButton("Havuz Ayarı Ekle");
        JButton havuzAyarSilButton = new JButton("Havuz Ayarı Sil");
     
        havuzAyarEkleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                havuzAyarEkle();
            }
        });
        havuzAyarSilButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = havuzAyarTable.getSelectedRow();
                if (selectedRow != -1) {
                    int havuzAyarId = (int) havuzAyarTable.getValueAt(selectedRow, 0);
                    int choice = JOptionPane.showConfirmDialog(null, "Seçilen havuz ayarını silmek istediğinize emin misiniz?", "Havuz Ayarı Sil", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        havuzAyarSil(havuzAyarId);
                    }
                }
            }
        });
        JScrollPane havuzAyarScrollPane = new JScrollPane(havuzAyarTable);
        havuzAyarPanel.add(new JLabel("Havuz Ayarları"), BorderLayout.NORTH);
        havuzAyarPanel.add(havuzAyarScrollPane, BorderLayout.CENTER);
        havuzAyarPanel.add(havuzAyarEkleButton, BorderLayout.WEST);
        havuzAyarPanel.add(havuzAyarSilButton, BorderLayout.EAST);
        add(havuzAyarPanel);

        // Rezervasyon Tablosu
        JPanel rezervasyonPanel = new JPanel(new BorderLayout());
        rezervasyonTable = new JTable();
        JButton rezervasyonEkleButton = new JButton("Rezervasyon Ekle");
        JButton rezervasyonSilButton = new JButton("Rezervasyon Sil");
      
        rezervasyonEkleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rezervasyonEkle();
            }
        });
        rezervasyonSilButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = rezervasyonTable.getSelectedRow();
                if (selectedRow != -1) {
                    int rezervasyonId = (int) rezervasyonTable.getValueAt(selectedRow, 0);
                    int choice = JOptionPane.showConfirmDialog(null, "Seçilen rezervasyonu silmek istediğinize emin misiniz?", "Rezervasyon Sil", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        rezervasyonSil(rezervasyonId);
                    }
                }
            }
        });
      

        JScrollPane rezervasyonScrollPane = new JScrollPane(rezervasyonTable);
        rezervasyonPanel.add(new JLabel("Rezervasyonlar"), BorderLayout.NORTH);
        rezervasyonPanel.add(rezervasyonScrollPane, BorderLayout.CENTER);
        rezervasyonPanel.add(rezervasyonEkleButton, BorderLayout.WEST);
        rezervasyonPanel.add(rezervasyonSilButton, BorderLayout.EAST);
        add(rezervasyonPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        connectToDatabase();
        updateKullaniciTable();
        updateHavuzTable();
        updateHavuzAyarTable();
        updateRezervasyonTable();
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            connection.setAutoCommit(false); // Otomatik işlem modunu devre dışı bırak
        } catch (SQLException ex) {
            System.out.println("Veritabanına bağlantı sırasında hata oluştu: " + ex.getMessage());
        }
    }

    private void kullaniciGuncelle(int kullaniciId, String ad, String soyad, String kullaniciAdi, String sifre) {
        try {
            String sql = "UPDATE kullanicilar SET ad = ?, soyad = ?, kullaniciad = ?, sifre = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, ad);
            statement.setString(2, soyad);
            statement.setString(3, kullaniciAdi);
            statement.setString(4, sifre);
            statement.setInt(5, kullaniciId);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Kullanıcı güncellendi.");
                connection.commit(); // Veri tabanına güncellemenin yansıması için commit işlemi
            } else {
                System.out.println("Kullanıcı güncellenirken bir hata oluştu.");
            }

            statement.close();
        } catch (SQLException ex) {
            System.out.println("Veritabanı bağlantısı sırasında hata oluştu: " + ex.getMessage());
            System.out.println("Kullanıcı güncellenirken bir hata oluştu.");
        }
    }

    private void havuzGuncelle(int havuzId, String havuzAdi, String doluluk) {
        try {
            String sql = "UPDATE havuzlar SET havuzad = ?, doluluk = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, havuzAdi);
            statement.setString(2, doluluk);
            statement.setInt(3, havuzId);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Havuz güncellendi.");
                connection.commit();
                updateHavuzTable(); 
            } else {
                System.out.println("Havuz güncellenirken bir hata oluştu.");
            }

            statement.close();
        } catch (SQLException ex) {
            System.out.println("Veritabanı bağlantısı sırasında hata oluştu: " + ex.getMessage());
        }
    }

    private void havuzAyarGuncelle(int havuzAyarId, float sicaklik, float phDeger, float kimyasalOran, boolean aydinlatma) {
        try {
            String sql = "UPDATE havuzayarlar SET sicaklik = ?, phdeger = ?, kimyasaloran = ?, aydinlatma = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setFloat(1, sicaklik);
            statement.setFloat(2, phDeger);
            statement.setFloat(3, kimyasalOran);
            statement.setBoolean(4, aydinlatma);
            statement.setInt(5, havuzAyarId);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Havuz ayarları güncellendi.");
                connection.commit(); // Veri tabanına güncellemenin yansıması için commit işlemi
            } else {
                System.out.println("Havuz ayarları güncellenirken bir hata oluştu.");
            }

            statement.close();
        } catch (SQLException ex) {
            System.out.println("Veritabanı bağlantısı sırasında hata oluştu: " + ex.getMessage());
        }
    }

    private void rezervasyonGuncelle(int rezervasyonId, int kullaniciId, int havuzId, String havuzGiris) {
        try {
            String sql = "UPDATE rezervasyonlar SET kullaniciid = ?, havuzid = ?, havuzgiris = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, kullaniciId);
            statement.setInt(2, havuzId);
            statement.setString(3, havuzGiris);
            statement.setInt(4, rezervasyonId);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Rezervasyon güncellendi.");
                connection.commit(); // Veri tabanına güncellemenin yansıması için commit işlemi
            } else {
                System.out.println("Rezervasyon güncellenirken bir hata oluştu.");
            }

            statement.close();
        } catch (SQLException ex) {
            System.out.println("Veritabanı bağlantısı sırasında hata oluştu: " + ex.getMessage());
        }
    }

    private void kullaniciEkle() {
        try {
            String ad = JOptionPane.showInputDialog("Ad:");
            String soyad = JOptionPane.showInputDialog("Soyad:");
            String kullaniciAdi = JOptionPane.showInputDialog("Kullanıcı Adı:");
            String sifre = JOptionPane.showInputDialog("Şifre:");

            String sql = "INSERT INTO kullanicilar (ad, soyad, kullaniciad, sifre) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, ad);
            statement.setString(2, soyad);
            statement.setString(3, kullaniciAdi);
            statement.setString(4, sifre);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Kullanıcı eklendi.");
                connection.commit();
                updateKullaniciTable();
            } else {
                System.out.println("Kullanıcı eklenirken bir hata oluştu.");
            }

            statement.close();
        } catch (SQLException ex) {
            System.out.println("Veritabanı bağlantısı sırasında hata oluştu: " + ex.getMessage());
        }
    }

    private void havuzEkle() {
        try {
            String havuzAdi = JOptionPane.showInputDialog("Havuz Adı:");
            String doluluk = JOptionPane.showInputDialog("Doluluk:");

            String sql = "INSERT INTO havuzlar (havuzad, doluluk) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, havuzAdi);
            statement.setString(2, doluluk);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Havuz eklendi.");
                connection.commit();
                updateHavuzTable();
            } else {
                System.out.println("Havuz eklenirken bir hata oluştu.");
            }

            statement.close();
        } catch (SQLException ex) {
            System.out.println("Veritabanı bağlantısı sırasında hata oluştu: " + ex.getMessage());
        }
    }

    private void havuzAyarEkle() {
        try {
            float sicaklik = Float.parseFloat(JOptionPane.showInputDialog("Sıcaklık:"));
            float phDeger = Float.parseFloat(JOptionPane.showInputDialog("pH Değer:"));
            float kimyasalOran = Float.parseFloat(JOptionPane.showInputDialog("Kimyasal Oran:"));
            boolean aydinlatma = Boolean.parseBoolean(JOptionPane.showInputDialog("Aydınlatma (true/false):"));
            int havuzId = Integer.parseInt(JOptionPane.showInputDialog("Havuz ID:"));
            String sql = "INSERT INTO havuzayarlar (sicaklik, phdeger, kimyasaloran, aydinlatma, havuzid) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setFloat(1, sicaklik);
            statement.setFloat(2, phDeger);
            statement.setFloat(3, kimyasalOran);
            statement.setBoolean(4, aydinlatma);
            statement.setInt(5, havuzId);
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Havuz ayarı eklendi.");
                connection.commit();
                updateHavuzAyarTable();
            } else {
                System.out.println("Havuz ayarı eklenirken bir hata oluştu.");
            }

            statement.close();
        } catch (SQLException ex) {
            System.out.println("Veritabanı bağlantısı sırasında hata oluştu: " + ex.getMessage());
        }
    }

    private void rezervasyonEkle() {
        try {
            int kullaniciId = Integer.parseInt(JOptionPane.showInputDialog("Kullanıcı ID:"));
            int havuzId = Integer.parseInt(JOptionPane.showInputDialog("Havuz ID:"));
            String havuzGiris = JOptionPane.showInputDialog("Havuz Giriş Tarihi:");

            String sql = "INSERT INTO rezervasyonlar (kullaniciid, havuzid, havuzgiris) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, kullaniciId);
            statement.setInt(2, havuzId);
            statement.setString(3, havuzGiris);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Rezervasyon eklendi.");
                connection.commit();
                updateRezervasyonTable();
            } else {
                System.out.println("Rezervasyon eklenirken bir hata oluştu.");
            }

            statement.close();
        } catch (SQLException ex) {
            System.out.println("Veritabanı bağlantısı sırasında hata oluştu: " + ex.getMessage());
        }
    }

    private void updateKullaniciTable() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM kullanicilar");

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Ad");
            model.addColumn("Soyad");
            model.addColumn("Kullanıcı Adı");
            model.addColumn("Şifre");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String ad = resultSet.getString("ad");
                String soyad = resultSet.getString("soyad");
                String kullaniciAdi = resultSet.getString("kullaniciad");
                String sifre = resultSet.getString("sifre");

                model.addRow(new Object[] { id, ad, soyad, kullaniciAdi, sifre });
            }

            kullaniciTable.setModel(model);

            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            System.out.println("Veritabanı bağlantısı sırasında hata oluştu: " + ex.getMessage());
        }
    }

    private void updateHavuzTable() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM havuzlar");

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Havuz Adı");
            model.addColumn("Doluluk");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String havuzAdi = resultSet.getString("havuzad");
                String doluluk = resultSet.getString("doluluk");

                model.addRow(new Object[] { id, havuzAdi, doluluk });
            }

            havuzTable.setModel(model);

            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            System.out.println("Veritabanı bağlantısı sırasında hata oluştu: " + ex.getMessage());
        }
    }

    private void updateHavuzAyarTable() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM havuzayarlar");

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Sıcaklık");
            model.addColumn("pH Değer");
            model.addColumn("Kimyasal Oran");
            model.addColumn("Aydınlatma");
            model.addColumn("Havuz id");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                float sicaklik = resultSet.getFloat("sicaklik");
                float phDeger = resultSet.getFloat("phdeger");
                float kimyasalOran = resultSet.getFloat("kimyasaloran");
                boolean aydinlatma = resultSet.getBoolean("aydinlatma");
                int havuzid = resultSet.getInt("havuzid");

                model.addRow(new Object[] { id, sicaklik, phDeger, kimyasalOran, aydinlatma, havuzid });
            }

            havuzAyarTable.setModel(model);

            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            System.out.println("Veritabanı bağlantısı sırasında hata oluştu: " + ex.getMessage());
        }
    }

    private void updateRezervasyonTable() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM rezervasyonlar");

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Kullanıcı ID");
            model.addColumn("Havuz ID");
            model.addColumn("Havuz Giriş");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int kullaniciId = resultSet.getInt("kullaniciid");
                int havuzId = resultSet.getInt("havuzid");
                String havuzGiris = resultSet.getString("havuzgiris");

                model.addRow(new Object[] { id, kullaniciId, havuzId, havuzGiris });
            }

            rezervasyonTable.setModel(model);

            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            System.out.println("Veritabanı bağlantısı sırasında hata oluştu: " + ex.getMessage());
        }
    }
    private void kullaniciSil(int kullaniciId) {
        try {
            // Kullanıcıya ait rezervasyonları sil
            String rezervasyonSilSql = "DELETE FROM rezervasyonlar WHERE kullaniciid = ?";
            PreparedStatement rezervasyonSilStatement = connection.prepareStatement(rezervasyonSilSql);
            rezervasyonSilStatement.setInt(1, kullaniciId);
            rezervasyonSilStatement.executeUpdate();
            rezervasyonSilStatement.close();

            // Kullanıcıyı sil
            String kullaniciSilSql = "DELETE FROM kullanicilar WHERE id = ?";
            PreparedStatement kullaniciSilStatement = connection.prepareStatement(kullaniciSilSql);
            kullaniciSilStatement.setInt(1, kullaniciId);

            int affectedRows = kullaniciSilStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Kullanıcı silindi.");
                connection.commit();
                updateKullaniciTable();
            } else {
                System.out.println("Kullanıcı silinirken bir hata oluştu.");
            }

            kullaniciSilStatement.close();
        } catch (SQLException ex) {
            System.out.println("Veritabanı bağlantısı sırasında hata oluştu: " + ex.getMessage());
        }
    }
    private void rezervasyonSil(int rezervasyonId) {
        try {
            String sql = "DELETE FROM rezervasyonlar WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, rezervasyonId);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Rezervasyon silindi.");
                connection.commit();
                updateRezervasyonTable();
            } else {
                System.out.println("Rezervasyon silinirken bir hata oluştu.");
            }

            statement.close();
        } catch (SQLException ex) {
            System.out.println("Veritabanı bağlantısı sırasında hata oluştu: " + ex.getMessage());
        }
    }

    private void havuzAyarSil(int havuzAyarId) {
        try {
            String havuzAyarSilSql = "DELETE FROM havuzayarlar WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(havuzAyarSilSql);
            statement.setInt(1, havuzAyarId);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Havuz ayarı silindi.");
                connection.commit();
                updateHavuzAyarTable();
            } else {
                System.out.println("Havuz ayarı silinirken bir hata oluştu.");
            }

            statement.close();
        } catch (SQLException ex) {
            System.out.println("Veritabanı bağlantısı sırasında hata oluştu: " + ex.getMessage());
        }
    }

    private void havuzSil(int havuzId) {
        try {
           
            String havuzAyarSilSql = "DELETE FROM havuzayarlar WHERE havuzid = ?";
            PreparedStatement havuzAyarSilStatement = connection.prepareStatement(havuzAyarSilSql);
            havuzAyarSilStatement.setInt(1, havuzId);
            int havuzAyarSilAffectedRows = havuzAyarSilStatement.executeUpdate();

           
            String havuzSilSql = "DELETE FROM havuzlar WHERE id = ?";
            PreparedStatement havuzSilStatement = connection.prepareStatement(havuzSilSql);
            havuzSilStatement.setInt(1, havuzId);
            int havuzSilAffectedRows = havuzSilStatement.executeUpdate();

            if (havuzSilAffectedRows > 0) {
                System.out.println("Havuz silindi.");
                connection.commit();
                updateHavuzTable();
            } else {
                System.out.println("Havuz silinirken bir hata oluştu.");
            }

            havuzAyarSilStatement.close();
            havuzSilStatement.close();
        } catch (SQLException ex) {
            System.out.println("Veritabanı bağlantısı sırasında hata oluştu: " + ex.getMessage());
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AdminPaneli();
            }
        });
    }
}