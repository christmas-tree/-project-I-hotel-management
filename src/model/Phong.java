package model;


import javafx.beans.property.IntegerProperty;

import java.util.ArrayList;

public class Phong {

    // TRANG THAI
    public static final int SANSANG = 0;
    public static final int DANGSUDUNG = 1;
    public static final int DANGDON = 2;
    public static final int DANGSUACHUA = 3;

    private int maPhong;
    private LoaiPhong loaiPhong;
    private int tang;
    private IntegerProperty trangThai;
    private String ghiChu;

    public static final String[] dsTrangThai = {"Sẵn sàng", "Đang sử dụng", "Đang dọn", "Đang sửa chữa"};
    private ArrayList<ChiTietPhong> dsChiTietPhong;

    public Phong() {
    }

    public Phong(int maPhong, LoaiPhong loaiPhong, int tang, int trangThai, String ghiChu) {
        this.maPhong = maPhong;
        this.loaiPhong = loaiPhong;
        this.tang = tang;
        this.trangThai.set(trangThai);
        this.ghiChu = ghiChu;
    }

    public int getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(int maPhong) {
        this.maPhong = maPhong;
    }

    public LoaiPhong getLoaiPhong() {
        return loaiPhong;
    }

    public void setLoaiPhong(LoaiPhong loaiPhong) {
        this.loaiPhong = loaiPhong;
    }

    public int getTang() {
        return tang;
    }

    public void setTang(int tang) {
        this.tang = tang;
    }

    public int getTrangThai() {
        return trangThai.get();
    }

    public IntegerProperty trangThaiProperty() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai.set(trangThai);
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public ArrayList<ChiTietPhong> getDsChiTietPhong() {
        return dsChiTietPhong;
    }

    public void setDsChiTietPhong(ArrayList<ChiTietPhong> dsChiTietPhong) {
        this.dsChiTietPhong = dsChiTietPhong;
    }

    public void setTrangThai(String trangThai) {
        for (int i = 0; i < dsTrangThai.length; i++) {
            if (trangThai.equals(dsTrangThai[i])) {
                this.trangThai.set(i);
                break;
            }
        }
    }
}
