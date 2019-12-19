package dao;

import model.DatPhong;
import model.KhachHang;
import model.NhanVien;
import model.Phong;
import util.DbConnection;
import util.ExHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    public ArrayList<DatPhong> getAll() {
        ArrayList<DatPhong> dsDatPhong = new ArrayList<>();
        DatPhong datPhong = null;

        String sql = "SELECT ma_dat_phong, ngay_dat, phuong_thuc, ngay_checkin, ngay_checkout, ma_kh_dat, ten_khach, ma_nv_nhan, ten_nv, so_nguoi, tien_dat_coc, la_khach_doan, da_huy, ghi_chu " +
                "FROM dat_phong, khach_hang, nhan_vien " +
                "WHERE dat_phong.ma_kh_dat=khach_hang.ma_kh AND nhan_vien.ma_nv=dat_phong.ma_nv_nhan";

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
                        rs.getNString(14)
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

        String sql = "SELECT ma_dat_phong, ngay_dat, phuong_thuc, ngay_checkin, ngay_checkout, ma_kh_dat, ten_khach, ma_nv_nhan, ten_nv, so_nguoi, tien_dat_coc, la_khach_doan, da_huy, ghi_chu " +
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
                        rs.getNString(14)
                );
            }
            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException e) {
            ExHandler.handle(e);
        }

        return datPhong;
    }

    public boolean update(DatPhong datPhong) {
        String sql = "UPDATE dat_phong " +
                "SET phuong_thuc=?, ngay_checkin=?, ngay_checkout=?, ma_kh_dat=?, so_nguoi=?, tien_dat_coc=?, la_khach_doan=?, da_huy=?, ghi_chu=? " +
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
            stmt.setInt(10, datPhong.getMaDatPhong());

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