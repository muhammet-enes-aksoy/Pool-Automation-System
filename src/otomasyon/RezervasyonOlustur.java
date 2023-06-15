package otomasyon;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class RezervasyonOlustur {

    private Connection conn;

    public RezervasyonOlustur() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/havuzdbS", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getHavuzlar() {
        ArrayList<String> havuzlar = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM havuzlar");
            while (rs.next()) {
                havuzlar.add(rs.getString("havuz_adi"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return havuzlar;
    }

    public void addRezervasyon(String kullaniciAdi, String havuzAdi, String tarih) {
        try {
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO rezervasyonlar (kullanici_adi, havuz_adi, tarih) " +
                         "VALUES ('" + kullaniciAdi + "', '" + havuzAdi + "', '" + tarih + "')";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	public void rezervasyonEkle(String rezervasyonData) {
		// TODO Auto-generated method stub
		
	}
}