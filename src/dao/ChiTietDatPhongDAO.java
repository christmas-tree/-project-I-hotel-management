package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import util.DbConnection;
import util.ExceptionHandler;

import java.sql.*;
import java.util.ArrayList;

public class ChiTietDatPhongDAO {

    private static ChiTietDatPhongDAO instance = new ChiTietDatPhongDAO();

    public static ChiTietDatPhongDAO getInstance() {
        return instance;
    }

    public boolean create(ChiTietDatPhong chiTietDatPhong) {

        String sql = "INSERT INTO chi_tiet_dat_phong(ma_dat_phong, ma_phong) " +
                "VALUES (?, ?)";
        boolean result = false;
        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, chiTietDatPhong.getDatPhong().getMaDatPhong());
            stmt.setInt(2, chiTietDatPhong.getPhong().getMaPhong());

            result = (stmt.executeUpdate() > 0);

            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExceptionHandler.handle(e);
        }

        return result;
    }

    public boolean create(ObservableList<ChiTietDatPhong> dsChiTietDatPhong) {

        String sql = "INSERT INTO chi_tiet_dat_phong(ma_dat_phong, ma_phong) " +
                "VALUES (?, ?)";
        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            ChiTietDatPhong chiTietDatPhong = null;

            for (int i = 0; i < dsChiTietDatPhong.size(); i++) {
                chiTietDatPhong = dsChiTietDatPhong.get(i);
                stmt.setInt(1, chiTietDatPhong.getDatPhong().getMaDatPhong());
                stmt.setInt(2, chiTietDatPhong.getPhong().getMaPhong());

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

    public ObservableList<ChiTietDatPhong> getAll(DatPhong datPhong, ArrayList<Phong> dsPhong) {
        ObservableList<ChiTietDatPhong> dsChiTietDatPhong = FXCollections.observableArrayList();
        ChiTietDatPhong chiTietDatPhong = null;

        String sql = "SELECT ma_phong FROM chi_tiet_dat_phong WHERE ma_dat_phong=?";

        Connection con = DbConnection.getConnection();
        ResultSet rs;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, datPhong.getMaDatPhong());
            rs = stmt.executeQuery();

            while (rs.next()) {
                chiTietDatPhong = new ChiTietDatPhong(
                        datPhong,
                        getPhongFromArray(dsPhong, rs.getInt(1))
                );
                dsChiTietDatPhong.add(chiTietDatPhong);
            }
            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException e) {
            ExceptionHandler.handle(e);
        }
        return dsChiTietDatPhong;
    }


    public ChiTietDatPhong get(DatPhong datPhong, ArrayList<Phong> dsPhong) {
        ChiTietDatPhong chiTietDatPhong = null;

        String sql = "SELECT ma_phong FROM chi_tiet_dat_phong WHERE ma_dat_phong=?";

        String sql2 = "SELECT khach_hang.ma_kh, ten_khach FROM ds_noi_o, khach_hang WHERE ds_noi_o.ma_kh=khach_hang.ma_kh AND ds_noi_o.ma_phong=? AND ds_noi_o.ma_dat_phong=?";

        Connection con = DbConnection.getConnection();
        ResultSet rs;
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, datPhong.getMaDatPhong());
            rs = stmt.executeQuery();
            while (rs.next()) {
                chiTietDatPhong = new ChiTietDatPhong(
                        datPhong,
                        getPhongFromArray(dsPhong, rs.getInt(1))
                );
            }
            rs.close();
            stmt.close();

            ArrayList<KhachHang> dsKhachHang = new ArrayList<>();
            stmt = con.prepareStatement(sql2);
            stmt.setInt(1, chiTietDatPhong.getPhong().getMaPhong());
            stmt.setInt(2, chiTietDatPhong.getDatPhong().getMaDatPhong());
            rs = stmt.executeQuery();
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
            ExceptionHandler.handle(e);
        }
        return chiTietDatPhong;
    }

    public ChiTietDatPhong getByPhong(Phong phong) {
        ChiTietDatPhong chiTietDatPhong = null;

        String sql = "SELECT dat_phong.ma_dat_phong FROM chi_tiet_dat_phong, nhan_vien, dat_phong " +
                "WHERE dat_phong.ma_dat_phong=chi_tiet_dat_phong.ma_dat_phong AND ma_phong=? AND ngay_checkout_tt IS NULL AND ngay_checkin_tt IS NOT NULL";

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
                        phong
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
            ExceptionHandler.handle(e);
        }
        return chiTietDatPhong;
    }

    public ArrayList<ChiTietDatPhong> getAllActive(ArrayList<Phong> dsPhong) {

        ArrayList<DatPhong> dsDatPhongActive = DatPhongDAO.getInstance().getAllActiveBooking();
        ArrayList<ChiTietDatPhong> dsChiTietDatPhong = new ArrayList<>();
        ChiTietDatPhong chiTietDatPhong = null;

        String sql = "SELECT ma_phong FROM chi_tiet_dat_phong ct, dat_phong dp " +
                "WHERE dp.ma_dat_phong=ct.ma_dat_phong AND ct.ma_dat_phong=?";

        Connection con = DbConnection.getConnection();
        ResultSet rs;
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            for (DatPhong datPhong : dsDatPhongActive) {
                stmt.setInt(1, datPhong.getMaDatPhong());
                rs = stmt.executeQuery();

                while (rs.next()) {
                    chiTietDatPhong = new ChiTietDatPhong(
                            datPhong,
                            getPhongFromArray(dsPhong, rs.getInt(1))
                    );
                    dsChiTietDatPhong.add(chiTietDatPhong);
                }
                rs.close();
            }
            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExceptionHandler.handle(e);
        }
        return dsChiTietDatPhong;
    }

