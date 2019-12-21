package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.ChiTietDatPhong;
import model.ChiTietDichVu;
import model.DichVu;
import util.DbConnection;
import util.ExHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChiTietDichVuDAO {
    private static ChiTietDichVuDAO instance = new ChiTietDichVuDAO();

    public static ChiTietDichVuDAO getInstance() {return instance;}

    public boolean create(ChiTietDichVu chiTietDichVu) {
        String sql = "INSERT INTO chi_tiet_dich_vu(ma_dv, ma_dat_phong, ma_phong, ngay_dv, so_luong, thanh_tien, ghi_chu) VALUES (?, ?, ?, ?, ?, ?, ?)";
        boolean result = false;
        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, chiTietDichVu.getDichVu().getMaDv());
            stmt.setInt(2, chiTietDichVu.getDatPhong().getMaDatPhong());
            stmt.setInt(3, chiTietDichVu.getPhong().getMaPhong());
            stmt.setTimestamp(4, chiTietDichVu.getNgayDv());
            stmt.setInt(5, chiTietDichVu.getSoLuong());
            stmt.setLong(6, chiTietDichVu.getThanhTien());
            stmt.setNString(7, chiTietDichVu.getGhiChu());

            result = (stmt.executeUpdate() > 0);

            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }

        return result;
    }

    public ObservableList<ChiTietDichVu> getAll(ChiTietDatPhong chiTietDatPhong) {
        ArrayList<DichVu> dsDichVu = DichVuDAO.getInstance().getAll();

        ObservableList<ChiTietDichVu> dsChiTietDichVu = FXCollections.observableArrayList();
        ChiTietDichVu chiTietDichVu = null;

        String sql = "SELECT ma_dv, ngay_dv, so_luong, thanh_tien, ghi_chu FROM chi_tiet_dich_vu WHERE ma_dat_phong=? AND ma_phong=?";

        Connection con = DbConnection.getConnection();
        ResultSet rs;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, chiTietDatPhong.getDatPhong().getMaDatPhong());
            stmt.setInt(2, chiTietDatPhong.getPhong().getMaPhong());
            rs = stmt.executeQuery();

            while(rs.next()) {
                chiTietDichVu = new ChiTietDichVu(
                        getDichVu(rs.getInt(1), dsDichVu),
                        chiTietDatPhong.getDatPhong(),
                        chiTietDatPhong.getPhong(),
                        rs.getTimestamp(2),
                        rs.getInt(3),
                        rs.getLong(4),
                        rs.getNString(5)
                );
                dsChiTietDichVu.add(chiTietDichVu);
            }
            rs.close();
            stmt.close();
            con.close();
            chiTietDatPhong.setDsDichVuSuDung(dsChiTietDichVu);

        } catch (SQLException e) {
            ExHandler.handle(e);
        }

        return dsChiTietDichVu;
    }

    public DichVu getDichVu(int maDv, ArrayList<DichVu> dsDichVu) {
        for (int i=0 ; i<dsDichVu.size(); i++) {
            if (dsDichVu.get(i).getMaDv() == maDv)
                return dsDichVu.get(i);
        }
        return null;
    }
}
