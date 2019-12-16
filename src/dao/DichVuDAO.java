package dao;

import model.DichVu;
import util.DbConnection;
import util.ExHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DichVuDAO {

    private static DichVuDAO instance = new DichVuDAO();

    private DichVuDAO() {}

    public static DichVuDAO getInstance() {
        return instance;
    }

    public boolean create(DichVu dichVu) {
        String sql = "INSERT INTO dich_vu(ten_dv, gia_dv, don_vi_tinh, ghi_chu) VALUES (?, ?, ?, ?)";

        Connection con = DbConnection.getConnection();

        boolean ketQua = false;
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setNString(1, dichVu.getTenDv());
            stmt.setLong(2, dichVu.getGiaDv());
            stmt.setNString(3, dichVu.getDonVi());
            stmt.setNString(4, dichVu.getGhiChu());

            ketQua = (stmt.executeUpdate() > 0);

            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }
        return ketQua;
    }

    public DichVu get(int maDv) {
        String sql = "SELECT * FROM dich_vu WHERE ma_dv=?";

        Connection con = DbConnection.getConnection();
        DichVu dichVu = null;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setNString(1, dichVu.getTenDv());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                dichVu = new DichVu(
                        rs.getInt("ma_dv"),
                        rs.getNString("ten_dv"),
                        rs.getNString("don_vi_tinh"),
                        rs.getLong("gia_dv"),
                        rs.getNString("ghi_chu")
                );
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }
        return dichVu;
    }

    public ArrayList<DichVu> getAll() {
        String sql = "SELECT * FROM dich_vu";
        ArrayList<DichVu> dsDichVu = new ArrayList<>();

        Connection con = DbConnection.getConnection();
        DichVu dichVu = null;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                dichVu = new DichVu(
                        rs.getInt("ma_dv"),
                        rs.getNString("ten_dv"),
                        rs.getNString("don_vi_tinh"),
                        rs.getLong("gia_dv"),
                        rs.getNString("ghi_chu")
                );
                dsDichVu.add(dichVu);
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }
        return dsDichVu;
    }

    public boolean update(DichVu dichVu) {
        String sql = "UPDATE dich_vu SET ten_dv=?, gia_dv=?, don_vi_tinh=?, ghi_chu=? WHERE ma_dv=?";

        Connection con = DbConnection.getConnection();

        boolean ketQua = false;
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setNString(1, dichVu.getTenDv());
            stmt.setLong(2, dichVu.getGiaDv());
            stmt.setNString(3, dichVu.getDonVi());
            stmt.setNString(4, dichVu.getGhiChu());
            stmt.setInt(5, dichVu.getMaDv());

            ketQua = (stmt.executeUpdate() > 0);

            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }
        return ketQua;
    }

    public boolean delete(DichVu dichVu) {
        String sql = "DELETE FROM dich_vu WHERE ma_dv=?";

        Connection con = DbConnection.getConnection();

        boolean ketQua = false;
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, dichVu.getMaDv());

            ketQua = (stmt.executeUpdate() > 0);

            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }
        return ketQua;
    }
}
