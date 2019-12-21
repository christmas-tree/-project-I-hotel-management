package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import util.DbConnection;
import util.ExHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class ChiTietDatPhongDAO {

    private static ChiTietDatPhongDAO instance = new ChiTietDatPhongDAO();

    public static ChiTietDatPhongDAO getInstance() {
        return instance;
    }

    public boolean create(ChiTietDatPhong chiTietDatPhong) {

        String sql = "INSERT INTO chi_tiet_dat_phong(ma_dat_phong, ma_phong, ngay_checkin_tt, ma_nv_le_tan, he_so_ngay_le, he_so_khuyen_mai, ghi_chu) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        boolean result = false;
        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, chiTietDatPhong.getDatPhong().getMaDatPhong());
            stmt.setInt(2, chiTietDatPhong.getPhong().getMaPhong());
            stmt.setTimestamp(3, chiTietDatPhong.getNgayCheckinTt());
            if (chiTietDatPhong.getNvLeTan() != null)
                stmt.setInt(4, chiTietDatPhong.getNvLeTan().getMaNv());
            else
                stmt.setNull(4, Types.INTEGER);
            stmt.setFloat(5, chiTietDatPhong.getHeSoNgayLe());
            stmt.setFloat(6, chiTietDatPhong.getHeSoKhuyenMai());
            stmt.setNString(7, chiTietDatPhong.getGhiChu());

            result = (stmt.executeUpdate() > 0);

            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }

        return result;
    }

    public boolean create(ObservableList<ChiTietDatPhong> dsChiTietDatPhong) {

        String sql = "INSERT INTO chi_tiet_dat_phong(ma_dat_phong, ma_phong, ngay_checkin_tt, ma_nv_le_tan, he_so_ngay_le, he_so_khuyen_mai, ghi_chu) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            ChiTietDatPhong chiTietDatPhong = null;

            for (int i = 0; i < dsChiTietDatPhong.size(); i++) {
                chiTietDatPhong = dsChiTietDatPhong.get(i);

                stmt.setInt(1, chiTietDatPhong.getDatPhong().getMaDatPhong());
                stmt.setInt(2, chiTietDatPhong.getPhong().getMaPhong());
                stmt.setTimestamp(3, chiTietDatPhong.getNgayCheckinTt());
                stmt.setInt(4, chiTietDatPhong.getNvLeTan().getMaNv());
                stmt.setFloat(5, chiTietDatPhong.getHeSoNgayLe());
                stmt.setFloat(6, chiTietDatPhong.getHeSoKhuyenMai());
                stmt.setNString(7, chiTietDatPhong.getGhiChu());

                stmt.executeUpdate();
            }
            stmt.close();
            con.close();

            return true;
        } catch (SQLException e) {
            ExHandler.handle(e);
            return false;
        }
    }

    public ObservableList<ChiTietDatPhong> getAll(DatPhong datPhong, ArrayList<Phong> dsPhong) {
        ObservableList<ChiTietDatPhong> dsChiTietDatPhong = FXCollections.observableArrayList();
        ChiTietDatPhong chiTietDatPhong = null;

        String sql = "SELECT ma_phong, ngay_checkin_tt, ngay_checkout_tt, ma_nv_le_tan, ten_nv, he_so_ngay_le, he_so_khuyen_mai, thanh_tien, chi_tiet_dat_phong.ghi_chu " +
                "FROM chi_tiet_dat_phong, nhan_vien WHERE chi_tiet_dat_phong.ma_nv_le_tan=nhan_vien.ma_nv AND ma_dat_phong=?";

        Connection con = DbConnection.getConnection();
        ResultSet rs;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, datPhong.getMaDatPhong());
            rs = stmt.executeQuery();

            while(rs.next()) {
                chiTietDatPhong = new ChiTietDatPhong(
                        datPhong,
                        getPhongFromArray(dsPhong, rs.getInt(1)),
                        rs.getTimestamp(2),
                        rs.getTimestamp(3),
                        new NhanVien(rs.getInt(4), rs.getNString(5)),
                        rs.getFloat(6),
                        rs.getFloat(7),
                        rs.getLong(8),
                        rs.getNString(9)
                );
                dsChiTietDatPhong.add(chiTietDatPhong);
            }
            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException e) {
            ExHandler.handle(e);
        }
        return dsChiTietDatPhong;
    }


    public ChiTietDatPhong get(DatPhong datPhong, ArrayList<Phong> dsPhong) {
        ObservableList<ChiTietDatPhong> dsChiTietDatPhong = FXCollections.observableArrayList();
        ChiTietDatPhong chiTietDatPhong = null;

        String sql = "SELECT ma_phong, ngay_checkin_tt, ngay_checkout_tt, ma_nv_le_tan, ten_nv, he_so_ngay_le, he_so_khuyen_mai, thanh_tien, chi_tiet_dat_phong.ghi_chu " +
                "FROM chi_tiet_dat_phong, nhan_vien WHERE chi_tiet_dat_phong.ma_nv_le_tan=nhan_vien.ma_nv AND ma_dat_phong=?";

        String sql2 = "SELECT khach_hang.ma_kh, ten_khach FROM ds_noi_o, khach_hang WHERE ds_noi_o.ma_kh=khach_hang.ma_kh AND ds_noi_o.ma_phong=? AND ds_noi_o.ma_dat_phong=?";

        Connection con = DbConnection.getConnection();
        ResultSet rs;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, datPhong.getMaDatPhong());
            rs = stmt.executeQuery();

            while(rs.next()) {
                chiTietDatPhong = new ChiTietDatPhong(
                        datPhong,
                        getPhongFromArray(dsPhong, rs.getInt(1)),
                        rs.getTimestamp(2),
                        rs.getTimestamp(3),
                        new NhanVien(rs.getInt(4), rs.getNString(5)),
                        rs.getFloat(6),
                        rs.getFloat(7),
                        rs.getLong(8),
                        rs.getNString(9)
                );
            }
            rs.close();
            stmt.close();

            ArrayList<KhachHang> dsKhachHang = new ArrayList<>();
            while (rs.next()) {
                KhachHang khachHang = new KhachHang(
                        rs.getInt(1),
                        rs.getNString(2)
                );
                dsKhachHang.add(khachHang);
            }
            rs.close();
            stmt.close();
            con.close();

            stmt = con.prepareStatement(sql2);
            stmt.setInt(1, chiTietDatPhong.getPhong().getMaPhong());
            stmt.setInt(2, chiTietDatPhong.getDatPhong().getMaDatPhong());

            chiTietDatPhong.setDsKhachHang(dsKhachHang);
            con.close();

        } catch (SQLException e) {
            ExHandler.handle(e);
        }
        return chiTietDatPhong;
    }

    public ChiTietDatPhong getByPhong(Phong phong) {
        ChiTietDatPhong chiTietDatPhong = null;

        String sql = "SELECT dat_phong.ma_dat_phong, ngay_checkin_tt, ngay_checkout_tt, ma_nv_le_tan, ten_nv, he_so_ngay_le, he_so_khuyen_mai, thanh_tien, dat_phong.ghi_chu " +
                "FROM chi_tiet_dat_phong, nhan_vien, dat_phong " +
                "WHERE chi_tiet_dat_phong.ma_nv_le_tan=nhan_vien.ma_nv AND dat_phong.ma_dat_phong=chi_tiet_dat_phong.ma_dat_phong AND ma_phong=? AND ngay_checkout_tt IS NULL AND ngay_checkin_tt IS NOT NULL AND da_huy=0";

        String sql2 = "SELECT khach_hang.ma_kh, ten_khach FROM ds_noi_o, khach_hang WHERE ds_noi_o.ma_kh=khach_hang.ma_kh AND ds_noi_o.ma_phong=? AND ds_noi_o.ma_dat_phong=?";
        Connection con = DbConnection.getConnection();
        ResultSet rs;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, phong.getMaPhong());
            rs = stmt.executeQuery();

            while (rs.next())
                chiTietDatPhong = new ChiTietDatPhong(
                        DatPhongDAO.getInstance().get(rs.getInt(1)),
                        phong,
                        rs.getTimestamp(2),
                        rs.getTimestamp(3),
                        new NhanVien(rs.getInt(4), rs.getNString(5)),
                        rs.getFloat(6),
                        rs.getFloat(7),
                        rs.getLong(8),
                        rs.getNString(9)
                );
            rs.close();
            stmt.close();

            stmt = con.prepareStatement(sql2);
            stmt.setInt(1, phong.getMaPhong());
            stmt.setInt(2, chiTietDatPhong.getDatPhong().getMaDatPhong());
            rs = stmt.executeQuery();

            ArrayList<KhachHang> dsKhachHang = new ArrayList<>();
            while (rs.next()) {
                KhachHang khachHang = new KhachHang(
                        rs.getInt(1),
                        rs.getNString(2)
                );
                dsKhachHang.add(khachHang);
            }

            rs.close();
            stmt.close();
            con.close();
            chiTietDatPhong.setDsKhachHang(dsKhachHang);
        } catch (SQLException e) {
            ExHandler.handle(e);
        }
        return chiTietDatPhong;
    }

    public boolean update(ObservableList<ChiTietDatPhong> dsChiTietDatPhong) {
        String sql = "UPDATE chi_tiet_dat_phong " +
                "SET ngay_checkin_tt=?, ngay_checkout_tt=?, ma_nv_le_tan=?, he_so_ngay_le=?, he_so_khuyen_mai=?, thanh_tien=?, ghi_chu=? " +
                "WHERE ma_dat_phong=? AND ma_phong=?";
        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            ChiTietDatPhong chiTietDatPhong = null;

            for (int i = 0; i < dsChiTietDatPhong.size(); i++) {
                chiTietDatPhong = dsChiTietDatPhong.get(i);

                stmt.setTimestamp(1, chiTietDatPhong.getNgayCheckinTt());
                stmt.setTimestamp(2, chiTietDatPhong.getNgayCheckoutTt());
                stmt.setInt(3, chiTietDatPhong.getNvLeTan().getMaNv());
                stmt.setFloat(4, chiTietDatPhong.getHeSoNgayLe());
                stmt.setFloat(5, chiTietDatPhong.getHeSoKhuyenMai());
                stmt.setLong(6, chiTietDatPhong.getThanhTien());
                stmt.setNString(7, chiTietDatPhong.getGhiChu());
                stmt.setInt(8, chiTietDatPhong.getDatPhong().getMaDatPhong());
                stmt.setInt(9, chiTietDatPhong.getPhong().getMaPhong());

                stmt.executeUpdate();
            }
            stmt.close();
            con.close();
            return true;
        } catch (SQLException e) {
            ExHandler.handle(e);
            return false;
        }
    }

    public boolean update(ChiTietDatPhong chiTietDatPhong1) {
        String sql = "UPDATE chi_tiet_dat_phong " +
                "SET ngay_checkin_tt=?, ngay_checkout_tt=?, ma_nv_le_tan=?, he_so_ngay_le=?, he_so_khuyen_mai=?, thanh_tien=?, ghi_chu=? " +
                "WHERE ma_dat_phong=? AND ma_phong=?";

        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setTimestamp(1, chiTietDatPhong1.getNgayCheckinTt());
            stmt.setTimestamp(2, chiTietDatPhong1.getNgayCheckoutTt());
            stmt.setInt(3, chiTietDatPhong1.getNvLeTan().getMaNv());
            stmt.setFloat(4, chiTietDatPhong1.getHeSoNgayLe());
            stmt.setFloat(5, chiTietDatPhong1.getHeSoKhuyenMai());
            stmt.setLong(6, chiTietDatPhong1.getThanhTien());
            stmt.setNString(7, chiTietDatPhong1.getGhiChu());
            stmt.setInt(8, chiTietDatPhong1.getDatPhong().getMaDatPhong());
            stmt.setInt(9, chiTietDatPhong1.getPhong().getMaPhong());

            stmt.executeUpdate();
            stmt.close();
            con.close();

            return true;
        } catch (SQLException e) {
            ExHandler.handle(e);
            return false;
        }
    }

    public boolean delete(ArrayList<ChiTietDatPhong> dsChiTietDatPhongXoa) {
        String sql = "DELETE FROM chi_tiet_dat_phong WHERE ma_dat_phong=? AND ma_phong=?";

        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            ChiTietDatPhong chiTietDatPhong = null;

            for (int i = 0; i < dsChiTietDatPhongXoa.size(); i++) {
                chiTietDatPhong = dsChiTietDatPhongXoa.get(i);

                stmt.setInt(1, chiTietDatPhong.getDatPhong().getMaDatPhong());
                stmt.setInt(2, chiTietDatPhong.getPhong().getMaPhong());

                stmt.executeUpdate();
            }
            stmt.close();
            con.close();

            return true;
        } catch (SQLException e) {
            ExHandler.handle(e);
            return false;
        }
    }
    
    public Phong getPhongFromArray(ArrayList<Phong> dsPhong, int maPhong) {
        for (int i=0; i < dsPhong.size(); i++) {
            if (dsPhong.get(i).getMaPhong() == maPhong)
                return dsPhong.get(i);
        }
        return null;
    }

    public boolean updateDsKhachO(ChiTietDatPhong chiTietDatPhong, ObservableList<KhachHang> dsKhachHang, ArrayList<KhachHang> dsXoa) {

        String sql = "IF NOT EXISTS (SELECT * FROM [ds_noi_o] WHERE ma_phong=? AND ma_dat_phong=? AND ma_kh=?) " +
                "INSERT INTO ds_noi_o(ma_dat_phong, ma_phong, ma_kh) VALUES (?, ?, ?) ";

        String sql2 = "DELETE FROM ds_noi_o WHERE ma_phong=? AND ma_dat_phong=? AND ma_kh=?";

        Connection con = DbConnection.getConnection();
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            int maPhong = chiTietDatPhong.getPhong().getMaPhong();
            int maDatPhong = chiTietDatPhong.getDatPhong().getMaDatPhong();
            KhachHang khachHang = null;
            for (int i = 0; i < dsKhachHang.size(); i++) {
                khachHang = dsKhachHang.get(i);

                stmt.setInt(1, maPhong);
                stmt.setInt(2, maDatPhong);
                stmt.setInt(3, khachHang.getMaKh());
                stmt.setInt(4, maDatPhong);
                stmt.setInt(5, maPhong);
                stmt.setInt(6, khachHang.getMaKh());

                stmt.executeUpdate();
            }
            stmt.close();

            stmt = con.prepareStatement(sql2);
            for (int i = 0; i < dsXoa.size(); i++) {
                khachHang = dsXoa.get(i);

                stmt.setInt(1, maPhong);
                stmt.setInt(2, maDatPhong);
                stmt.setInt(3, khachHang.getMaKh());

                stmt.executeUpdate();
            }
            con.close();
            return true;
        } catch (SQLException e) {
            ExHandler.handle(e);
            return false;
        }
    }
    public Phong timPhong(int maPhong, ObservableList<Phong> dsPhong) {
        for (Phong phong: dsPhong) {
            if (maPhong == phong.getMaPhong())
                return phong;
        }
        return null;
    }
}

