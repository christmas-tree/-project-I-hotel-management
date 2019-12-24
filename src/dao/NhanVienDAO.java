package dao;

import model.NhanVien;
import util.DbConnection;
import util.ExceptionHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class NhanVienDAO {

    private static NhanVienDAO instance = new NhanVienDAO();

    private NhanVienDAO() {
    }

    public static NhanVienDAO getInstance() {
        return instance;
    }

    public NhanVien get(int maNv) {
        NhanVien nhanVien = null;

        String sql = "SELECT * FROM nhan_vien WHERE ma_nv=?";

        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, maNv);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                nhanVien = new NhanVien(
                        rs.getInt("ma_nv"),
                        rs.getNString("ten_nv"),
                        rs.getInt("loai_nv"),
                        rs.getNString("ten_dang_nhap"),
                        rs.getBoolean("gioi_tinh"),
                        rs.getLong("cmnd"),
                        rs.getLong("dien_thoai"),
                        rs.getString("email"),
                        rs.getNString("dia_chi"),
                        rs.getNString("ghi_chu")
                );
            }
            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExceptionHandler.handle(e);
        }
        return nhanVien;
    }

    public ArrayList<NhanVien> getAll() {
        ArrayList<NhanVien> dsNhanVien = new ArrayList<>();
        NhanVien nhanVien = null;

        String sql = "SELECT * FROM nhan_vien";

        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                nhanVien = new NhanVien(
                        rs.getInt("ma_nv"),
                        rs.getNString("ten_nv"),
                        rs.getInt("loai_nv"),
                        rs.getNString("ten_dang_nhap"),
                        rs.getBoolean("gioi_tinh"),
                        rs.getLong("cmnd"),
                        rs.getLong("dien_thoai"),
                        rs.getString("email"),
                        rs.getNString("dia_chi"),
                        rs.getNString("ghi_chu")
                );
                dsNhanVien.add(nhanVien);
            }

            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExceptionHandler.handle(e);
        }
        return dsNhanVien;
    }

    public ArrayList<NhanVien> search(int searchMethod, String value)
            throws SQLException, NumberFormatException {

        ArrayList<NhanVien> searchResult = new ArrayList<>();
        NhanVien nhanVien;

        Connection con;
        PreparedStatement stmt;
        ResultSet rs;

//      String searchChoices[] = {"Mã nhân viên", "Tên", "Giới tính", "Vị trí", "Tên đăng nhập", "Email"};
        //                              0             1         2         3         4              5
        switch (searchMethod) {

            case 0:
                con = DbConnection.getConnection();
                stmt = con.prepareStatement("SELECT * FROM [nhan_vien] WHERE ma_nv=?");
                stmt.setInt(1, Integer.parseInt(value));
                rs = stmt.executeQuery();
                break;
            case 1:
                con = DbConnection.getConnection();
                stmt = con.prepareStatement("SELECT * FROM [nhan_vien] WHERE ten_nv LIKE ?");
                stmt.setNString(1, "%" + value + "%");
                rs = stmt.executeQuery();
                break;
            case 2:
                con = DbConnection.getConnection();
                stmt = con.prepareStatement("SELECT * FROM [nhan_vien] WHERE gioi_tinh=?");
                stmt.setBoolean(1, value=="Nam"?true:false);
                rs = stmt.executeQuery();
                break;
            case 3:
                con = DbConnection.getConnection();
                stmt = con.prepareStatement("SELECT * FROM [nhan_vien] WHERE loai_nv=?");
                stmt.setInt(1, value=="Quản lý"?0:1);
                rs = stmt.executeQuery();
                break;
            case 4:
                con = DbConnection.getConnection();
                stmt = con.prepareStatement("SELECT * FROM [nhan_vien] WHERE ten_dang_nhap LIKE ?");
                stmt.setNString(1, "%" + value + "%");
                rs = stmt.executeQuery();
                break;

            case 5:
                con = DbConnection.getConnection();
                stmt = con.prepareStatement("SELECT * FROM [nhan_vien] WHERE email LIKE ?");
                stmt.setNString(1, "%" + value + "%");
                rs = stmt.executeQuery();
                break;

            default:
                throw new IllegalArgumentException("Illegal Searching Method.");
        }

        while (rs.next()) {
            nhanVien = new NhanVien(
                    rs.getInt("ma_nv"),
                    rs.getNString("ten_nv"),
                    rs.getInt("loai_nv"),
                    rs.getNString("ten_dang_nhap"),
                    rs.getBoolean("gioi_tinh"),
                    rs.getLong("cmnd"),
                    rs.getLong("dien_thoai"),
                    rs.getString("email"),
                    rs.getNString("dia_chi"),
                    rs.getNString("ghi_chu")
            );
            searchResult.add(nhanVien);
        }
        rs.close();
        stmt.close();
        con.close();

        return searchResult;
    }

    public boolean create(NhanVien nhanVien) {

        String sql = "INSERT INTO nhan_vien(ten_nv, loai_nv, ten_dang_nhap, mat_khau, gioi_tinh, cmnd, dien_thoai, email, dia_chi, ghi_chu)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection con = DbConnection.getConnection();

        boolean ketQua = false;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setNString(1, nhanVien.getTenNv());
            stmt.setInt(2, nhanVien.getLoaiNv());
            stmt.setNString(3, nhanVien.getTenDangNhap());
            stmt.setNString(4, nhanVien.getMatKhau());
            stmt.setBoolean(5, nhanVien.getGioiTinh());
            stmt.setLong(6, nhanVien.getCmnd());
            stmt.setLong(7, nhanVien.getDienThoai());
            stmt.setString(8, nhanVien.getEmail());
            stmt.setNString(9, nhanVien.getDiaChi());
            stmt.setNString(10, nhanVien.getGhiChu());

            ketQua = (stmt.executeUpdate() > 0);

            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExceptionHandler.handle(e);
        }
        return ketQua;
    }

    public boolean update(NhanVien nhanVien) {

        String sql;
        if (nhanVien.getMatKhau() == null)
            sql = "UPDATE nhan_vien " +
                    "SET ten_nv=?, loai_nv=?, ten_dang_nhap=?, gioi_tinh=?, cmnd=?, dien_thoai=?, email=?, dia_chi=?, ghi_chu=? " +
                    "WHERE ma_nv=?";
        else
            sql = "UPDATE nhan_vien " +
                    "SET ten_nv=?, loai_nv=?, ten_dang_nhap=?, gioi_tinh=?, cmnd=?, dien_thoai=?, email=?, dia_chi=?, ghi_chu=?, mat_khau=?" +
                    "WHERE ma_nv=?";

        Connection con = DbConnection.getConnection();

        boolean ketQua = false;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setNString(1, nhanVien.getTenNv());
            stmt.setInt(2, nhanVien.getLoaiNv());
            stmt.setNString(3, nhanVien.getTenDangNhap());
            stmt.setBoolean(4, nhanVien.getGioiTinh());
            stmt.setLong(5, nhanVien.getCmnd());
            stmt.setLong(6, nhanVien.getDienThoai());
            stmt.setString(7, nhanVien.getEmail());
            stmt.setNString(8, nhanVien.getDiaChi());
            stmt.setNString(9, nhanVien.getGhiChu());
            if (nhanVien.getMatKhau() == null) {
                stmt.setInt(10, nhanVien.getMaNv());
            } else {
                stmt.setNString(10, nhanVien.getMatKhau());
                stmt.setNString(11, nhanVien.getMatKhau());
            }

            ketQua = (stmt.executeUpdate() > 0);

            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExceptionHandler.handle(e);
        }
        return ketQua;
    }

    public boolean delete(NhanVien nhanVien) {
        String sql = "DELETE FROM nhan_vien WHERE ma_nv = ?";
        boolean result = false;

        Connection con = DbConnection.getConnection();
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, nhanVien.getMaNv());
            result = (stmt.executeUpdate() > 0);
            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExceptionHandler.handle(e);
        }

        return result;
    }

    public static NhanVien authenticate(String username, String password) throws SQLException {
        Connection con = DbConnection.getConnection();

        String sql = "SELECT * FROM [nhan_vien] WHERE [ten_dang_nhap] = ? AND [mat_khau] = HASHBYTES('SHA2_256', ?)";

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setNString(2, password);
        ResultSet rs = stmt.executeQuery();

        NhanVien user = null;

        if (rs.next()) {
            user = new NhanVien(
                    rs.getInt("ma_nv"),
                    rs.getNString("ten_nv"),
                    rs.getInt("loai_nv"),
                    rs.getNString("ten_dang_nhap"),
                    rs.getBoolean("gioi_tinh"),
                    rs.getLong("cmnd"),
                    rs.getLong("dien_thoai"),
                    rs.getString("email"),
                    rs.getNString("dia_chi"),
                    rs.getNString("ghi_chu")
            );
        }

        rs.close();
        stmt.close();
        con.close();

        return user;
    }
}
