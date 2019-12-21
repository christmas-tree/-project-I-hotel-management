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
import java.util.ArrayList;
import java.util.HashMap;

public class LoaiPhongDAO {

    private static LoaiPhongDAO instance = new LoaiPhongDAO();

    public static LoaiPhongDAO getInstance() {
        return instance;
    }

    public boolean create(LoaiPhong loaiPhong) {

        String sql = "INSERT INTO loai_phong(ma_loai_phong, loai_phong, gia_tien, so_nguoi, ghi_chu) VALUES (?, ?, ?, ?, ?)";
        boolean result = false;

        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, loaiPhong.getMaLoaiPhong());
            stmt.setNString(2, loaiPhong.getLoaiPhong());
            stmt.setLong(3, loaiPhong.getGiaTien());
            stmt.setInt(4, loaiPhong.getSoNguoi());
            stmt.setNString(5, loaiPhong.getGhiChu());

            result = (stmt.executeUpdate() > 0);

            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }

        return result;
    }

    public ObservableList<LoaiPhong> getAll() {
        ObservableList<LoaiPhong> dsLoaiPhong = FXCollections.observableArrayList();
        LoaiPhong loaiPhong = null;

        String sql = "SELECT ma_loai_phong, loai_phong, gia_tien, so_nguoi, ghi_chu FROM loai_phong";

        String sql2 = "SELECT ma_gia_phong, ngay_bd, ngay_kt, lap_lai, loai_gia, luong, he_so, ghi_chu FROM gia_phong_troi WHERE ma_loai_phong=? AND hieu_luc=1";

        Connection con = DbConnection.getConnection();
        ResultSet rs;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                loaiPhong = new LoaiPhong(
                        rs.getString(1),
                        rs.getNString(2),
                        rs.getLong(3),
                        rs.getInt(4),
                        rs.getNString(5)
                );
                dsLoaiPhong.add(loaiPhong);
            }
            rs.close();
            stmt.close();
            stmt = con.prepareStatement(sql2);

            for (LoaiPhong loaiPhongA: dsLoaiPhong) {
                ArrayList<GiaPhongTroi> dsGiaPhongTroi = new ArrayList<>();
                stmt.setString(1, loaiPhongA.getMaLoaiPhong());
                rs = stmt.executeQuery();
                while (rs.next()) {
                    GiaPhongTroi giaPhongTroi = new GiaPhongTroi(
                            rs.getInt(1),
                            loaiPhongA,
                            rs.getDate(2),
                            rs.getDate(3),
                            rs.getBoolean(4),
                            rs.getBoolean(5),
                            rs.getLong(6),
                            rs.getFloat(7),
                            rs.getNString(8)
                    );
                    dsGiaPhongTroi.add(giaPhongTroi);
                }
                loaiPhongA.setDsGiaTroi(dsGiaPhongTroi);
            }
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }
        return dsLoaiPhong;
    }

    public HashMap<String, LoaiPhong> getMap() {
        HashMap<String, LoaiPhong> dsLoaiPhong = new HashMap<>();
        LoaiPhong loaiPhong = null;

        String sql = "SELECT ma_loai_phong, loai_phong, gia_tien, so_nguoi, ghi_chu FROM loai_phong";

        Connection con = DbConnection.getConnection();
        ResultSet rs;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                loaiPhong = new LoaiPhong(
                        rs.getString(1),
                        rs.getNString(2),
                        rs.getLong(3),
                        rs.getInt(4),
                        rs.getNString(5)
                );
                dsLoaiPhong.put(loaiPhong.getMaLoaiPhong(), loaiPhong);
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }
        return dsLoaiPhong;
    }

    public LoaiPhong get(String maLoaiPhong) {
        LoaiPhong loaiPhong = null;
        String sql = "SELECT loai_phong, gia_tien, so_nguoi, ghi_chu FROM loai_phong WHERE ma_loai_phong=?";

        Connection con = DbConnection.getConnection();
        ResultSet rs;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                loaiPhong = new LoaiPhong(
                        maLoaiPhong,
                        rs.getNString(1),
                        rs.getLong(2),
                        rs.getInt(3),
                        rs.getNString(4)
                );
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }
        return loaiPhong;
    }

    public boolean update(LoaiPhong loaiPhong) {
        String sql = "UPDATE loai_phong SET loai_phong=?, gia_tien=?, so_nguoi=?, ghi_chu=? WHERE ma_loai_phong=?";
        Connection con = DbConnection.getConnection();
        boolean result = false;
        try {
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setNString(1, loaiPhong.getLoaiPhong());
            stmt.setLong(2, loaiPhong.getGiaTien());
            stmt.setInt(3, loaiPhong.getSoNguoi());
            stmt.setNString(4, loaiPhong.getGhiChu());
            stmt.setString(5, loaiPhong.getMaLoaiPhong());

            result = (stmt.executeUpdate() > 0);
            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }
        return result;
    }

    public boolean delete(LoaiPhong loaiPhong) {
        String sql = "DELETE FROM loai_phong WHERE ma_loai_phong=?";
        Connection con = DbConnection.getConnection();
        boolean result = false;
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, loaiPhong.getMaLoaiPhong());

            result = (stmt.executeUpdate() > 0);
            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }
        return result;
    }
}

