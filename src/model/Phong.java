package model;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

import java.util.Objects;

public class Phong {

    // TRANG THAI
    public static final int SANSANG = 0;
    public static final int DANGSUDUNG = 1;
    public static final int DANGDON = 2;
    public static final int DANGSUACHUA = 3;

    private int maPhong;
    private LoaiPhong loaiPhong;
    private int tang;
    private IntegerProperty trangThai = new SimpleIntegerProperty();
    private String ghiChu;

    public static final String[] dsTrangThai = {"Sẵn sàng", "Đang sử dụng", "Đang dọn", "Đang sửa chữa"};
    private ObservableList<ChiTietPhong> dsChiTietPhong;

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

    public String getTrangThaiString() {
        return dsTrangThai[trangThai.get()];
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

    public ObservableList<ChiTietPhong> getDsChiTietPhong() {
        return dsChiTietPhong;
    }

    public void setDsChiTietPhong(ObservableList<ChiTietPhong> dsChiTietPhong) {
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

    @Override
    public String toString() {
        return maPhong + " - " + loaiPhong.getMaLoaiPhong() + " - " + String.format("%,3d", loaiPhong.getGiaTien());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Phong)) return false;
        Phong phong = (Phong) o;
        return maPhong == phong.maPhong;
    }
}
