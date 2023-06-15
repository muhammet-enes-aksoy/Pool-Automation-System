package otomasyon;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UyeKayit {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/havuzdb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void kaydet(String ad, String soyad, String kullaniciAdi, String sifre) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            System.out.println("Veritabanına bağlantı başarılı!");

            kaydet(connection, ad, soyad, kullaniciAdi, sifre);

            System.out.println("Üyelik oluşturuldu!");
        } catch (SQLException e) {
            System.out.println("Veritabanı bağlantısı sırasında hata oluştu: " + e.getMessage());
        }
    }

    private static void kaydet(Connection connection, String ad, String soyad, String kullaniciAdi, String sifre)
            throws SQLException {
        String sql = "INSERT INTO kullanicilar (id, ad, soyad, kullaniciad, sifre) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, null);
            statement.setString(2, ad);
            statement.setString(3, soyad);
            statement.setString(4, kullaniciAdi);
            statement.setString(5, sifre);

            statement.executeUpdate();
        }
    }
}