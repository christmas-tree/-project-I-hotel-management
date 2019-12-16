package dao;

import model.KhachHang;
import util.DbConnection;
import util.ExHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class KhachHangDAO {
    private static KhachHangDAO instance = new KhachHangDAO();

    public static KhachHangDAO getInstance() {
        return instance;
    }

    public boolean create(KhachHang khachHang) {

        String sql = "INSERT INTO khach_hang(ten_khach, gioi_tinh, cmnd, dien_thoai, email, dia_chi, ghi_chu) OUTPUT inserted.ma_kh VALUES (?, ?, ?, ?, ?, ?, ?)";

        int id = 0;

        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setNString(1, khachHang.getTenKhach());
            stmt.setBoolean(2, khachHang.getGioiTinh());
            stmt.setLong(3, khachHang.getCmnd());
            stmt.setLong(4, khachHang.getDienThoai());
            stmt.setString(5, khachHang.getEmail());
            stmt.setNString(6, khachHang.getDiaChi());
            stmt.setNString(7, khachHang.getGhiChu());

            ResultSet rs = stmt.executeQuery();

            while (rs.next())
                id = rs.getInt(1);
            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }
        if (id != 0) {
            khachHang.setMaKh(id);
            return true;
        } else
            return false;
    }

    public ArrayList<KhachHang> getAll() {
        ArrayList<KhachHang> dsKhachHang = new ArrayList<>();
        KhachHang khachHang = null;

        String sql = "SELECT * FROM khach_hang";

        Connection con = DbConnection.getConnection();
        ResultSet rs;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                khachHang = new KhachHang(
                        rs.getInt("ma_kh"),
                        rs.getNString("ten_khachHang"),
                        rs.getBoolean("gioi_tinh"),
                        rs.getLong("cmnd"),
                        rs.getLong("dien_thoai"),
                        rs.getString("email"),
                        rs.getNString("dia_chi"),
                        rs.getNString("ghi_chu")
                );
                dsKhachHang.add(khachHang);
            }
            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException e) {
            ExHandler.handle(e);
        }

        return dsKhachHang;
    }

    public KhachHang get(int maKh) {
        KhachHang khachHang = null;

        String sql = "SELECT * FROM khach_hang WHERE ma_kh=?";

        Connection con = DbConnection.getConnection();
        ResultSet rs;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, maKh);

            rs = stmt.executeQuery();
            while (rs.next()) {
                khachHang = new KhachHang(
                        rs.getInt("ma_kh"),
                        rs.getNString("ten_khachHang"),
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

        } catch (SQLException e) {
            ExHandler.handle(e);
        }

        return khachHang;
    }

    public ArrayList<KhachHang> search(int searchType, String value) {
        ArrayList<KhachHang> searchResult = new ArrayList<>();
        KhachHang khachHang;

        String sql = "SELECT * FROM khach_hang";
        Connection con;
        PreparedStatement stmt;
        ResultSet rs;

//        String searchChoices[] = {"Mã khách hàng", "Tên", "Giới tính", "CMND", "Điện thoại", "Email"};
        //                              0           1          2          3         4           5
        con = DbConnection.getConnection();
        try {
            switch (searchType) {
                case 0:
                    sql += " WHERE ma_kh=?";
                    stmt = con.prepareStatement(sql);
                    try {
                        stmt.setInt(1, Integer.parseInt(value));
                    } catch (NumberFormatException e) {
                        ExHandler.handle(e);
                    }
                    break;
                case 1: // Ten
                    sql += " WHERE [ten_khach] LIKE ?";
                    con = DbConnection.getConnection();
                    stmt = con.prepareStatement(sql);
                    stmt.setNString(1, "%" + value + "%");
                    break;

                case 2: // Gioi Tinh
                    sql += " WHERE [gioi_tinh]=?";
                    con = DbConnection.getConnection();
                    stmt = con.prepareStatement(sql);
                    stmt.setBoolean(1, (value == "Nam") ? true : false);
                    break;

                case 3: // Cmnd
                    sql += " WHERE cmnd=?";
                    stmt = con.prepareStatement(sql);
                    try {
                        stmt.setLong(1, Long.parseLong(value));
                    } catch (NumberFormatException e) {
                        ExHandler.handle(e);
                    }
                    break;
                case 4: // Dien thoai
                    sql += " WHERE dien_thoai=?";
                    stmt = con.prepareStatement(sql);
                    try {
                        stmt.setLong(1, Long.parseLong(value));
                    } catch (NumberFormatException e) {
                        ExHandler.handle(e);
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Illegal Searching Method.");
            }
            rs = stmt.executeQuery();

            while (rs.next()) {
                khachHang = new KhachHang(
                        rs.getInt("ma_kh"),
                        rs.getNString("ten_khachHang"),
                        rs.getBoolean("gioi_tinh"),
                        rs.getLong("cmnd"),
                        rs.getLong("dien_thoai"),
                        rs.getString("email"),
                        rs.getNString("dia_chi"),
                        rs.getNString("ghi_chu")
                );
                searchResult.add(khachHang);
            }
            rs.close();
            stmt.close();
            con.close();
            return searchResult;

        } catch (SQLException e) {
            ExHandler.handle(e);
            return null;
        }
    }

    public boolean update(KhachHang khachHang) {
        String sql = "UPDATE khach_hang(ten_khachHang, gioi_tinh, cmnd, dien_thoai, email, dia_chi, ghi_chu) SET (?, ?, ?, ?, ?, ?, ?) WHERE ma_kh=?";

        boolean result = false;

        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setNString(1, khachHang.getTenKhach());
            stmt.setBoolean(2, khachHang.getGioiTinh());
            stmt.setLong(3, khachHang.getCmnd());
            stmt.setLong(4, khachHang.getDienThoai());
            stmt.setString(5, khachHang.getEmail());
            stmt.setNString(6, khachHang.getDiaChi());
            stmt.setNString(7, khachHang.getGhiChu());
            stmt.setInt(8, khachHang.getMaKh());

            result = (stmt.executeUpdate() > 0);

            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }

        return result;
    }

    public boolean delete(KhachHang khachHang) {
        String sql = "DELETE FROM khach_hang WHERE ma_kh=?";

        boolean result = false;

        Connection con = DbConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, khachHang.getMaKh());

            result = (stmt.executeUpdate() > 0);

            stmt.close();
            con.close();
        } catch (SQLException e) {
            ExHandler.handle(e);
        }

        return result;
    }

    public void importKhachHang(ArrayList<KhachHang> dsKhachHang) {
        String sql = "INSERT INTO khach_hang(ten_khach, gioi_tinh, cmnd, dien_thoai, email, dia_chi, ghi_chu) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection con = DbConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            String err = "";
            KhachHang khachHang = null;
            for (int i = 0; i < dsKhachHang.size(); i++) {
                khachHang = dsKhachHang.get(i);
                try {
                    stmt.setNString(1, khachHang.getTenKhach());
                    stmt.setBoolean(2, khachHang.getGioiTinh());
                    stmt.setLong(3, khachHang.getCmnd());
                    stmt.setLong(4, khachHang.getDienThoai());
                    stmt.setString(5, khachHang.getEmail());
                    stmt.setNString(6, khachHang.getDiaChi());
                    stmt.setNString(7, khachHang.getGhiChu());

                    stmt.executeUpdate();
                } catch (SQLException e) {
                    err += "Có vấn đề nhập độc giả số " + (i+1) + " - "+ khachHang.getTenKhach() + ".\n";
                }
            }
            stmt.close();
            con.close();

            if (!err.isBlank())
                ExHandler.handleLong(err);
        } catch (SQLException e) {
            ExHandler.handle(e);
        }
    }
}
