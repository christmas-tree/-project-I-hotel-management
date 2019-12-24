package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import util.DbConnection;
import util.ExceptionHandler;

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
            ExceptionHandler.handle(e);
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
            ExceptionHandler.handle(e);
        }

        return dsChiTietDichVu;
    }

    public ObservableList<ChiTietDichVu> getAll(DatPhong datPhong, ArrayList<Phong> dsPhong) {
        ArrayList<DichVu> dsDichVu = DichVuDAO.getInstance().getAll();

        ObservableList<ChiTietDichVu> dsChiTietDichVu = FXCollections.observableArrayList();
        ChiTietDichVu chiTietDichVu = null;

        String sql = "SELECT ma_dv, ma_phong, ngay_dv, so_luong, thanh_tien, ghi_chu FROM chi_tiet_dich_vu WHERE ma_dat_phong=? ORDER BY ma_phong";

        Connection con = DbConnection.getConnection();
        ResultSet rs;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, datPhong.getMaDatPhong());
            rs = stmt.executeQuery();

            while(rs.next()) {
                chiTietDichVu = new ChiTietDichVu(
                        getDichVu(rs.getInt(1), dsDichVu),
                        datPhong,
                        getPhong(rs.getInt(2), dsPhong),
                        rs.getTimestamp(3),
                        rs.getInt(4),
                        rs.getLong(5),
                        rs.getNString(6)
                );
                dsChiTietDichVu.add(chiTietDichVu);
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExceptionHandler.handle(e);
        }

        return dsChiTietDichVu;
    }

    public ObservableList<ChiTietDichVu> getByPhong(Phong phong, ArrayList<DichVu> dsDichVu) {
        ObservableList<ChiTietDichVu> dsChiTietDichVu = FXCollections.observableArrayList();
        ChiTietDichVu chiTietDichVu = null;

        String sql = "SELECT ma_dv, ma_dat_phong, ngay_dv, so_luong, thanh_tien, ghi_chu FROM chi_tiet_dich_vu, dat_phong WHERE dat_phong.ma_dat_phong=chi_tiet_dich_vu.ma_dat_phong AND da_xong=0 AND ma_phong=?";

        Connection con = DbConnection.getConnection();
        ResultSet rs;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, phong.getMaPhong());
            rs = stmt.executeQuery();

            while(rs.next()) {
                chiTietDichVu = new ChiTietDichVu(
                        getDichVu(rs.getInt(1), dsDichVu),
                        new DatPhong(rs.getInt(2)),
                        phong,
                        rs.getTimestamp(3),
                        rs.getInt(4),
                        rs.getLong(5),
                        rs.getNString(6)
                );
                dsChiTietDichVu.add(chiTietDichVu);
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExceptionHandler.handle(e);
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

    public Phong getPhong(int maPhong, ArrayList<Phong> dsPhong) {
        for (int i=0 ; i<dsPhong.size(); i++) {
            if (dsPhong.get(i).getMaPhong() == maPhong)
                return dsPhong.get(i);
        }
        return null;
    }

    public boolean update(ObservableList<ChiTietDichVu> dsChiTietDichVu, ArrayList<ChiTietDichVu> dsXoa) {

        String sql = "IF NOT EXISTS (SELECT * FROM [chi_tiet_dich_vu] WHERE ma_dv=? AND ma_dat_phong=? AND ma_phong=?) " +
                "INSERT INTO chi_tiet_dich_vu(ma_dv, ma_dat_phong, ma_phong, ngay_dv, so_luong, thanh_tien) VALUES (?, ?, ?, ?, ?, ?)";

        String sql2 = "DELETE FROM chi_tiet_dich_vu WHERE ma_dv=? AND ma_dat_phong=? AND ma_phong=? AND ngay_dv=?";

        Connection con = DbConnection.getConnection();
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            ChiTietDichVu chiTietDichVu = null;
            for (int i = 0; i < dsChiTietDichVu.size(); i++) {
                chiTietDichVu = dsChiTietDichVu.get(i);

                stmt.setInt(1, chiTietDichVu.getDichVu().getMaDv());
                stmt.setInt(2, chiTietDichVu.getDatPhong().getMaDatPhong());
                stmt.setInt(3, chiTietDichVu.getPhong().getMaPhong());
                stmt.setInt(4, chiTietDichVu.getDichVu().getMaDv());
                stmt.setInt(5, chiTietDichVu.getDatPhong().getMaDatPhong());
                stmt.setInt(6, chiTietDichVu.getPhong().getMaPhong());
                stmt.setTimestamp(7, chiTietDichVu.getNgayDv());
                stmt.setInt(8, chiTietDichVu.getSoLuong());
                stmt.setLong(9, chiTietDichVu.getThanhTien());

                stmt.executeUpdate();
            }
            stmt.close();

            stmt = con.prepareStatement(sql2);
            for (int i = 0; i < dsXoa.size(); i++) {
                chiTietDichVu = dsXoa.get(i);

                stmt.setInt(1, chiTietDichVu.getDichVu().getMaDv());
                stmt.setInt(2, chiTietDichVu.getDatPhong().getMaDatPhong());
                stmt.setInt(3, chiTietDichVu.getPhong().getMaPhong());
                stmt.setTimestamp(4, chiTietDichVu.getNgayDv());

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
