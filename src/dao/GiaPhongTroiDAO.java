package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.GiaPhongTroi;
import model.LoaiPhong;
import util.DbConnection;
import util.ExHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GiaPhongTroiDAO {

    private static GiaPhongTroiDAO instance = new GiaPhongTroiDAO();

    public static GiaPhongTroiDAO getInstance() {
        return instance;
    }

    public boolean create(GiaPhongTroi giaPhongTroi) {

        String sql = "INSERT INTO gia_phong(ma_loai_phong, ngay_bd, ngay_kt, lap_lai, loai_gia, luong, he_so, ghi_chu) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        boolean result = false;

        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setNString(1, giaPhongTroi.getLoaiPhong().getMaLoaiPhong());
            stmt.setDate(2, giaPhongTroi.getNgayBatDau());
            stmt.setDate(3, giaPhongTroi.getNgayKetThuc());
            stmt.setBoolean(4, giaPhongTroi.getLoaiGia());
            stmt.setBoolean(5, giaPhongTroi.getLoaiGia());
            stmt.setLong(6, giaPhongTroi.getGiaTri());
            stmt.setFloat(7, giaPhongTroi.getHeSo());
            stmt.setNString(8, giaPhongTroi.getGhiChu());

            result = (stmt.executeUpdate() > 0);

            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }

        return result;
    }

    public ObservableList<GiaPhongTroi> getAll() {
        ObservableList<LoaiPhong> dsLoaiPhong = LoaiPhongDAO.getInstance().getAll();
        ObservableList<GiaPhongTroi> dsGiaPhongTroi = FXCollections.observableArrayList();
        GiaPhongTroi giaPhongTroi = null;

        String sql = "SELECT ma_gia_phong, ma_loai_phong, ngay_bd, ngay_kt, lap_lai, loai_gia, luong, he_so, ghi_chu FROM gia_phong_troi";

        Connection con = DbConnection.getConnection();
        ResultSet rs;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                giaPhongTroi = new GiaPhongTroi(
                        rs.getInt(1),
                        getLoaiPhongTuArray(rs.getNString(2), dsLoaiPhong),
                        rs.getDate(3),
                        rs.getDate(4),
                        rs.getBoolean(5),
                        rs.getBoolean(6),
                        rs.getLong(7),
                        rs.getFloat(8),
                        rs.getNString(9)
                );
                dsGiaPhongTroi.add(giaPhongTroi);
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }
        return dsGiaPhongTroi;
    }

    public boolean update(GiaPhongTroi giaPhongTroi) {
        String sql = "UPDATE gia_phong_troi SET ma_loai_phong=?, ngay_bd=?, ngay_kt=?, lap_lai=?, loai_gia=?, luong=?, he_so=?, ghi_chu=? WHERE ma_gia_phong=?";
        Connection con = DbConnection.getConnection();
        boolean result = false;
        try {
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setNString(1, giaPhongTroi.getLoaiPhong().getMaLoaiPhong());
            stmt.setDate(2, giaPhongTroi.getNgayBatDau());
            stmt.setDate(3, giaPhongTroi.getNgayKetThuc());
            stmt.setBoolean(4, giaPhongTroi.isLapLai());
            stmt.setBoolean(5, giaPhongTroi.getLoaiGia());
            stmt.setLong(6, giaPhongTroi.getGiaTri());
            stmt.setFloat(7, giaPhongTroi.getHeSo());
            stmt.setNString(8, giaPhongTroi.getGhiChu());
            stmt.setInt(9, giaPhongTroi.getMaGiaPhong());

            result = (stmt.executeUpdate() > 0);
            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }
        return result;
    }

    public boolean delete(GiaPhongTroi giaPhongTroi) {
        String sql = "DELETE FROM gia_phong_troi WHERE ma_gia_phong=?";
        Connection con = DbConnection.getConnection();
        boolean result = false;
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, giaPhongTroi.getMaGiaPhong());

            result = (stmt.executeUpdate() > 0);
            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }
        return result;
    }

    private LoaiPhong getLoaiPhongTuArray(String maLoaiPhong, ObservableList<LoaiPhong> dsLoaiPhong) {
        for (LoaiPhong loaiPhong: dsLoaiPhong) {
            if (loaiPhong.getMaLoaiPhong().equals(maLoaiPhong))
            return loaiPhong;
        }
        return null;
    }
}

