package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.DatPhong;
import model.KhachHang;
import model.NhanVien;
import model.Phong;
import util.DbConnection;
import util.ExHandler;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class DatPhongDAO {
    private static DatPhongDAO instance = new DatPhongDAO();

    public static DatPhongDAO getInstance() {
        return instance;
    }
    
    public boolean create(DatPhong datPhong) {
        String sql = "INSERT INTO dat_phong(ngay_dat, phuong_thuc, ngay_checkin, ngay_checkout, ma_kh_dat, ma_nv_nhan ,so_nguoi, tien_dat_coc, la_khach_doan, da_huy, ghi_chu) " +
                "OUTPUT INSERTED.ma_dat_phong " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setTimestamp(1, datPhong.getNgayDat());
            stmt.setNString(2, datPhong.getPhuongThuc());
            stmt.setTimestamp(3, datPhong.getNgayCheckin());
            stmt.setTimestamp(4, datPhong.getNgayCheckout());
            stmt.setInt(5, datPhong.getKhachHang().getMaKh());
            stmt.setInt(6, datPhong.getNvNhan().getMaNv());
            stmt.setInt(7, datPhong.getSoNguoi());
            stmt.setLong(8, datPhong.getTienDatCoc());
            stmt.setBoolean(9, datPhong.isKhachDoan());
            stmt.setBoolean(10, datPhong.isDaHuy());
            stmt.setNString(11, datPhong.getGhiChu());

            ResultSet rs = stmt.executeQuery();

            if (rs.next())
                datPhong.setMaDatPhong(rs.getInt(1));
            else
                datPhong.setMaDatPhong(0);

            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }

        return (datPhong.getMaDatPhong() != 0);
    }

    public ArrayList<DatPhong> getAllActiveBooking() {

        Timestamp today = Timestamp.valueOf(LocalDate.now().atStartOfDay());
        ArrayList<DatPhong> dsDatPhong = new ArrayList<>();
        DatPhong datPhong = null;

        String sql = "SELECT ma_dat_phong, ngay_dat, phuong_thuc, ngay_checkin, ngay_checkout, ma_kh_dat, ten_khach, ma_nv_nhan, ten_nv, so_nguoi, tien_dat_coc, la_khach_doan, da_huy, dat_phong.ghi_chu, da_xong " +
                "FROM dat_phong, khach_hang, nhan_vien " +
                "WHERE dat_phong.ma_kh_dat=khach_hang.ma_kh AND nhan_vien.ma_nv=dat_phong.ma_nv_nhan AND da_huy=0 AND ngay_checkin > ?";

        Connection con = DbConnection.getConnection();
        ResultSet rs;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setTimestamp(1, today);
            rs = stmt.executeQuery();

            while (rs.next()) {
                datPhong = new DatPhong(
                        rs.getInt(1),
                        rs.getTimestamp(2),
                        rs.getNString(3),
                        rs.getTimestamp(4),
                        rs.getTimestamp(5),
                        new KhachHang(rs.getInt(6), rs.getNString(7)),
                        new NhanVien(rs.getInt(8), rs.getNString(9)),
                        rs.getInt(10),
                        rs.getLong(11),
                        rs.getBoolean(12),
                        rs.getBoolean(13),
                        rs.getNString(14),
                        rs.getBoolean(15)
                );
                dsDatPhong.add(datPhong);
            }
            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException e) {
            ExHandler.handle(e);
        }

        return dsDatPhong;
    }

    public ObservableList<DatPhong> getChuaCheckin() {
        ObservableList<DatPhong> dsDatPhong = FXCollections.observableArrayList();
        DatPhong datPhong = null;

        String sql = "SELECT dat_phong.ma_dat_phong, ngay_dat, phuong_thuc, ngay_checkin, ngay_checkout, ma_kh_dat, ten_khach, ma_nv_nhan, ten_nv, so_nguoi, tien_dat_coc, la_khach_doan, da_huy, dat_phong.ghi_chu, da_xong " +
                "FROM dat_phong, khach_hang, nhan_vien, chi_tiet_dat_phong " +
                "WHERE dat_phong.ma_kh_dat=khach_hang.ma_kh AND nhan_vien.ma_nv=dat_phong.ma_nv_nhan AND chi_tiet_dat_phong.ma_dat_phong=dat_phong.ma_dat_phong AND da_huy=1 AND da_xong=0 AND chi_tiet_dat_phong.ngay_checkin_tt IS NULL " +
                "ORDER BY ngay_checkin ASC";

        Connection con = DbConnection.getConnection();
        ResultSet rs;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                datPhong = new DatPhong(
                        rs.getInt(1),
                        rs.getTimestamp(2),
                        rs.getNString(3),
                        rs.getTimestamp(4),
                        rs.getTimestamp(5),
                        new KhachHang(rs.getInt(6), rs.getNString(7)),
                        new NhanVien(rs.getInt(8), rs.getNString(9)),
                        rs.getInt(10),
                        rs.getLong(11),
                        rs.getBoolean(12),
                        rs.getBoolean(13),
                        rs.getNString(14),
                        rs.getBoolean(15)
                );
                dsDatPhong.add(datPhong);
            }
            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException e) {
            ExHandler.handle(e);
        }

        return dsDatPhong;
    }

    public DatPhong get(int maDatPhong) {
        DatPhong datPhong = null;

        String sql = "SELECT ma_dat_phong, ngay_dat, phuong_thuc, ngay_checkin, ngay_checkout, ma_kh_dat, ten_khach, ma_nv_nhan, ten_nv, so_nguoi, tien_dat_coc, la_khach_doan, da_huy, dat_phong.ghi_chu, da_xong " +
                "FROM dat_phong, khach_hang, nhan_vien " +
                "WHERE dat_phong.ma_kh_dat=khach_hang.ma_kh AND nhan_vien.ma_nv=dat_phong.ma_nv_nhan AND ma_dat_phong=?";

        Connection con = DbConnection.getConnection();
        ResultSet rs;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, maDatPhong);
            rs = stmt.executeQuery();

            while (rs.next()) {
                datPhong = new DatPhong(
                        rs.getInt(1),
                        rs.getTimestamp(2),
                        rs.getNString(3),
                        rs.getTimestamp(4),
                        rs.getTimestamp(5),
                        new KhachHang(rs.getInt(6), rs.getNString(7)),
                        new NhanVien(rs.getInt(8), rs.getNString(9)),
                        rs.getInt(10),
                        rs.getLong(11),
                        rs.getBoolean(12),
                        rs.getBoolean(13),
                        rs.getNString(14),
                        rs.getBoolean(15)
                );
            }
            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException e) {
            ExHandler.handle(e);
        }
        System.out.println("dat phong: " + datPhong);
        return datPhong;
    }

    public boolean update(DatPhong datPhong) {
        String sql = "UPDATE dat_phong " +
                "SET phuong_thuc=?, ngay_checkin=?, ngay_checkout=?, ma_kh_dat=?, so_nguoi=?, tien_dat_coc=?, la_khach_doan=?, da_huy=?, ghi_chu=?, da_xong=? " +
                "WHERE ma_dat_phong=?";

        Connection con = DbConnection.getConnection();

        boolean ketQua = false;
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setNString(1, datPhong.getPhuongThuc());
            stmt.setTimestamp(2, datPhong.getNgayCheckin());
            stmt.setTimestamp(3, datPhong.getNgayCheckout());
            stmt.setInt(4, datPhong.getKhachHang().getMaKh());
            stmt.setInt(5, datPhong.getSoNguoi());
            stmt.setLong(6, datPhong.getTienDatCoc());
            stmt.setBoolean(7, datPhong.isKhachDoan());
            stmt.setBoolean(8, datPhong.isDaHuy());
            stmt.setNString(9, datPhong.getGhiChu());
            stmt.setBoolean(10, datPhong.isDaXong());
            stmt.setInt(11, datPhong.getMaDatPhong());

            System.out.println();
            ketQua = (stmt.executeUpdate() > 0);

            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }
        return ketQua;
    }

    public boolean delete(DatPhong datPhong) {
        String sql = "DELETE FROM dat_phong WHERE ma_dat_phong=?";

        Connection con = DbConnection.getConnection();

        boolean ketQua = false;
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, datPhong.getMaDatPhong());

            ketQua = (stmt.executeUpdate() > 0);

            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }
        return ketQua;
    }
}