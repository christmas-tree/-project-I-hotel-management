package dao;

import javafx.collections.ObservableList;
import model.ChiTietDatPhong;
import model.LoaiPhong;
import model.Phong;
import util.DbConnection;
import util.ExHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class PhongDAO {
    
    private static PhongDAO instance = new PhongDAO();
    
    public static PhongDAO getInstance() {
        return instance;
    }

    public boolean create(Phong phong) {

        String sql = "INSERT INTO phong(ma_phong, ma_loai_phong, tang, trang_thai, ghi_chu) VALUES (?, ?, ?, ?, ?)";

        boolean result = false;

        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, phong.getMaPhong());
            stmt.setString(2, phong.getLoaiPhong().getMaLoaiPhong());
            stmt.setInt(3, phong.getTang());
            stmt.setInt(4, phong.getTrangThai());
            stmt.setNString(5, phong.getGhiChu());

            result = (stmt.executeUpdate() > 0);

            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }

        return result;
    }

    public ArrayList<Phong> getAll(HashMap<String, LoaiPhong> dsLoaiPhong) {
        ArrayList<Phong> dsPhong = new ArrayList<>();
        Phong phong = null;

        String sql = "SELECT * FROM phong ORDER BY tang ASC";

        Connection con = DbConnection.getConnection();
        ResultSet rs;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

            while(rs.next()) {
                System.out.println("trang thai = " + rs.getInt("trang_thai"));
                phong = new Phong(
                        rs.getInt("ma_phong"),
                        dsLoaiPhong.get(rs.getString("ma_loai_phong")),
                        rs.getInt("tang"),
                        rs.getInt("trang_thai"),
                        rs.getNString("ghi_chu")
                );
                dsPhong.add(phong);
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }

        return dsPhong;
    }

    public Phong get(HashMap<String, LoaiPhong> dsLoaiPhong, int maPhong) {
        Phong phong = null;

        String sql = "SELECT * FROM phong WHERE ma_phong=?";

        Connection con = DbConnection.getConnection();
        ResultSet rs;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, maPhong);

            rs = stmt.executeQuery();
            while(rs.next()) {
                phong = new Phong(
                        rs.getInt("ma_phong"),
                        dsLoaiPhong.get(rs.getString("ma_loai_phong")),
                        rs.getInt("tang"),
                        rs.getInt("trang_thai"),
                        rs.getNString("ghi_chu")
                );
            }
            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException e) {
            ExHandler.handle(e);
        }

        return phong;
    }

    public boolean update(Phong phong) {
        String sql = "UPDATE phong SET ma_loai_phong=?, tang=?, trang_thai=?, ghi_chu=? WHERE ma_phong=?";

        boolean result = false;
        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, phong.getLoaiPhong().getMaLoaiPhong());
            stmt.setInt(2, phong.getTang());
            stmt.setInt(3, phong.getTrangThai());
            stmt.setNString(4, phong.getGhiChu());
            stmt.setInt(5, phong.getMaPhong());

            result = (stmt.executeUpdate() > 0);

            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }

        return result;
    }

    public boolean update(ObservableList<ChiTietDatPhong> dsChiTietDatPhong) {
        String sql = "UPDATE phong SET trang_thai=? WHERE ma_phong=?";

        boolean result = false;
        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            for (ChiTietDatPhong chiTietDatPhong: dsChiTietDatPhong) {
                stmt.setInt(1, chiTietDatPhong.getPhong().getTrangThai());
                stmt.setInt(2, chiTietDatPhong.getPhong().getMaPhong());
                stmt.executeUpdate();
            }
            stmt.close();
            con.close();
            result = true;
        } catch (SQLException e) {
            ExHandler.handle(e);
        }
        return result;
    }

    public boolean delete(Phong phong) {
        String sql = "DELETE FROM phong WHERE ma_phong=?";

        boolean result = false;

        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, phong.getMaPhong());

            result = (stmt.executeUpdate() > 0);

            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }

        return result;
    }
}

