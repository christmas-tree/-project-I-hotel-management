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

        String sql = "INSERT INTO gia_phong_troi(ma_loai_phong, ten, ngay_bd, ngay_kt, lap_lai, loai_gia, luong, he_so, ghi_chu, hieu_luc) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        boolean result = false;
        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setNString(1, giaPhongTroi.getLoaiPhong().getMaLoaiPhong());
            stmt.setNString(2, giaPhongTroi.getTen());
            stmt.setDate(3, giaPhongTroi.getNgayBatDau());
            stmt.setDate(4, giaPhongTroi.getNgayKetThuc());
            stmt.setBoolean(5, giaPhongTroi.getLoaiGia());
            stmt.setBoolean(6, giaPhongTroi.getLoaiGia());
            stmt.setLong(7, giaPhongTroi.getGiaTri());
            stmt.setFloat(8, giaPhongTroi.getHeSo());
            stmt.setNString(9, giaPhongTroi.getGhiChu());
            stmt.setBoolean(10, false);

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

        String sql = "SELECT ma_gia_phong, ma_loai_phong, ten, ngay_bd, ngay_kt, lap_lai, loai_gia, luong, he_so, ghi_chu, hieu_luc FROM gia_phong_troi";

        Connection con = DbConnection.getConnection();
        ResultSet rs;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                giaPhongTroi = new GiaPhongTroi(
                        rs.getInt(1),
                        getLoaiPhongTuArray(rs.getNString(2), dsLoaiPhong),
                        rs.getNString(3),
                        rs.getDate(3),
                        rs.getDate(4),
                        rs.getBoolean(5),
                        rs.getBoolean(6),
                        rs.getLong(7),
                        rs.getFloat(8),
                        rs.getNString(9),
                        rs.getBoolean(10)
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
        String sql = "UPDATE gia_phong_troi SET ma_loai_phong=?, ten=?, ngay_bd=?, ngay_kt=?, lap_lai=?, loai_gia=?, luong=?, he_so=?, ghi_chu=?, hieu_luc=? WHERE ma_gia_phong=?";
        Connection con = DbConnection.getConnection();
        boolean result = false;
        try {
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setNString(1, giaPhongTroi.getLoaiPhong().getMaLoaiPhong());
            stmt.setNString(2, giaPhongTroi.getTen());
            stmt.setDate(3, giaPhongTroi.getNgayBatDau());
            stmt.setDate(4, giaPhongTroi.getNgayKetThuc());
            stmt.setBoolean(5, giaPhongTroi.isLapLai());
            stmt.setBoolean(6, giaPhongTroi.getLoaiGia());
            stmt.setLong(7, giaPhongTroi.getGiaTri());
            stmt.setFloat(8, giaPhongTroi.getHeSo());
            stmt.setNString(9, giaPhongTroi.getGhiChu());
            stmt.setInt(10, giaPhongTroi.getMaGiaPhong());
            stmt.setBoolean(11, giaPhongTroi.isHieuLuc());

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

