package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.BoiThuong;
import model.ChiTietDatPhong;
import util.DbConnection;
import util.ExHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BoiThuongDAO {

    private static BoiThuongDAO instance = new BoiThuongDAO();

    public static BoiThuongDAO getInstance() {return instance;}

    public boolean create(ObservableList<BoiThuong> dsBoiThuong) {
        String sql = "INSERT INTO boi_thuong(ma_phong, ma_dat_phong, ten_do, so_luong, trang_thai, boi_thuong, ghi_chu) VALUES (?, ?, ?, ?, ?, ?, ?)";
        boolean result = false;
        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            BoiThuong boiThuong = null;

            for (int i = 0; i < dsBoiThuong.size(); i++) {
                boiThuong = dsBoiThuong.get(i);
                stmt.setInt(1, boiThuong.getChiTietDatPhong().getPhong().getMaPhong());
                stmt.setInt(2, boiThuong.getChiTietDatPhong().getDatPhong().getMaDatPhong());
                stmt.setNString(3, boiThuong.getChiTietPhong().getTenDo());
                stmt.setInt(4, boiThuong.getSoLuong());
                stmt.setInt(5, boiThuong.getTrangThai());
                stmt.setLong(6, boiThuong.getBoiThuong());
                stmt.setNString(7, boiThuong.getGhiChu());

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

//    public ObservableList<ChiTietDichVu> getAll(ChiTietDatPhong chiTietDatPhong) {
//        ArrayList<DichVu> dsDichVu = DichVuDAO.getInstance().getAll();
//
//        ObservableList<ChiTietDichVu> dsChiTietDichVu = FXCollections.observableArrayList();
//        ChiTietDichVu chiTietDichVu = null;
//
//        String sql = "SELECT ma_dv, ngay_dv, so_luong, thanh_tien, ghi_chu FROM chi_tiet_dich_vu WHERE ma_dat_phong=? AND ma_phong=?";
//
//        Connection con = DbConnection.getConnection();
//        ResultSet rs;
//
//        try {
//            PreparedStatement stmt = con.prepareStatement(sql);
//            stmt.setInt(1, chiTietDatPhong.getDatPhong().getMaDatPhong());
//            stmt.setInt(2, chiTietDatPhong.getPhong().getMaPhong());
//            rs = stmt.executeQuery();
//
//            while(rs.next()) {
//                chiTietDichVu = new ChiTietDichVu(
//                        getDichVu(rs.getInt(1), dsDichVu),
//                        chiTietDatPhong.getDatPhong(),
//                        chiTietDatPhong.getPhong(),
//                        rs.getTimestamp(2),
//                        rs.getInt(3),
//                        rs.getLong(4),
//                        rs.getNString(5)
//                );
//                dsChiTietDichVu.add(chiTietDichVu);
//            }
//            rs.close();
//            stmt.close();
//            con.close();
//            chiTietDatPhong.setDsDichVuSuDung(dsChiTietDichVu);
//
//        } catch (SQLException e) {
//            ExHandler.handle(e);
//        }
//
//        return dsChiTietDichVu;
//    }
}
