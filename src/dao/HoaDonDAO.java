package dao;

import model.HoaDon;
import util.DbConnection;
import util.ExHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HoaDonDAO {
    private static HoaDonDAO instance = new HoaDonDAO();

    public static HoaDonDAO getInstance() {
        return instance;
    }

    public boolean create(HoaDon hoaDon) {

        String sql = "INSERT INTO hoa_don(ma_dat_phong, ten_muc, don_gia, so_luong, don_vi, thanh_tien) VALUES (?, ?, ?, ?, ?, ?, ?)";
        boolean result = false;

        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, hoaDon.getMaDatPhong());
            stmt.setNString(2, hoaDon.getTenMuc());
            stmt.setLong(3, hoaDon.getDonGia());
            stmt.setLong(4, hoaDon.getSoLuong());
            stmt.setNString(5, hoaDon.getDonVi());
            stmt.setLong(6, hoaDon.getThanhTien());

            result = (stmt.executeUpdate() > 0);

            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }

        return result;
    }

    public void 
}
