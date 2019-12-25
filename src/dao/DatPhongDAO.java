package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.DatPhong;
import model.KhachHang;
import model.NhanVien;
import util.DbConnection;
import util.ExceptionHandler;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class DatPhongDAO {
    private static DatPhongDAO instance = new DatPhongDAO();

    public static DatPhongDAO getInstance() {
        return instance;
    }

    public boolean create(DatPhong datPhong) {
        String sql = "INSERT INTO dat_phong(ngay_dat, phuong_thuc, khach_doan, tien_dat_coc, ngay_checkin, ngay_checkout, ma_kh_dat, ma_nv_nhan," +
                "ngay_checkin_tt, ngay_checkout_tt, ma_nv_le_tan, da_huy, ghi_chu) " +
                "OUTPUT INSERTED.ma_dat_phong " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setTimestamp(1, datPhong.getNgayDat());
            stmt.setNString(2, datPhong.getPhuongThuc());
            stmt.setBoolean(3, datPhong.isKhachDoan());
            stmt.setLong(4, datPhong.getTienDatCoc());
            stmt.setTimestamp(5, datPhong.getNgayCheckinDk());
            stmt.setTimestamp(6, datPhong.getNgayCheckoutDk());
            stmt.setInt(7, datPhong.getKhachHang().getMaKh());
            stmt.setInt(8, datPhong.getNvNhanDat().getMaNv());
            stmt.setTimestamp(9, datPhong.getNgayCheckinTt());
            stmt.setTimestamp(10, datPhong.getNgayCheckoutTt());
            if (datPhong.getNvLeTan() == null)
                stmt.setNull(11, Types.INTEGER);
            else
                stmt.setInt(11, datPhong.getNvLeTan().getMaNv());
            stmt.setBoolean(12, datPhong.isDaHuy());
            stmt.setNString(13, datPhong.getGhiChu());

            ResultSet rs = stmt.executeQuery();

            if (rs.next())
                datPhong.setMaDatPhong(rs.getInt(1));
            else
                datPhong.setMaDatPhong(0);

            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExceptionHandler.handle(e);
        }

        return (datPhong.getMaDatPhong() != 0);
    }

    public ArrayList<DatPhong> getAllActiveBooking() {

        ArrayList<DatPhong> dsDatPhong = new ArrayList<>();
        DatPhong datPhong = null;

        String sql = "SELECT ma_dat_phong, ngay_dat, phuong_thuc, khach_doan, tien_dat_coc, ngay_checkin, ngay_checkout, ma_kh_dat, ten_khach, ma_nv_nhan, nv1.ten_nv, ngay_checkin_tt, ngay_checkout_tt, ma_nv_le_tan, nv2.ten_nv, da_huy, dp.ghi_chu\n" +
                "FROM dat_phong dp\n" +
                "JOIN khach_hang kh ON dp.ma_kh_dat=kh.ma_kh\n" +
                "JOIN nhan_vien nv1 ON nv1.ma_nv=dp.ma_nv_nhan\n" +
                "LEFT JOIN nhan_vien nv2 ON nv2.ma_nv=dp.ma_nv_le_tan\n" +
                "WHERE da_huy=0 AND ngay_checkin_tt IS NULL AND ngay_checkin > ?";

        Connection con = DbConnection.getConnection();
        ResultSet rs;

        try {
            Timestamp today = Timestamp.valueOf(LocalDate.now().plusDays(-3).atStartOfDay());
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setTimestamp(1, today);
            rs = stmt.executeQuery();

            while (rs.next()) {
                datPhong = new DatPhong(
                        rs.getInt(1),
                        rs.getTimestamp(2),
                        rs.getNString(3),
                        rs.getBoolean(4),
                        rs.getLong(5),
                        rs.getTimestamp(6),
                        rs.getTimestamp(7),
                        new KhachHang(rs.getInt(8), rs.getNString(9)),
                        (rs.getNString(11) == null) ? null : new NhanVien(rs.getInt(10), rs.getNString(11)),
                        rs.getTimestamp(12),
                        rs.getTimestamp(13),
                        (rs.getNString(15) == null) ? null : new NhanVien(rs.getInt(14), rs.getNString(15)),
                        rs.getBoolean(16),
                        rs.getNString(17)
                );
                dsDatPhong.add(datPhong);
            }
            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException e) {
            ExceptionHandler.handle(e);
        }

        return dsDatPhong;
    }

    public ArrayList<DatPhong> getAllRunning() {

        ArrayList<DatPhong> dsDatPhong = new ArrayList<>();
        DatPhong datPhong = null;

        String sql = "SELECT ma_dat_phong, ngay_dat, phuong_thuc, khach_doan, tien_dat_coc, ngay_checkin, ngay_checkout, ma_kh_dat, ten_khach, ma_nv_nhan, nv1.ten_nv, ngay_checkin_tt, ngay_checkout_tt, ma_nv_le_tan, nv2.ten_nv, da_huy, dp.ghi_chu\n" +
                "FROM dat_phong dp\n" +
                "JOIN khach_hang kh ON dp.ma_kh_dat=kh.ma_kh\n" +
                "JOIN nhan_vien nv1 ON nv1.ma_nv=dp.ma_nv_nhan\n" +
                "LEFT JOIN nhan_vien nv2 ON nv2.ma_nv=dp.ma_nv_le_tan\n" +
                "WHERE da_huy=0 AND ngay_checkin_tt IS NOT NULL AND ngay_checkout_tt IS NULL";

        Connection con = DbConnection.getConnection();
        ResultSet rs;

        try {
            Timestamp today = Timestamp.valueOf(LocalDate.now().plusDays(-3).atStartOfDay());
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setTimestamp(1, today);
            rs = stmt.executeQuery();

            while (rs.next()) {
                datPhong = new DatPhong(
                        rs.getInt(1),
                        rs.getTimestamp(2),
                        rs.getNString(3),
                        rs.getBoolean(4),
                        rs.getLong(5),
                        rs.getTimestamp(6),
                        rs.getTimestamp(7),
                        new KhachHang(rs.getInt(8), rs.getNString(9)),
                        (rs.getNString(11) == null) ? null : new NhanVien(rs.getInt(10), rs.getNString(11)),
                        rs.getTimestamp(12),
                        rs.getTimestamp(13),
                        (rs.getNString(15) == null) ? null : new NhanVien(rs.getInt(14), rs.getNString(15)),
                        rs.getBoolean(16),
                        rs.getNString(17)
                );
                dsDatPhong.add(datPhong);
            }
            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException e) {
            ExceptionHandler.handle(e);
        }

        return dsDatPhong;
    }

    public DatPhong get(int maDatPhong) {
        DatPhong datPhong = null;

        String sql = "SELECT ma_dat_phong, ngay_dat, phuong_thuc, khach_doan, tien_dat_coc, ngay_checkin, ngay_checkout, ma_kh_dat, ten_khach, ma_nv_nhan, nv1.ten_nv, ngay_checkin_tt, ngay_checkout_tt, ma_nv_le_tan, nv2.ten_nv, da_huy, dp.ghi_chu " +
                "FROM dat_phong dp, khach_hang kh, nhan_vien nv1, nhan_vien nv2 " +
                "WHERE dp.ma_kh_dat=kh.ma_kh AND nv1.ma_nv=dp.ma_nv_nhan AND nv2.ma_nv=dp.ma_nv_le_tan AND dp.ma_dat_phong=?";

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
                        rs.getBoolean(4),
                        rs.getLong(5),
                        rs.getTimestamp(6),
                        rs.getTimestamp(7),
                        new KhachHang(rs.getInt(8), rs.getNString(9)),
                        new NhanVien(rs.getInt(10), rs.getNString(11)),
                        rs.getTimestamp(12),
                        rs.getTimestamp(13),
                        new NhanVien(rs.getInt(14), rs.getNString(15)),
                        rs.getBoolean(16),
                        rs.getNString(17)
                );
            }
            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException e) {
            ExceptionHandler.handle(e);
        }
        return datPhong;
    }

    public boolean update(DatPhong datPhong) {
        String sql = "UPDATE dat_phong " +
                "SET phuong_thuc=?, tien_dat_coc=?, ngay_checkin=?, ngay_checkout=?, ma_kh_dat=?, ma_nv_nhan=?, ngay_checkin_tt=?, ngay_checkout_tt=?, ma_nv_le_tan=?, da_huy=?, ghi_chu=? " +
                "WHERE ma_dat_phong=?";

        Connection con = DbConnection.getConnection();

        boolean ketQua = false;
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setNString(1, datPhong.getPhuongThuc());
            stmt.setLong(2, datPhong.getTienDatCoc());
            stmt.setTimestamp(3, datPhong.getNgayCheckinDk());
            stmt.setTimestamp(4, datPhong.getNgayCheckoutDk());
            stmt.setInt(5, datPhong.getKhachHang().getMaKh());
            stmt.setInt(6, datPhong.getNvNhanDat().getMaNv());
            stmt.setTimestamp(7, datPhong.getNgayCheckinTt());
            stmt.setTimestamp(8, datPhong.getNgayCheckoutTt());
            if (datPhong.getNvLeTan() == null)
                stmt.setNull(9, Types.INTEGER);
            else
                stmt.setInt(9, datPhong.getNvLeTan().getMaNv());
            stmt.setBoolean(10, datPhong.isDaHuy());
            stmt.setNString(11, datPhong.getGhiChu());
            stmt.setInt(12, datPhong.getMaDatPhong());

            ketQua = (stmt.executeUpdate() > 0);

            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExceptionHandler.handle(e);
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
            ExceptionHandler.handle(e);
        }
        return ketQua;
    }

    public ObservableList<DatPhong> getAllReceipts() {
        ObservableList<DatPhong> dsDatPhong = FXCollections.observableArrayList();
        DatPhong datPhong = null;

        String sql = "SELECT dp.ma_dat_phong, khach_doan, ma_kh_dat, kh.ten_khach, kh.cmnd, kh.dien_thoai, ngay_checkin_tt, ngay_checkout_tt, SUM(thanh_tien), dp.ghi_chu " +
                "FROM dat_phong dp, khach_hang kh, hoa_don hd " +
                "WHERE dp.ma_kh_dat=kh.ma_kh AND dp.ma_dat_phong=hd.ma_dat_phong " +
                "GROUP BY dp.ma_dat_phong, khach_doan, ma_kh_dat, kh.ten_khach, kh.cmnd, kh.dien_thoai, ngay_checkin_tt, ngay_checkout_tt, dp.ghi_chu";

        Connection con = DbConnection.getConnection();
        ResultSet rs;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                datPhong = new DatPhong(
                        rs.getInt(1),
                        rs.getBoolean(2),
                        new KhachHang(rs.getInt(3), rs.getNString(4), rs.getLong(5), rs.getLong(6)),
                        rs.getTimestamp(7),
                        rs.getTimestamp(8),
                        rs.getLong(9),
                        rs.getNString(10)
                );
                dsDatPhong.add(datPhong);
            }
            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException e) {
            ExceptionHandler.handle(e);
        }
        return dsDatPhong;
    }

    public ObservableList<DatPhong> searchReceipts(int searchType, String value) {
        ObservableList<DatPhong> dsDatPhong = FXCollections.observableArrayList();
        DatPhong datPhong = null;

        Connection con = DbConnection.getConnection();
        ResultSet rs;
        String sql_var = "";

        switch (searchType) {
//            String searchChoices[] = {"Mã đặt phòng", "Tên khách", "CMND/CCCD", "Điện thoại", "Ngày đến", "Ngày đi"};
            //                              0             1              2             3         4            5
            case 0:
                sql_var += "dp.ma_dat_phong=" + value;
                break;
            case 1:
                sql_var += "kh.ten_khach LIKE N'%" + value + "%'";
                break;
            case 2:
                sql_var += "CAST(kh.cmnd AS VARCHAR) LIKE '%" + value + "%'";
                break;
            case 3:
                sql_var += "CAST(kh.dien_thoai AS VARCHAR) LIKE '%" + value + "%'";
                break;
            default:
                ExceptionHandler.handle(new Exception("Illegal Searching Method"));
        }

        try {
            String sql = "SELECT dp.ma_dat_phong, khach_doan, ma_kh_dat, kh.ten_khach, kh.cmnd, kh.dien_thoai, ngay_checkin_tt, ngay_checkout_tt, SUM(thanh_tien), dp.ghi_chu " +
                    "FROM dat_phong dp, khach_hang kh, hoa_don hd " +
                    "WHERE dp.ma_kh_dat=kh.ma_kh AND dp.ma_dat_phong=hd.ma_dat_phong AND " + sql_var + " " +
                    "GROUP BY dp.ma_dat_phong, khach_doan, ma_kh_dat, kh.ten_khach, kh.cmnd, kh.dien_thoai, ngay_checkin_tt, ngay_checkout_tt, dp.ghi_chu";
            PreparedStatement stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                datPhong = new DatPhong(
                        rs.getInt(1),
                        rs.getBoolean(2),
                        new KhachHang(rs.getInt(3), rs.getNString(4), rs.getLong(5), rs.getLong(6)),
                        rs.getTimestamp(7),
                        rs.getTimestamp(8),
                        rs.getLong(9),
                        rs.getNString(10)
                );
                dsDatPhong.add(datPhong);
            }
            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException e) {
            ExceptionHandler.handle(e);
        }

        return dsDatPhong;
    }

    public ObservableList<DatPhong> searchReceipts(int searchType, LocalDate value1, LocalDate value2) {
        ObservableList<DatPhong> dsDatPhong = FXCollections.observableArrayList();
        DatPhong datPhong = null;
        String sql_var = "";

        Connection con = DbConnection.getConnection();
        ResultSet rs;
        try {
            switch (searchType) {
//            String searchChoices[] = {"Mã đặt phòng", "Tên khách", "CMND/CCCD", "Điện thoại", "Ngày đến", "Ngày đi"};
                //                              0             1              2             3         4            5
                case 4:
                    sql_var += "dp.ngay_checkin_tt BETWEEN ? AND ?";
                    break;
                case 5:
                    sql_var += "dp.ngay_checkout_tt BETWEEN ? AND ?";
                    break;
                default:
                    ExceptionHandler.handle(new Exception("Illegal Searching Method"));
            }

            String sql = "SELECT dp.ma_dat_phong, khach_doan, ma_kh_dat, kh.ten_khach, kh.cmnd, kh.dien_thoai, ngay_checkin_tt, ngay_checkout_tt, SUM(thanh_tien), dp.ghi_chu " +
                    "FROM dat_phong dp, khach_hang kh, hoa_don hd " +
                    "WHERE dp.ma_kh_dat=kh.ma_kh AND dp.ma_dat_phong=hd.ma_dat_phong AND " + sql_var + " " +
                    "GROUP BY dp.ma_dat_phong, khach_doan, ma_kh_dat, kh.ten_khach, kh.cmnd, kh.dien_thoai, ngay_checkin_tt, ngay_checkout_tt, dp.ghi_chu";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(value1));
            stmt.setDate(2, Date.valueOf(value2));
            rs = stmt.executeQuery();

            while (rs.next()) {
                datPhong = new DatPhong(
                        rs.getInt(1),
                        rs.getBoolean(2),
                        new KhachHang(rs.getInt(3), rs.getNString(4), rs.getLong(5), rs.getLong(6)),
                        rs.getTimestamp(7),
                        rs.getTimestamp(8),
                        rs.getLong(9),
                        rs.getNString(10)
                );
                dsDatPhong.add(datPhong);
            }
            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException e) {
            ExceptionHandler.handle(e);
        }

        return dsDatPhong;
    }
}