//    public boolean update(ObservableList<ChiTietDatPhong> dsChiTietDatPhong) {
//        String sql = "UPDATE chi_tiet_dat_phong " +
//                "SET ma_phong=? " +
//                "WHERE ma_dat_phong=? AND ma_phong=?";
//        Connection con = DbConnection.getConnection();
//
//        try {
//            PreparedStatement stmt = con.prepareStatement(sql);
//            ChiTietDatPhong chiTietDatPhong = null;
//
//            for (int i = 0; i < dsChiTietDatPhong.size(); i++) {
//                chiTietDatPhong = dsChiTietDatPhong.get(i);
//
//                stmt.setInt(1, chiTietDatPhong.getPhong().getMaPhong());
//                stmt.setInt(2, chiTietDatPhong.getDatPhong().getMaDatPhong());
//                stmt.setInt(3, chiTietDatPhong.getPhong().getMaPhong());
//
//                stmt.executeUpdate();
//            }
//            stmt.close();
//            con.close();
//            return true;
//        } catch (SQLException e) {
//            ExHandler.handle(e);
//            return false;
//        }
//    }

    public boolean update(ObservableList<ChiTietDatPhong> dsChiTietDatPhong, ArrayList<ChiTietDatPhong> dsChiTietDatPhongXoa) {
        String sql = "IF NOT EXISTS (SELECT * FROM chi_tiet_dat_phong WHERE ma_phong=? AND ma_dat_phong=?) " +
                "INSERT INTO chi_tiet_dat_phong(ma_phong, ma_dat_phong) VALUES (?, ?)";

        String sql2 = "DELETE FROM chi_tiet_dat_phong WHERE ma_dat_phong=? AND ma_phong=?";

        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            for (ChiTietDatPhong chiTietDatPhong: dsChiTietDatPhong) {
                stmt.setInt(1, chiTietDatPhong.getPhong().getMaPhong());
                stmt.setInt(2, chiTietDatPhong.getDatPhong().getMaDatPhong());
                stmt.setInt(3, chiTietDatPhong.getPhong().getMaPhong());
                stmt.setInt(4, chiTietDatPhong.getDatPhong().getMaDatPhong());
                stmt.executeUpdate();
            }
            stmt.close();

            stmt = con.prepareStatement(sql2);
            for (ChiTietDatPhong chiTietDatPhong: dsChiTietDatPhongXoa) {
                stmt.setInt(1, chiTietDatPhong.getDatPhong().getMaDatPhong());
                stmt.setInt(2, chiTietDatPhong.getPhong().getMaPhong());
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

//    public boolean delete(ArrayList<ChiTietDatPhong> dsChiTietDatPhongXoa) {
//        String sql = "DELETE FROM chi_tiet_dat_phong WHERE ma_dat_phong=? AND ma_phong=?";
//
//        Connection con = DbConnection.getConnection();
//
//        try {
//            PreparedStatement stmt = con.prepareStatement(sql);
//            ChiTietDatPhong chiTietDatPhong = null;
//
//            for (int i = 0; i < dsChiTietDatPhongXoa.size(); i++) {
//                chiTietDatPhong = dsChiTietDatPhongXoa.get(i);
//
//                stmt.setInt(1, chiTietDatPhong.getDatPhong().getMaDatPhong());
//                stmt.setInt(2, chiTietDatPhong.getPhong().getMaPhong());
//
//                stmt.executeUpdate();
//            }
//            stmt.close();
//            con.close();
//
//            return true;
//        } catch (SQLException e) {
//            ExHandler.handle(e);
//            return false;
//        }
//    }

    private Phong getPhongFromArray(ArrayList<Phong> dsPhong, int maPhong) {
        for (int i = 0; i < dsPhong.size(); i++) {
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
            ExceptionHandler.handle(e);
            return false;
        }
    }
}

