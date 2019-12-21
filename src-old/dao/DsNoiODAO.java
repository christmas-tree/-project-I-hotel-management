package dao;

import model.ChiTietDatPhong;
import model.KhachHang;
import util.DbConnection;
import util.ExHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class DsNoiODAO {

    private static DsNoiODAO instance = new DsNoiODAO();

    public static DsNoiODAO getInstance() {
        return instance;
    }


}
