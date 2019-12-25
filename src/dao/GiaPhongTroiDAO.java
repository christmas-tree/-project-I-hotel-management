package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.GiaPhongTroi;
import model.LoaiPhong;
import util.DbConnection;
import util.ExceptionHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GiaPhongTroiDAO {

    private static GiaPhongTroiDAO instance = new GiaPhongTroiDAO();

    public static GiaPhongTroiDAO getInstance() {
        return instance;
    }

    public boolean create(GiaPhongTroi giaPhongTroi) {

        String sql = "INSERT INTO gia_phong_troi(ma_loai_phong, ten, ngay_bd, ngay_kt, lap_lai, gia_tien, ghi_chu) VALUES (?, ?, ?, ?, ?, ?, ?)";
        boolean result = false;
        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, giaPhongTroi.getLoaiPhong().getMaLoaiPhong());
            stmt.setNString(2, giaPhongTroi.getTen());
            stmt.setDate(3, giaPhongTroi.getNgayBatDau());
            stmt.setDate(4, giaPhongTroi.getNgayKetThuc());
            stmt.setInt(5, giaPhongTroi.getLapLai());
            stmt.setLong(6, giaPhongTroi.getGiaTien());
            stmt.setNString(7, giaPhongTroi.getGhiChu());

            result = (stmt.executeUpdate() > 0);

            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExceptionHandler.handle(e);
        }

        return result;
    }

    public ObservableList<GiaPhongTroi> getAll() {
        ObservableList<LoaiPhong> dsLoaiPhong = LoaiPhongDAO.getInstance().getAll();
        ObservableList<GiaPhongTroi> dsGiaPhongTroi = FXCollections.observableArrayList();
        GiaPhongTroi giaPhongTroi = null;

        String sql = "SELECT ma_gia_phong, ma_loai_phong, ten, ngay_bd, ngay_kt, lap_lai, gia_tien, ghi_chu FROM gia_phong_troi";

        Connection con = DbConnection.getConnection();
        ResultSet rs;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                giaPhongTroi = new GiaPhongTroi(
                        rs.getInt(1),
                        getLoaiPhongTuArray(rs.getString(2), dsLoaiPhong),
                        rs.getNString(3),
                        rs.getDate(4),
                        rs.getDate(5),
                        rs.getInt(6),
                        rs.getLong(7),
                        rs.getNString(8)
                );
                dsGiaPhongTroi.add(giaPhongTroi);
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExceptionHandler.handle(e);
        }
        return dsGiaPhongTroi;
    }

    public ObservableList<GiaPhongTroi> get(LoaiPhong loaiPhong) {
        ObservableList<GiaPhongTroi> dsGiaPhongTroi = FXCollections.observableArrayList();
        GiaPhongTroi giaPhongTroi = null;

        String sql = "SELECT ma_gia_phong, ma_loai_phong, ten, ngay_bd, ngay_kt, lap_lai, ghi_chu FROM gia_phong_troi WHERE ma_loai_phong=?";

        Connection con = DbConnection.getConnection();
        ResultSet rs;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, loaiPhong.getMaLoaiPhong());
            rs = stmt.executeQuery();

            while (rs.next()) {
                giaPhongTroi = new GiaPhongTroi(
                        rs.getInt(1),
                        loaiPhong,
                        rs.getNString(3),
                        rs.getDate(3),
                        rs.getDate(4),
                        rs.getInt(5),
                        rs.getLong(6),
                        rs.getNString(7)
                );
                dsGiaPhongTroi.add(giaPhongTroi);
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExceptionHandler.handle(e);
        }
        return dsGiaPhongTroi;
    }

    public boolean update(GiaPhongTroi giaPhongTroi) {
        String sql = "UPDATE gia_phong_troi SET ma_loai_phong=?, ten=?, ngay_bd=?, ngay_kt=?, lap_lai=?, gia_tien=?, ghi_chu=? WHERE ma_gia_phong=?";
        Connection con = DbConnection.getConnection();
        boolean result = false;
        try {
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, giaPhongTroi.getLoaiPhong().getMaLoaiPhong());
            stmt.setNString(2, giaPhongTroi.getTen());
            stmt.setDate(3, giaPhongTroi.getNgayBatDau());
            stmt.setDate(4, giaPhongTroi.getNgayKetThuc());
            stmt.setInt(5, giaPhongTroi.getLapLai());
            stmt.setLong(6, giaPhongTroi.getGiaTien());
            stmt.setNString(7, giaPhongTroi.getGhiChu());
            stmt.setInt(8, giaPhongTroi.getMaGiaPhong());

            result = (stmt.executeUpdate() > 0);
            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExceptionHandler.handle(e);
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
            ExceptionHandler.handle(e);
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

        public void importData(ArrayList<GiaPhongTroi> dsGiaPhong) {
            String sql = "INSERT INTO gia_phong_troi(ma_loai_phong, ten, ngay_bd, ngay_kt, lap_lai, gia_tien, ghi_chu) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            String err = "";
            for (int i = 0; i < dsGiaPhong.size(); i++) {
                GiaPhongTroi giaPhongTroi = dsGiaPhong.get(i);
                try {
                    stmt.setString(1, giaPhongTroi.getMaLoaiPhong());
                    stmt.setNString(2, giaPhongTroi.getTen());
                    stmt.setDate(3, giaPhongTroi.getNgayBatDau());
                    stmt.setDate(4, giaPhongTroi.getNgayKetThuc());
                    stmt.setLong(5, giaPhongTroi.getLapLai());
                    stmt.setLong(6, giaPhongTroi.getGiaTien());
                    stmt.setNString(7, giaPhongTroi.getGhiChu());

                    stmt.executeUpdate();
                } catch (SQLException e) {
                    err += "Có vấn đề nhập mục số " + (i + 1) + " - " + giaPhongTroi.getTen() + ".\n";
                }
            }
            stmt.close();
            con.close();

            if (!err.isBlank())
                ExceptionHandler.handleLong(err);
        } catch (SQLException e) {
            ExceptionHandler.handle(e);
        }
    }
}

