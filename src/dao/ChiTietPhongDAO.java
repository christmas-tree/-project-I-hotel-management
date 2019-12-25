package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.ChiTietPhong;
import model.Phong;
import util.DbConnection;
import util.ExceptionHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChiTietPhongDAO {

    private static ChiTietPhongDAO instance = new ChiTietPhongDAO();

    public static ChiTietPhongDAO getInstance() {
        return instance;
    }

    public boolean create(ChiTietPhong chiTietPhong) {

        String sql = "INSERT INTO chi_tiet_phong(ma_phong, ten_do, so_luong, don_vi, gia_tien, trang_thai, ghi_chu) VALUES (?, ?, ?, ?, ?, ?, ?)";

        boolean result = false;

        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, chiTietPhong.getPhong().getMaPhong());
            stmt.setNString(2, chiTietPhong.getTenDo());
            stmt.setInt(3, chiTietPhong.getSoLuong());
            stmt.setNString(4, chiTietPhong.getDonVi());
            stmt.setLong(5, chiTietPhong.getGiaTien());
            stmt.setInt(6, chiTietPhong.getTrangThai());
            stmt.setNString(7, chiTietPhong.getGhiChu());

            result = (stmt.executeUpdate() > 0);

            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExceptionHandler.handle(e);
        }

        return result;
    }

    public boolean create(ObservableList<ChiTietPhong> dsChiTietPhong) {

        String sql = "INSERT INTO chi_tiet_phong(ma_phong, ten_do, so_luong, don_vi, gia_tien, trang_thai, ghi_chu) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            ChiTietPhong chiTietPhong = null;

            for (int i = 0; i < dsChiTietPhong.size(); i++) {
                chiTietPhong = dsChiTietPhong.get(i);

                stmt.setInt(1, chiTietPhong.getPhong().getMaPhong());
                stmt.setNString(2, chiTietPhong.getTenDo());
                stmt.setInt(3, chiTietPhong.getSoLuong());
                stmt.setNString(4, chiTietPhong.getDonVi());
                stmt.setLong(5, chiTietPhong.getGiaTien());
                stmt.setInt(6, chiTietPhong.getTrangThai());
                stmt.setNString(7, chiTietPhong.getGhiChu());

                stmt.executeUpdate();
            }
            stmt.close();
            con.close();

            return true;
        } catch (SQLException e) {
            ExceptionHandler.handle(e);
            return false;
        }
    }

    public ObservableList<ChiTietPhong> getAll(Phong phong) {
        ObservableList<ChiTietPhong> dsChiTietPhong = FXCollections.observableArrayList();
        ChiTietPhong chiTietPhong = null;

        String sql = "SELECT ten_do, so_luong, gia_tien, don_vi, trang_thai, ghi_chu FROM chi_tiet_phong WHERE ma_phong=?";

        Connection con = DbConnection.getConnection();
        ResultSet rs;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, phong.getMaPhong());
            rs = stmt.executeQuery();

            while(rs.next()) {
                chiTietPhong = new ChiTietPhong(
                        phong,
                        rs.getNString(1),
                        rs.getInt(2),
                        rs.getLong(3),
                        rs.getNString(4),
                        rs.getInt(5),
                        rs.getNString(6)
                );
                dsChiTietPhong.add(chiTietPhong);
            }
            rs.close();
            stmt.close();
            con.close();
            phong.setDsChiTietPhong(dsChiTietPhong);
        } catch (SQLException e) {
            ExceptionHandler.handle(e);
        }

        return dsChiTietPhong;
    }

    public boolean update(ObservableList<ChiTietPhong> dsChiTietPhong) {
//        String sql2 = "UPDATE chi_tiet_phong SET so_luong=?, gia_tien=?, don_vi=?, trang_thai=?, ghi_chu=? WHERE ma_phong=? AND ten_do=?";
        String sql = "IF NOT EXISTS (SELECT * FROM [chi_tiet_phong] WHERE ma_phong=? AND ten_do=?) " +
                "INSERT INTO chi_tiet_phong(ma_phong, ten_do, so_luong, don_vi, gia_tien, trang_thai, ghi_chu) VALUES (?, ?, ?, ?, ?, ?, ?) " +
                "ELSE " +
                "UPDATE chi_tiet_phong SET so_luong=?, gia_tien=?, don_vi=?, trang_thai=?, ghi_chu=? WHERE ma_phong=? AND ten_do=?";

        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            ChiTietPhong chiTietPhong = null;

            for (int i = 0; i < dsChiTietPhong.size(); i++) {
                chiTietPhong = dsChiTietPhong.get(i);

                stmt.setInt(1, chiTietPhong.getPhong().getMaPhong());
                stmt.setNString(2, chiTietPhong.getTenDo());
                stmt.setInt(3, chiTietPhong.getPhong().getMaPhong());
                stmt.setNString(4, chiTietPhong.getTenDo());
                stmt.setInt(5, chiTietPhong.getSoLuong());
                stmt.setNString(6, chiTietPhong.getDonVi());
                stmt.setLong(7, chiTietPhong.getGiaTien());
                stmt.setInt(8, chiTietPhong.getTrangThai());
                stmt.setNString(9, chiTietPhong.getGhiChu());
                stmt.setInt(10, chiTietPhong.getSoLuong());
                stmt.setLong(11, chiTietPhong.getGiaTien());
                stmt.setNString(12, chiTietPhong.getDonVi());
                stmt.setInt(13, chiTietPhong.getTrangThai());
                stmt.setNString(14, chiTietPhong.getGhiChu());
                stmt.setInt(15, chiTietPhong.getPhong().getMaPhong());
                stmt.setNString(16, chiTietPhong.getTenDo());

                stmt.executeUpdate();
            }
            stmt.close();
            con.close();

            return true;
        } catch (SQLException e) {
            ExceptionHandler.handle(e);
            return false;
        }
    }

    public boolean delete(ArrayList<ChiTietPhong> dsChiTietPhongXoa) {
        String sql = "DELETE FROM chi_tiet_phong WHERE ma_phong=? AND ten_do=?";
        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            ChiTietPhong chiTietPhong = null;

            for (int i = 0; i < dsChiTietPhongXoa.size(); i++) {
                chiTietPhong = dsChiTietPhongXoa.get(i);

                stmt.setInt(1, chiTietPhong.getPhong().getMaPhong());
                stmt.setNString(2, chiTietPhong.getTenDo());

                stmt.executeUpdate();
            }
            stmt.close();
            con.close();

            return true;
        } catch (SQLException e) {
            ExceptionHandler.handle(e);
            return false;
        }
    }

    public void importData(ArrayList<ChiTietPhong> dsChiTietPhong  ) {
        String sql = "INSERT INTO chi_tiet_phong(ma_phong, ten_do, so_luong, don_vi, gia_tien, trang_thai, ghi_chu) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            String err = "";
            for (int i = 0; i < dsChiTietPhong.size(); i++) {
                ChiTietPhong chiTietPhong = dsChiTietPhong.get(i);
                try {
                    stmt.setInt(1, chiTietPhong.getPhong().getMaPhong());
                    stmt.setNString(2, chiTietPhong.getTenDo());
                    stmt.setInt(3, chiTietPhong.getSoLuong());
                    stmt.setNString(4, chiTietPhong.getDonVi());
                    stmt.setLong(5, chiTietPhong.getGiaTien());
                    stmt.setInt(6, chiTietPhong.getTrangThai());
                    stmt.setNString(7, chiTietPhong.getGhiChu());

                    stmt.executeUpdate();
                } catch (SQLException e) {
                    err += "Có vấn đề nhập mục số " + (i + 1) + " - " + chiTietPhong.getTenDo() + ".\n";
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

