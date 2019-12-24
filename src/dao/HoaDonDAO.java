package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.DatPhong;
import model.HoaDon;
import util.DbConnection;
import util.ExceptionHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HoaDonDAO {
    private static HoaDonDAO instance = new HoaDonDAO();

    public static HoaDonDAO getInstance() {
        return instance;
    }

    public boolean create(ArrayList<HoaDon> dsCtHoaDon) {
        String sql = "INSERT INTO hoa_don(ma_dat_phong, ten_muc, don_gia, so_luong, don_vi, thanh_tien) VALUES (?, ?, ?, ?, ?, ?)";
        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            for (HoaDon hoaDon : dsCtHoaDon) {
                stmt.setInt(1, hoaDon.getMaDatPhong());
                stmt.setNString(2, hoaDon.getTenMuc());
                stmt.setLong(3, hoaDon.getDonGia());
                stmt.setLong(4, hoaDon.getSoLuong());
                stmt.setNString(5, hoaDon.getDonVi());
                stmt.setLong(6, hoaDon.getThanhTien());

                stmt.executeUpdate();
            }
            stmt.close();
            con.close();
            return true;
        } catch (SQLException e) {
            ExceptionHandler.handle(e);
        }
        return false;
    }

    public ObservableList<HoaDon> get(DatPhong datphong) {
        String sql = "SELECT ma_ct_hoa_don, ma_dat_phong, ten_muc, don_gia, so_luong, don_vi, thanh_tien FROM hoa_don WHERE ma_dat_phong=?";
        Connection con = DbConnection.getConnection();
        ObservableList<HoaDon> dsCtHoaDon = FXCollections.observableArrayList();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, datphong.getMaDatPhong());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                HoaDon hoaDon = new HoaDon(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getNString(3),
                        rs.getLong(4),
                        rs.getInt(5),
                        rs.getNString(6),
                        rs.getLong(7));
                dsCtHoaDon.add(hoaDon);
            }
            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExceptionHandler.handle(e);
        }
        return dsCtHoaDon;
    }
}
